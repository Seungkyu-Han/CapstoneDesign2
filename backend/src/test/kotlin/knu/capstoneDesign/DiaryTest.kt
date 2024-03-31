package knu.capstoneDesign

import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
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

    private final val today: LocalDate = LocalDate.now()
    private final val testContent = "테스트 일기입니다."

    private val testDiary = Diary(user = testUser, date =  today, content = testContent)

    @BeforeEach
    fun addTestUser(){
        userRepository.save(testUser)
        diaryRepository.save(testDiary)
    }

    @AfterEach
    fun deleteTestUSer(){
        diaryRepository.delete(testDiary)
        userRepository.delete(testUser)
    }

    /**
     * @author Seungkyu-Han
     * diary Post Api Test
     */
    @Test
    @Transactional
    fun testPost(){
        //given
        val testDay = today.minusDays(1)
        val testContent = "POST 테스트 일기입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = testContent, date = testDay)

        //then
        diaryService.post(diaryPostReq)

        //when
        val diary = diaryRepository.findByUserAndDate(user = testUser, date = testDay)

        assert(diary.user.id == testUser.id)
        assert(diary.date == testDay)
        assert(diary.content == testContent)

        //after
        diaryRepository.deleteByUserAndDate(testUser, testDay)
    }

    /**
     * @author Seungkyu-Han
     * diary Post Api Conflict Test
     */
    @Test
    fun testPostConflict(){
        //given
        val diaryPostReq = DiaryPostReq(userId = testUser.id, date = today, content = "POST CONFLICT 테스트입니다.")

        //then
        try {
            diaryService.post(diaryPostReq)
            assert(false)
        } catch(_: DataIntegrityViolationException){ }

    }


    /**
     * @author Seungkyu-Han
     * diary Get Api Test
     */
    @Test
    @Transactional
    fun testGet(){
        //given
        val yesterday = today.minusDays(1)
        val getTestContent = "GET 테스트 일기입니다."
        val diary = Diary(user = testUser, date = yesterday, content = getTestContent)
        diaryRepository.save(diary)

        //then
        val firstDiary = diaryService.get(userId = testUser.id, date = yesterday).body
        val secondDiary = diaryService.get(userId = testUser.id, date = today).body

        //when
        assert(firstDiary?.content == getTestContent)
        assert(firstDiary?.date == yesterday)

        assert(secondDiary?.content == testContent)
        assert(secondDiary?.date == today)

        //after
        diaryRepository.deleteByUserAndDate(user = testUser, date = yesterday)

    }

    /**
     * @author Seungkyu-Han
     * diary Patch Api Test
     */
    @Test
    fun testPatch(){
        //given
        val newContent = "PATCH 테스트입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = newContent, date = today)

        //then
        diaryService.patch(diaryPostReq)

        //when
        val newDiary = diaryRepository.findByUserAndDate(user = testUser, date = today)
        assert(newDiary.content == newContent)
        assert(newDiary.date == today)
        assert(newDiary.user.id == testUser.id)

    }

    /**
     * @author Seungkyu-Han
     * diary Delete Api Test
     */
    @Test
    fun testDelete(){
        //given
        val deleteDate = LocalDate.of(1000, 1, 1)
        val diaryToDelete = Diary(user = testUser, date = deleteDate, content = "Delete 테스트 일기입니다.")
        diaryRepository.save(diaryToDelete)
        val originalCount = diaryRepository.count()

        //then
        diaryService.delete(userId = testUser.id, date = deleteDate)

        //when
        val currentCount = diaryRepository.count()
        assert(originalCount - 1 == currentCount)
    }

}