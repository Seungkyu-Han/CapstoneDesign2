package knu.capstoneDesign.application.authImpl

import com.fasterxml.jackson.databind.ObjectMapper
import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTReq
import knu.capstoneDesign.data.dto.chatGPT.res.ChatGPTDiaryRes
import knu.capstoneDesign.data.entity.Consulting
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.ConsultingRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserConsultingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.nio.charset.StandardCharsets
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
    override fun get(content: String, authentication: Authentication): ResponseEntity<String> {

        val result = requestChat(content, userId = authentication.name.toLong(), 0)


        return ResponseEntity.ok(result)
    }

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

    override fun getDiary(diaryId: Int, userId: Long): ResponseEntity<ChatGPTDiaryRes> {
        val diary = diaryRepository.findById(diaryId).orElseThrow { NullPointerException() }
        val consult = requestChat(diary.content + chatGPTConsult, userId, 0)

        consultingRepository.save(
            Consulting(id = null, diary = diary, content = consult)
        )

        disconnectChatGPT(diaryId)

        return ResponseEntity(ChatGPTDiaryRes(consult), HttpStatus.OK)
    }

    private fun disconnectChatGPT(diaryId: Int){
        redisTemplate.opsForValue().set(diaryId.toString(), "connect", 5, TimeUnit.SECONDS)
        scheduler.schedule({
            if(redisTemplate.opsForValue().get(diaryId.toString()) == null){
                requestChat("연결 종료", diaryId.toLong(), 1)
            }
        }, 10, TimeUnit.SECONDS)
    }
}