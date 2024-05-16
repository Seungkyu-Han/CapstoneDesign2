package knu.capstoneDesign

import knu.capstoneDesign.application.ChatGPTService
import knu.capstoneDesign.data.dto.chatGPT.req.ChatGPTPostConsultingReq
import knu.capstoneDesign.data.entity.Consulting
import knu.capstoneDesign.data.entity.Diary
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.ConsultingRepository
import knu.capstoneDesign.repository.DiaryRepository
import knu.capstoneDesign.repository.UserConsultingRepository
import knu.capstoneDesign.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
    private val consultingRepository: ConsultingRepository,
    @Autowired
    private val userConsultingRepository: UserConsultingRepository
) {

    private val testUser = User(id = Long.MAX_VALUE, name = "test", refreshToken = "")
    private val authentication = UsernamePasswordAuthenticationToken(
        testUser.id, null, listOf(SimpleGrantedAuthority("USER"))
    )
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

    //postConsult
    @Test
    fun testPostConsult(){
        //given
        val diary = Diary(testUser, today, "오늘의 일기", "오늘은 너무 피곤한 하루였다.")
        diaryRepository.save(diary)
        chatGPTService.getDiary(diaryId = diary.id?:0, userId = diary.user.id)
        val question = "어떻게 해야할까?"

        //when
        val chatGPTPostConsultingReq = ChatGPTPostConsultingReq(diary.id ?: 0, question)
        val consultResponse = chatGPTService.postConsult(chatGPTPostConsultingReq, authentication).body ?: throw NullPointerException()

        //then
        val consult = userConsultingRepository.findById(consultResponse.id).orElseThrow{NullPointerException()}
        println(consultResponse.answer)
        assert(consultResponse.answer.isNotEmpty())
        assert(consultResponse.question == question)
        assert(consultResponse.answer == consult.answer)

        //after
        diaryRepository.delete(diary)
    }
    //getConsult
    @Test
    fun testGetConsult(){
        //then
        val diary = Diary(testUser, today, "오늘의 일기 1", "오늘은 살짝 피곤한 하루였다.")
        diaryRepository.save(diary)
        chatGPTService.getDiary(diaryId = diary.id?:0, userId = testUser.id)
        val question = listOf("어떻게 해야 할까?", "답변 고마워")
        question.map {
            q ->
            chatGPTService.postConsult(ChatGPTPostConsultingReq(diary.id ?: 0, q), authentication)
        }

        //when
        val chatGPTGetConsultingResList = chatGPTService.getConsult(diary).body ?: throw NullPointerException()
        //then
        var questionIndex = 0
        chatGPTGetConsultingResList.map {
            consultResponse ->
            val consult = userConsultingRepository.findById(consultResponse.id).orElseThrow { NullPointerException() }
            println(consultResponse.answer)
            assert(consultResponse.answer.isNotEmpty())
            assert(consultResponse.question == question[questionIndex++])
            assert(consultResponse.answer == consult.answer)
        }

        //after
        diaryRepository.delete(diary)
    }

    //deleteConsult
    @Test
    fun deleteConsult(){
        //given
        val diary = Diary(testUser, today, "오늘의 일기", "오늘은 너무 피곤한 하루였다.")
        diaryRepository.save(diary)
        chatGPTService.getDiary(diaryId = diary.id?:0, userId = diary.user.id)
        val question = "어떻게 해야할까?"
        val chatGPTPostConsultingReq = ChatGPTPostConsultingReq(diary.id ?: 0, question)
        val consultResponse = chatGPTService.postConsult(chatGPTPostConsultingReq, authentication).body ?: throw NullPointerException()

        //when
        chatGPTService.deleteConsult(userConsultingId = consultResponse.id, authentication)

        //then
        val chatGPTGetConsultingResList = chatGPTService.getConsult(diary).body ?: throw NullPointerException()
        assert(chatGPTGetConsultingResList.isEmpty())

        //after
        diaryRepository.delete(diary)
    }
}