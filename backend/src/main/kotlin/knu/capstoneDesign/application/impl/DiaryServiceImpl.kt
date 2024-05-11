package knu.capstoneDesign.application.impl

import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.data.enum.Emotion
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.net.ConnectException
import java.time.LocalDate
import java.time.YearMonth

open class DiaryServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${ai.server}")
    private val aiServerUrl: String):DiaryService {

    override fun post(diaryPostReq: DiaryPostReq): ResponseEntity<Int> {

        val user = getEmptyUserById(diaryPostReq.userId)

        val diary = Diary(
            id = null,
            user = user,
            date = diaryPostReq.date,
            title = diaryPostReq.title,
            content = diaryPostReq.content
        )


        diaryRepository.save(diary)

        kotlinx.coroutines.GlobalScope.launch {
            val analysisResult = requestAnalysis(diaryPostReq.content ?: "")
            withContext(Dispatchers.IO) {
                try{
                    analysisRepository.save(
                        Analysis(id = null, diary = diary,
                            emotion = Emotion.valueOf(analysisResult), "")
                    )
                }catch(_: DataIntegrityViolationException){}
            }

        }

        return ResponseEntity.ok(diary.id)
    }

    override fun get(id: Int): ResponseEntity<DiaryGetRes> {

        val diary = diaryRepository.findById(id).orElseThrow{NullPointerException()}

        return ResponseEntity.ok(DiaryGetRes(id = diary.id ?: 0, date = diary.date, title = diary.title ?: "", content = diary.content ?: ""))
    }

    override fun patch(diaryPatchReq: DiaryPatchReq): ResponseEntity<HttpStatusCode> {

        val diary = diaryRepository.findById(diaryPatchReq.id).orElseThrow {NullPointerException()}

        diary.title = diaryPatchReq.title
        diary.content = diaryPatchReq.content ?: diary.content

        diaryRepository.save(diary)

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
        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val request = "{\"content\": \"${content}\"}"

        val requestEntity = HttpEntity(request, httpHeaders)

        val responseEntity = restTemplate.postForEntity(
            "$aiServerUrl/sentiment", requestEntity, String::class.java)
        return responseEntity.body ?: throw ConnectException()
    }

    private fun getEmptyUserById(id: Long): User{
        return User(id = id, name = null, refreshToken = null)
    }
}