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
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Year

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
    private final val testTitle = "테스트 일기입니다."

    private val testDiary = Diary(user = testUser, date =  today, title = testTitle, content = testContent)

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
        val testTitle = "테스트입니다."
        val testContent = "POST 테스트 일기입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = testContent, title = testTitle, date = testDay)

        //then
        diaryService.post(diaryPostReq)

        //when
        val diary = diaryRepository.findByUserAndDate(user = testUser, date = testDay)

        assert(diary.user.id == testUser.id)
        assert(diary.date == testDay)
        assert(diary.title == testTitle)
        assert(diary.content == testContent)

        //after
        diaryRepository.deleteByUserAndDate(testUser, testDay)
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
        val getTestTitle = "테스트입니다."
        val diary = Diary(user = testUser, date = yesterday, title = getTestTitle, content = getTestContent)
        diaryRepository.save(diary)

        //then
        val firstDiary = diaryService.get(userId = testUser.id, date = yesterday).body
        val secondDiary = diaryService.get(userId = testUser.id, date = today).body

        //when
        assert(firstDiary?.content == getTestContent)
        assert(firstDiary?.date == yesterday)
        assert(firstDiary?.title == getTestTitle)

        assert(secondDiary?.content == testContent)
        assert(secondDiary?.title == testTitle)
        assert(secondDiary?.date == today)

        //after
        diaryRepository.deleteByUserAndDate(user = testUser, date = yesterday)

    }

    /**
     * @author Seungkyu-Han
     * diary Patch Content Api Test
     */
    @Test
    fun testPatchContent(){
        //given
        val newContent = "PATCH 테스트입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = newContent, title = null, date = today)

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
     * diary Patch title Api Test
     */
    @Test
    fun testPatchTitle(){
        //given
        val newTitle = "PATCH 테스트입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = null, title = newTitle, date = today)

        //then
        diaryService.patch(diaryPostReq)

        //when
        val newDiary = diaryRepository.findByUserAndDate(user = testUser, date = today)
        assert(newDiary.title == newTitle)
        assert(newDiary.date == today)
        assert(newDiary.user.id == testUser.id)

    }

    /**
     * @author Seungkyu-Han
     * diary Patch Content All Test
     */
    @Test
    fun testPatchAll(){
        //given
        val newContent = "PATCH 테스트입니다."
        val newTitle = "PATCH 테스트입니다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = newContent, title = newTitle, date = today)

        //then
        diaryService.patch(diaryPostReq)

        //when
        val newDiary = diaryRepository.findByUserAndDate(user = testUser, date = today)
        assert(newDiary.content == newContent)
        assert(newDiary.title == newTitle)
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
        val diaryToDelete = Diary(user = testUser, date = deleteDate, title = "테스트입니다.", content = "Delete 테스트 일기입니다.")
        diaryRepository.save(diaryToDelete)
        val originalCount = diaryRepository.count()

        //then
        diaryService.delete(userId = testUser.id, date = deleteDate)

        //when
        val currentCount = diaryRepository.count()
        assert(originalCount - 1 == currentCount)
    }

    /**
     * @author Seungkyu-Han
     * diary Delete Not Api Test
     */

    @Test
    fun testDeleteNot(){
        //given
        val notDeleteDate = LocalDate.of(1000, 1, 2)
        val originalCount = diaryRepository.count()

        //then
        diaryService.delete(userId = testUser.id, date = notDeleteDate)

        //when
        val currentCount = diaryRepository.count()
        assert(originalCount == currentCount)
    }

    /**
     * @author Seungkyu-Han
     * diary Get List Api Test
     */
    @Test
    fun testGetList(){
        //given
        val count = 10
        val titleList = mutableListOf<String>()
        val contentList = mutableListOf<String>()

        for (i in 1..count) {
            titleList.add("GET LIST 테스트$i 제목입니다.")
            contentList.add("GET LIST 테스트$i 일기입니다.")
        }

        val diaryList = mutableListOf<Diary>()

        for (i in 1..count)
            diaryList.add(Diary(testUser, LocalDate.of(1000, 1, i), titleList[i - 1],contentList[i - 1]))

        diaryRepository.saveAll(diaryList)

        //then
        val result = diaryService.getList(testUser.id,
            LocalDate.of(1000, 1, 1),
            LocalDate.of(1000, 12, 30)).body

        //when
        assert(result?.size == count)
        for(i in 1..count){
            assert(result?.get(i - 1)?.title == titleList[i - 1])
            assert(result?.get(i - 1)?.content == contentList[i - 1])
        }


        //after
        diaryRepository.deleteAll(diaryList)
    }

    /**
     * @author Seungkyu-Han
     * diary Get Month Api Test
     */
    @Test
    fun testGetMonth(){

        for(i in 1..12){
            //given
            val year = Year.now()
            val startTitle = "GET MONTH 테스트 1입니다."
            val startContent = "GET MONTH 테스트 1 본문입니다."
            val endTitle = "GET MONTH 테스트 2입니다."
            val endContent = "GET MONTH 테스트 2 본문입니다."
            val startDiary = Diary(testUser, year.atMonth(i).atDay(1), title = startTitle, content = startContent)
            val endDiary = Diary(testUser, year.atMonth(i).atEndOfMonth(), title = endTitle, content = endContent)

            diaryRepository.saveAll(listOf(startDiary, endDiary))

            //then
            val result = diaryService.getMonth(testUser.id, year.value , i).body?.sortedBy { it.id }

            var index = 0

            //when
            if(i == LocalDate.now().monthValue){
                assert(result?.get(index)?.title == testTitle)
                assert(result?.get(index)?.content == testContent)
                index++
            }

            assert(result?.get(index)?.title == startTitle)
            assert(result?.get(index)?.content == startContent)

            index++

            assert(result?.get(index)?.title == endTitle)
            assert(result?.get(index)?.content == endContent)

            //after
            diaryRepository.deleteAll(listOf(startDiary, endDiary))

        }

    }

}