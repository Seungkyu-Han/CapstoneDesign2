package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DiaryServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository):DiaryService {

    override fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode> {

        val user = getEmptyUserById(diaryPostReq.userId)

        val diary = Diary(
            id = null,
            user = user,
            date = diaryPostReq.date,
            content = diaryPostReq.content
        )

        diaryRepository.save(diary)


        return ResponseEntity.ok().build()
    }

    override fun get(userId: Int, date: LocalDate): ResponseEntity<DiaryGetRes> {
        val user = getEmptyUserById(userId)

        val diary = diaryRepository.findByUserAndDate(user, date)

        return ResponseEntity.ok(DiaryGetRes(id = diary.id ?: 0, date = diary.date, content = diary.content))
    }

    override fun patch(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode> {

        val user = getEmptyUserById(diaryPostReq.userId)

        val diary = diaryRepository.findByUserAndDate(user, diaryPostReq.date)

        diary.content = diaryPostReq.content

        diaryRepository.save(diary)

        return ResponseEntity.ok().build()
    }

    @Transactional
    override fun delete(userId: Int, date: LocalDate): ResponseEntity<HttpStatusCode> {
        val user = getEmptyUserById(userId)
        println(diaryRepository.deleteByUserAndDate(user, date))

        return ResponseEntity.ok().build()
    }

    private fun getEmptyUserById(id: Int): User{
        return User(id = id, name = null)
    }
}