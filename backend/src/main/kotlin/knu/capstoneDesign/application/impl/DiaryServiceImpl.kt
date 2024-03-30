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
import java.time.LocalDate

@Service
class DiaryServiceImpl(
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository):DiaryService {

    override fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode> {

//        val user = userRepository.findById(diaryPostReq.userId)
//            .orElseThrow{throw NullPointerException()}

        val user = User(id = diaryPostReq.userId, name = null)

        val diary = Diary(
            id = null,
            user = user,
            date = LocalDate.now(),
            content = diaryPostReq.content
        )

        diaryRepository.save(diary)


        return ResponseEntity.ok().build()
    }

    override fun get(userId: Int, date: LocalDate): ResponseEntity<DiaryGetRes> {
        val user = User(id = userId)

        val diary = diaryRepository.findByUserAndDate(user, date)

        return ResponseEntity.ok(DiaryGetRes(id = diary.id ?: 0, date = diary.date, content = diary.content))
    }
}