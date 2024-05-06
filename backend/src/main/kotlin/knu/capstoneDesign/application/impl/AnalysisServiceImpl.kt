package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.entity.Analysis
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AnalysisServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val analysisRepository: AnalysisRepository,
    @Value("\${ai.server}")
    private val aiServerUrl: String
) {

    fun get(diary: Diary, userId: Long):ResponseEntity<String>{

        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON

        val request = "{\"content\": \"${diary.content}\"}"

        val requestEntity = HttpEntity(request, httpHeaders)

        val responseEntity = restTemplate.postForEntity(
            "$aiServerUrl/sentiment", requestEntity, String::class.java)

        analysisRepository.save(
            Analysis(id = null, diary = diary, isPositive = (responseEntity.body == "positive"), summary = "요약입니다.")
        )


        return ResponseEntity.ok(responseEntity.body)
    }
}