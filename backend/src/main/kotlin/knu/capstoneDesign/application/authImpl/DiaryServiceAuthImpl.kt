package knu.capstoneDesign.application.authImpl

import knu.capstoneDesign.application.impl.DiaryServiceImpl
import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryServiceAuthImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository
): DiaryServiceImpl(userRepository, diaryRepository) {

    fun post(diaryPostReq: DiaryPostReq, authentication: Authentication): ResponseEntity<HttpStatusCode> {
        val userId = authentication.name.toLong()
        diaryPostReq.userId = userId
        return super.post(diaryPostReq)
    }

    fun get(id: Int, authentication: Authentication): ResponseEntity<DiaryGetRes> {
        val result = diaryRepository.existsByIdAndUser(id, User(authentication.name.toLong()))
        if (!result) throw NullPointerException()
        return super.get(id)
    }

    fun patch(diaryPatchReq: DiaryPatchReq, authentication: Authentication): ResponseEntity<HttpStatusCode> {
        val result = diaryRepository.existsByIdAndUser(diaryPatchReq.id, User(authentication.name.toLong()))
        if (!result) throw NullPointerException()
        return super.patch(diaryPatchReq)
    }

    fun delete(id: Int, authentication: Authentication): ResponseEntity<HttpStatusCode> {
        val result = diaryRepository.existsByIdAndUser(id, User(authentication.name.toLong()))
        if (!result) throw NullPointerException()
        return super.delete(id)
    }

    fun getList(
        startDate: LocalDate,
        endDate: LocalDate,
        authentication: Authentication
    ): ResponseEntity<List<DiaryGetListRes>> {
        return super.getList(authentication.name.toLong(), startDate, endDate)
    }

    fun getMonth(year: Int, month: Int, authentication: Authentication): ResponseEntity<List<DiaryGetListRes>> {
        return super.getMonth(authentication.name.toLong(), year, month)
    }

    fun getAll(authentication: Authentication): ResponseEntity<List<DiaryGetListRes>> {
        return super.getAll(authentication.name.toLong())
    }
}