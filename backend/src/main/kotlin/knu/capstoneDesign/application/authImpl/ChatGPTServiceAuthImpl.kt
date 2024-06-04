package knu.capstoneDesign.application.authImpl

import com.fasterxml.jackson.databind.ObjectMapper
import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTPostConsultingReq
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTReq
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTGetConsultingRes
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTPostConsultingRes
import knu.capstoneDesign.data.entity.Consulting
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.data.entity.UserConsulting
import knu.capstoneDesign.repository.ConsultingRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserConsultingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.*
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Service
class ChatGPTServiceAuthImpl(
    @Value("\${ai.server}")
    private val aiServerUrl: String,
    @Value("\${chatGPT.consult}")
    private val chatGPTConsult: String,
    private val diaryRepository: DiaryRepository,
    private val consultingRepository: ConsultingRepository,
    private val userConsultingRepository: UserConsultingRepository,
    private val redisTemplate: RedisTemplate<String, String>
): ChatGPTService {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    private fun requestChat(content: String, userId: Long, signnum: Int): String{
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val request = ChatGPTReq(content, userId, signnum)

        val objectMapper = ObjectMapper()
        val jsonRequest = objectMapper.writeValueAsString(request)

        val requestEntity = HttpEntity(jsonRequest, httpHeaders)

        val responseEntity = restTemplate.postForEntity(
            "$aiServerUrl/chatcomp", requestEntity, String::class.java
        )

        return responseEntity.body?.let {
            String(it.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
        } ?: throw ConnectException()
    }

    override fun getDiary(diaryId: Int, authentication: Authentication): ResponseEntity<ChatGPTDiaryRes> {
        val result = diaryRepository.existsByIdAndUser(diaryId, User(authentication.name.toLong()))
        if (!result) throw NullPointerException()
        return getDiary(diaryId, authentication.name.toLong())
    }

//    override fun getDiary(diaryId: Int, userId: Long): ResponseEntity<ChatGPTDiaryRes> {
//        val consulting = consultingRepository.findByDiary(Diary(diaryId))
//        if(consulting.isPresent){
//            return ResponseEntity(ChatGPTDiaryRes(consulting.get().content), HttpStatus.OK)
//        }
//        val diary = diaryRepository.findById(diaryId).orElseThrow { NullPointerException() }
//        val consultingFromGPT = requestChat(diary.content ?: "", diaryId.toLong(), 0)
//        consultingRepository.save(
//            Consulting(id = null, diary = Diary(diaryId), content = consultingFromGPT)
//        )
//        disconnectChatGPT(diaryId)
//
//        return ResponseEntity(ChatGPTDiaryRes(consultingFromGPT), HttpStatus.OK)
//    }

    override fun getDiary(diaryId: Int, userId: Long): ResponseEntity<ChatGPTDiaryRes> {

        val consultFromDatabaseRequest = CompletableFuture.supplyAsync{
            consultingRepository.findByDiary(Diary(diaryId))
        }.thenApply {
                consult ->
            if(consult.isEmpty){
                null
            }else{
                consult.get().content
            }
        }

        val consultFromGptRequest = CompletableFuture.supplyAsync{
            diaryRepository.findById(diaryId).orElseThrow{NullPointerException()}.content
        }.thenApply {
                diaryContent ->
            requestChat((diaryContent ?: "") + chatGPTConsult, diaryId.toLong(), 0)
        }

        return if(consultFromDatabaseRequest.get() != null){
            disconnectChatGPT(diaryId)
            ResponseEntity(ChatGPTDiaryRes(consultFromDatabaseRequest.get() ?: ""), HttpStatus.OK)
        } else{
            val consultFromGpt = consultFromGptRequest.get()
            consultingRepository.save(
                Consulting(id = null, diary = Diary(diaryId), content = consultFromGpt)
            )
            disconnectChatGPT(diaryId)

            ResponseEntity(ChatGPTDiaryRes(consultFromGpt), HttpStatus.OK)
        }
    }

    private fun disconnectChatGPT(diaryId: Int){
        redisTemplate.opsForValue().set(diaryId.toString(), "connect", 20, TimeUnit.MINUTES)
        scheduler.schedule({
            if(redisTemplate.opsForValue().get(diaryId.toString()) == null){
                requestChat("연결 종료", diaryId.toLong(), 1)
            }
        }, 21, TimeUnit.MINUTES)
    }



    override fun postConsult(chatGPTPostConsultingReq: ChatGPTPostConsultingReq, authentication: Authentication): ResponseEntity<ChatGPTPostConsultingRes> {
        val gptCompletableFuture = CompletableFuture.supplyAsync {
            requestChat(chatGPTPostConsultingReq.content, chatGPTPostConsultingReq.diaryId.toLong(), 0)
        }

        val diaryCompletableFuture = CompletableFuture.supplyAsync {
            diaryRepository.findById(chatGPTPostConsultingReq.diaryId).orElseThrow{NullPointerException("asdf")}
        }

        return CompletableFuture.allOf(gptCompletableFuture, diaryCompletableFuture).thenApply {
            val diary = diaryCompletableFuture.get()
            val consult = gptCompletableFuture.get()

            if(diary.user?.id != authentication.name.toLong())
                throw IllegalAccessException()

            val userConsulting = UserConsulting(id = null, diary = diary, localDateTime = LocalDateTime.now(), question = chatGPTPostConsultingReq.content, answer = consult)
            userConsultingRepository.save(userConsulting)

            disconnectChatGPT(chatGPTPostConsultingReq.diaryId)

            ResponseEntity(ChatGPTPostConsultingRes(userConsulting), HttpStatus.OK)
        }.get()
    }

    override fun getConsult(diaryId: Int, authentication: Authentication): ResponseEntity<List<ChatGPTGetConsultingRes>> {
        val diary = diaryRepository.findById(diaryId).orElseThrow { NullPointerException() }
        if(diary.user?.id != authentication.name.toLong())
            throw IllegalAccessException()
        return this.getConsult(diary)
    }

    override fun getConsult(diary: Diary): ResponseEntity<List<ChatGPTGetConsultingRes>> {
        return ResponseEntity(userConsultingRepository.findByDiary(diary)
            .map {
                userConsulting ->
                    ChatGPTGetConsultingRes(id = userConsulting.id ?: 0, localDateTime = userConsulting.localDateTime,
                    question = userConsulting.question, answer = userConsulting.answer)
            }, HttpStatus.OK)
    }

    override fun deleteConsult(userConsultingId: Int, authentication: Authentication): ResponseEntity<HttpStatus> {
        val userConsulting = userConsultingRepository.findById(userConsultingId).orElseThrow { NullPointerException() }
        if(userConsulting.diary.user?.id != authentication.name.toLong())
            throw IllegalAccessException()
        return deleteConsult(userConsulting)
    }

    override fun deleteConsult(userConsulting: UserConsulting): ResponseEntity<HttpStatus> {
        userConsultingRepository.delete(userConsulting)
        return ResponseEntity(HttpStatus.OK)
    }
}