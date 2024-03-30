package knu.capstoneDesign

import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
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
import java.lang.RuntimeException
import java.time.LocalDate

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
    private final val diary2DaysAgoContent = "2일전 일기"
    private final val diary1DaysAgoContent = "1일전 일기"
    private final val today: LocalDate = LocalDate.now()

    val diary2DaysAgo = Diary(user = testUser, date = today.minusDays(2), content = diary2DaysAgoContent)
    val diary1DaysAgo = Diary(user = testUser, date = today.minusDays(1), content = diary1DaysAgoContent)

    @BeforeAll
    fun addTestUser(){
        userRepository.save(testUser)
    }

    @AfterAll
    fun deleteTestUSer(){
        diaryRepository.delete(diaryRepository.findTopByOrderByIdDesc())
        diaryRepository.deleteAll(listOf(diary2DaysAgo, diary1DaysAgo))
        userRepository.delete(testUser)
    }

    /**
     * @author Seungkyu-Han
     * diary Post Api Test
     */
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

    /**
     * @author Seungkyu-Han
     * diary Get Api Test
     */
    @Test
    fun testGet(){
        //given
        diaryRepository.saveAll(listOf(diary2DaysAgo, diary1DaysAgo))

        //then
        val diary2DaysAgoResult =
            diaryService.get(userId = testUser.id, today.minusDays(2)).body ?: throw RuntimeException()
        val diary1DaysAgoResult =
            diaryService.get(userId = testUser.id, today.minusDays(1)).body ?: throw RuntimeException()

        //when
        assert(today.minusDays(2) == diary2DaysAgoResult.date)
        assert(diary2DaysAgoContent == diary2DaysAgoResult.content)

        assert(today.minusDays(1) == diary1DaysAgoResult.date)
        assert(diary2DaysAgoContent == diary2DaysAgoResult.content)
    }
}