package knu.capstoneDesign

import knu.capstoneDesign.application.DiaryService
import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.data.enum.Emotion
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
class AnalysisTest(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val diaryRepository: DiaryRepository,
    @Autowired
    private val analysisRepository: AnalysisRepository,
    @Autowired
    private val diaryService: DiaryService,
    @Autowired
    @Qualifier("analysisServiceImpl") private val analysisService: AnalysisServiceImpl
){

    private val testUser = User(id = Long.MAX_VALUE, name = "test", refreshToken = "")
    private final val today: LocalDate = LocalDate.now()

    @BeforeEach
    fun addTestUser(){
        userRepository.save(testUser)
    }

    @AfterEach
    fun deleteTestUSer(){
        userRepository.delete(testUser)
    }

    @Test
    @Transactional
    fun testAnalysisPositive(){
        //given
        val testTitle = "테스트입니다."
        val testContent = "오늘은 기분이 좋았다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = testContent, title = testTitle, date = today)
        val diaryId = diaryService.post(diaryPostReq).body ?: 0
        val diary = Diary(id = diaryId, user = testUser, date = today, title = null, content = null)

        GlobalScope.launch {
            delay(5000)
            //when
            val analysisResult = analysisService.get(diary).body

            //then
            assert(analysisResult == Emotion.positive.name)

            //after
            withContext(Dispatchers.IO) {
                analysisRepository.deleteByDiary(diary)
            }
            withContext(Dispatchers.IO) {
                diaryRepository.deleteById(diaryId)
            }
        }
    }

    @Test
    @Transactional
    fun testAnalysisNegative(){
        //given
        val testTitle = "테스트입니다."
        val testContent = "오늘은 기분이 안좋았다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = testContent, title = testTitle, date = today)
        val diaryId = diaryService.post(diaryPostReq).body ?: 0
        val diary = Diary(id = diaryId, user = testUser, date = today, title = null, content = null)

        GlobalScope.launch {
            delay(5000)
            //when
            val analysisResult = analysisService.get(diary).body

            //then
            assert(analysisResult == Emotion.negative.name)

            //after
            withContext(Dispatchers.IO) {
                analysisRepository.deleteByDiary(diary)
            }
            withContext(Dispatchers.IO) {
                diaryRepository.deleteById(diaryId)
            }
        }
    }

    @Test
    @Transactional
    fun testAnalysisNeutral(){
        //given
        val testTitle = "테스트입니다."
        val testContent = "오늘이었다."
        val diaryPostReq = DiaryPostReq(userId = testUser.id, content = testContent, title = testTitle, date = today)
        val diaryId = diaryService.post(diaryPostReq).body ?: 0
        val diary = Diary(id = diaryId, user = testUser, date = today, title = null, content = null)

        GlobalScope.launch {
            delay(5000)
            //when
            val analysisResult = analysisService.get(diary).body

            //then
            assert(analysisResult == Emotion.neutral.name)

            //after
            withContext(Dispatchers.IO) {
                analysisRepository.deleteByDiary(diary)
            }
            withContext(Dispatchers.IO) {
                diaryRepository.deleteById(diaryId)
            }
        }
    }
}