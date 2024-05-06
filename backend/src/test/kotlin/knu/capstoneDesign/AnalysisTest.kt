package knu.capstoneDesign

import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.AnalysisRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
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
    @Qualifier("analysisServiceImpl") private val analysisService: AnalysisServiceImpl,
    @Autowired
    private val diaryRepository: DiaryRepository,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val analysisRepository: AnalysisRepository
) {

    private val testUser = User(id = Long.MAX_VALUE, name = "test", refreshToken = "")
    private final val today = LocalDate.now()

    @BeforeEach
    fun addTestUser(){
        userRepository.save(testUser)
    }

    @AfterEach
    fun deleteTestUser(){
        userRepository.delete(testUser)
    }

    /**
     * @author Seungkyu-Han
     * get positive analysis Test
     */
    @Test
    @Transactional
    fun testGetPositiveAnalysis(){
        //given
        val positiveDiary = Diary(testUser, today, "긍정일기입니다.", "오늘은 기분이 좋았다.")
        diaryRepository.save(positiveDiary)
        val diary = diaryRepository.findByUserAndDate(testUser, today).orElseThrow()

        //when
        val analysisResult = analysisService.get(diary, testUser.id)

        //then
        assert(analysisResult.body == "positive")

        //after
        analysisRepository.deleteByDiary(diary)
        diaryRepository.delete(diary)
    }

    /**
     * @author Seungkyu-Han
     * get negative analysis Test
     */
    @Test
    @Transactional
    fun testGetNegativeAnalysis(){
        //given
        val negativeDiary = Diary(testUser, today, "부정일기입니다.", "오늘은 기분이 안좋았다.")
        diaryRepository.save(negativeDiary)
        val diary = diaryRepository.findByUserAndDate(testUser, today).orElseThrow()

        //when
        val analysisResult = analysisService.get(diary, testUser.id)

        //then
        assert(analysisResult.body == "negative")

        //after
        analysisRepository.deleteByDiary(diary)
        diaryRepository.delete(diary)
    }

}