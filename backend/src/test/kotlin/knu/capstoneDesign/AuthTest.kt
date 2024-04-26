package knu.capstoneDesign

import knu.capstoneDesign.application.AuthService
import knu.capstoneDesign.config.jwt.JwtTokenProvider
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class AuthTest(
    @Autowired
    private val jwtTokenProvider: JwtTokenProvider,
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val authService: AuthService
) {
    private val testUser = User(id = Long.MAX_VALUE, name = "test", refreshToken = "")

    @BeforeEach
    fun addTestUser(){
        userRepository.save(testUser)
    }

    @AfterEach
    fun deleteTestUSer(){
        userRepository.delete(testUser)
    }

    /**
     * @author Seungkyu-Han
     * auth patch Login Test
     */
    @Test
    fun testPatchLogin(){
        //given
        val refreshToken = jwtTokenProvider.createRefreshToken(testUser.id)
        testUser.refreshToken = refreshToken
        userRepository.save(testUser)

        //then
        val patchLogin = authService.patchLogin("Bearer $refreshToken")

        //when
        val newAccessToken = patchLogin.body?.accessToken
        val newRefreshToken = patchLogin.body?.refreshToken

        assert(refreshToken == newRefreshToken)
        assert(jwtTokenProvider.isAccessToken(newAccessToken ?: ""))
        assert(jwtTokenProvider.getId(newAccessToken ?: "") == testUser.id)

    }

}