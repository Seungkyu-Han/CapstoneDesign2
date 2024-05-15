package knu.capstoneDesign

import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.entity.Consulting
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.ConsultingRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ExtendWith(SpringExtension::class)
class ChatGPTTest(
    @Autowired
    private val diaryRepository: DiaryRepository,
    @Autowired
    private val chatGPTService: ChatGPTService,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val consultingRepository: ConsultingRepository
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
     * chatGPT consulting Test
     */
    @Test
    @Transactional
    fun testChatGPTConsult(){
        //given
        val diary = Diary(testUser, today, "제목", "오늘은 배가 살짝쿵 아팠다.")
        diaryRepository.save(diary)

        //when
        val consult = chatGPTService.getDiary(diary.id ?: 0, testUser.id).body?.content ?: throw NullPointerException()

        //then
        assert(consult.isNotEmpty())
        val consulting:Consulting = consultingRepository.findByDiary(diary).orElseThrow { NullPointerException() }
        assert(consulting.content == consult)

        //after
        diaryRepository.delete(diary)
    }
}