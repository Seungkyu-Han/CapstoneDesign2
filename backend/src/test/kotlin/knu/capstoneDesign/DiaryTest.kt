package knu.capstoneDesign

import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension::class)
class DiaryTest(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val diaryRepository: DiaryRepository,
    @Autowired
    private val diaryService: DiaryService
){

    private val testUser = User(id = Int.MAX_VALUE, name = "test")

    @BeforeAll
    fun addTestUser(){
        userRepository.save(testUser)
    }

    @AfterAll
    fun deleteTestUSer(){
        diaryRepository.delete(diaryRepository.findTopByOrderByIdDesc())
        userRepository.delete(testUser)

    }

    @Test
    fun testPost(){
        //given
        val diaryPostReq = DiaryPostReq(userId = testUser.id, "테스트용 일기입니다.")

        //then
        diaryService.post(diaryPostReq)

        //when
        val diary = diaryRepository.findTopByOrderByIdDesc()

        assert(diary.content == diaryPostReq.content)
        assert(diary.user.id == diaryPostReq.userId)

    }
}