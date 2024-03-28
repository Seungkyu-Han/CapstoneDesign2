package knu.capstoneDesign.application.impl

import knu.capstoneDesign.data.dto.diary.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import knu.capstoneDesign.application.DiaryService
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
}