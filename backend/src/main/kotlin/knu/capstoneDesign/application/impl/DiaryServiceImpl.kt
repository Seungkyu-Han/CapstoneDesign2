package knu.capstoneDesign.application.impl

import com.fasterxml.jackson.databind.ObjectMapper
import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTReq
import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.data.enum.Emotion
import knu.capstoneDesign.repository.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

open class DiaryServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${chatGPT.analysis}")
    private val chatGPTAnalysis: String,
    @Value("\${ai.server}")
    private val aiServerUrl: String,
    private val redisTemplate: RedisTemplate<String, String>,
    private val consultingRepository: ConsultingRepository
):DiaryService {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    override fun post(diaryPostReq: DiaryPostReq): ResponseEntity<Int> {

        val user = getEmptyUserById(diaryPostReq.userId)

        val diary = Diary(
            id = null,
            user = user,
            date = diaryPostReq.date,
            title = diaryPostReq.title,
            content = diaryPostReq.content
        )

        val diaryContent = diaryPostReq.content?.replace("\n", "\\n")

        diaryRepository.save(diary)

        val analysisFromChatGPTRequest = CompletableFuture.supplyAsync{
            requestAnalysisToChatGPT(diaryContent + chatGPTAnalysis, diaryPostReq.userId, 0)
        }

        CompletableFuture.supplyAsync{
            requestAnalysis(diaryContent ?: "")
        }.thenApply {
            analysis ->
                analysisRepository.save(Analysis(id = null, diary = diary, emotion = Emotion.valueOf(analysis), analysisFromChatGPTRequest.get()))
        }.thenRunAsync {
            disconnectChatGPT(diary.id ?: 0)
        }

        return ResponseEntity.ok(diary.id)
    }

    override fun get(id: Int): ResponseEntity<DiaryGetRes> {

        val diary = diaryRepository.findById(id).orElseThrow{NullPointerException()}

        return ResponseEntity.ok(DiaryGetRes(id = diary.id ?: 0, date = diary.date ?: LocalDate.now(), title = diary.title ?: "", content = diary.content ?: ""))
    }

    override fun patch(diaryPatchReq: DiaryPatchReq): ResponseEntity<HttpStatusCode> {

        val diary = diaryRepository.findById(diaryPatchReq.id).orElseThrow {NullPointerException()}

        diary.title = diaryPatchReq.title
        diary.content = diaryPatchReq.content ?: diary.content

        diaryRepository.save(diary)

        val diaryContent = diaryPatchReq.content?.replace("\n", "\\n")
        val userId = diary.user?.id ?: 0

        val analysisFromChatGPTRequest = CompletableFuture.supplyAsync{
            println("REQUEST ANALYSIS TO CHATGPT")
            requestAnalysisToChatGPT(diaryContent + chatGPTAnalysis, userId, 0)
        }


        CompletableFuture
            .runAsync {
                println("analysis Delete: ${analysisRepository.deleteByDiary(diary)}")
                println("consulting Delete: ${consultingRepository.deleteByDiary(Diary(diaryPatchReq.id))}")
            }
            .thenApplyAsync{ requestAnalysis(diaryContent ?: "") }
            .thenApply {
                analysis ->
            analysisRepository.save(Analysis(id = null, diary = diary, emotion = Emotion.valueOf(analysis), analysisFromChatGPTRequest.get()))
        }.thenRunAsync {
                disconnectChatGPT(diary.id ?: 0)
        }

        return ResponseEntity.ok().build()
    }

    @Transactional
    override fun delete(id: Int): ResponseEntity<HttpStatusCode> {
        diaryRepository.deleteById(id)

        return ResponseEntity.ok().build()
    }

    override fun getList(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): ResponseEntity<List<DiaryGetListRes>> {

        return ResponseEntity
            .ok()
            .body(diaryRepository.findByUserIdAndDateBetween(userId, startDate, endDate))
    }

    override fun getMonth(userId: Long, year: Int, month: Int): ResponseEntity<List<DiaryGetListRes>> {
        val startDate = YearMonth.of(year, month).atDay(1)
        val endDate = YearMonth.of(year, month).atEndOfMonth()
        return this.getList(userId, startDate, endDate)
    }

    override fun getAll(userId: Long): ResponseEntity<List<DiaryGetListRes>> {
        return ResponseEntity
            .ok()
            .body(diaryRepository.findByUserId(userId))
    }

    private fun requestAnalysis(content: String): String{
        println(content)
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val request = "{\"content\": \"${content}\"}"

        val requestEntity = HttpEntity(request, httpHeaders)

        val responseEntity = restTemplate.postForEntity(
            "$aiServerUrl/sentiment", requestEntity, String::class.java)

        println(responseEntity)
        return responseEntity.body ?: throw ConnectException()
    }

    private fun getEmptyUserById(id: Long): User{
        return User(id = id, name = null, refreshToken = null)
    }

    private fun requestAnalysisToChatGPT(content: String, userId: Long, signnum: Int): String{
        println("REQUEST ANALYSIS CONTENT: $content")
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

    private fun disconnectChatGPT(diaryId: Int){
        redisTemplate.opsForValue().set(diaryId.toString(), "connect", 20, TimeUnit.MINUTES)
        scheduler.schedule({
            if(redisTemplate.opsForValue().get(diaryId.toString()) == null){
                requestAnalysisToChatGPT("연결 종료", diaryId.toLong(), 1)
            }
        }, 21, TimeUnit.MINUTES)
    }
}