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
import org.springframework.dao.DataIntegrityViolationException
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
    private final val originalContent = "원래 내용입니다."
    private final val today: LocalDate = LocalDate.now()

    val diary2DaysAgo = Diary(user = testUser, date = today.minusDays(2), content = diary2DaysAgoContent)
    val diary1DaysAgo = Diary(user = testUser, date = today.minusDays(1), content = diary1DaysAgoContent)
    val originalDiary = Diary(user = testUser, date = today.plusDays(1), content = originalContent)

    @BeforeAll
    fun addTestUser(){
        userRepository.save(testUser)
        diaryRepository.save(originalDiary)
    }

    @AfterAll
    fun deleteTestUSer(){
        diaryRepository.deleteAll(listOf(diary2DaysAgo, diary1DaysAgo))
        diaryRepository.delete(originalDiary)
        userRepository.delete(testUser)
    }

    /**
     * @author Seungkyu-Han
     * diary Post Api Test
     */
    @Test
    fun testPost(){
        //given
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = "오늘의 일기입니다.", date = LocalDate.now())

        //then
        diaryService.post(diaryPostReq)

        //when
        val diary = diaryRepository.findTopByOrderByIdDesc()

        assert(diary.content == diaryPostReq.content)
        assert(diary.user.id == diaryPostReq.userId)


        //after
        diaryRepository.delete(diaryRepository.findTopByOrderByIdDesc())
    }

    /**
     * @author Seungkyu-Han
     * diary Post Api Conflict Test
     */
    @Test
    fun testPostConflict(){
        //given
        val diary = Diary(user = testUser, date = today.plusDays(2), content = "테스트 입니다.")
        diaryRepository.save(diary)

        //then
        val diaryPostReq = DiaryPostReq(userId = testUser.id, date = today.plusDays(2), content = "테스트 입니다.")
        try{
            diaryService.post(diaryPostReq)
            throw RuntimeException()
        }catch(_: DataIntegrityViolationException){ }
        finally {
            diaryRepository.delete(diary)
        }

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

    /**
     * @author seungkyu-Han
     * diary Patch Api Test
     */
    @Test
    fun testPatch(){
        //given
        val newContent = "바뀐 내용입니다."
        val diaryPatchReq = DiaryPostReq(userId = testUser.id, content = newContent, date = originalDiary.date)

        //then
        diaryService.patch(diaryPatchReq)

        //when
        val diary = diaryRepository.findByUserAndDate(testUser, today.plusDays(1))

        assert(diary.content == newContent)
        assert(diary.date == today.plusDays(1))
        assert(diary.user == testUser)
        assert(diary.id == originalDiary.id)
    }
}