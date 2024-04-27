package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.YearMonth

open class DiaryServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository):DiaryService {

    override fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode> {

        val user = getEmptyUserById(diaryPostReq.userId)

        val diary = Diary(
            id = null,
            user = user,
            date = diaryPostReq.date,
            title = diaryPostReq.title,
            content = diaryPostReq.content
        )

        diaryRepository.save(diary)


        return ResponseEntity.ok().build()
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

    private fun getEmptyUserById(id: Long): User{
        return User(id = id, name = null, refreshToken = null)
    }
}