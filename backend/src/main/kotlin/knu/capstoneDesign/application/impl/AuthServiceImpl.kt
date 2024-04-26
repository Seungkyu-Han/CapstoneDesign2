package knu.capstoneDesign.application.impl

import knu.capstoneDesign.application.AuthService
import knu.capstoneDesign.config.jwt.JwtTokenProvider
import knu.capstoneDesign.data.dto.auth.res.AuthKakaoAccessRes
import knu.capstoneDesign.data.dto.auth.res.AuthKakaoInfoRes
import knu.capstoneDesign.data.dto.auth.res.AuthLoginRes
import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import java.lang.NullPointerException
import java.net.URI

@Service
class AuthServiceImpl(
    @Value("\${kakao.auth.client_id}")
    private val clientId: String,
    @Value("\${kakao.auth.redirect_uri}")
    private val redirectUri: String,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
): AuthService {

    private val accessTokenReqUrl = "https://kauth.kakao.com/oauth/token"
    private val jsonReqUrl = "https://kapi.kakao.com/v2/user/me"

    override fun getLogin(code: String): ResponseEntity<AuthLoginRes> {

        val kakaoAccessToken = getKakaoAccessToken(code) ?: throw NullPointerException()

        val authKakaoInfoRes = getIdByKakao(kakaoAccessToken) ?: throw NullPointerException()

        val user = userRepository.findById(authKakaoInfoRes.id)

        if (user.isEmpty)
            return register(authKakaoInfoRes)

        val accessToken = jwtTokenProvider.createAccessToken(authKakaoInfoRes.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(authKakaoInfoRes.id)

        user.get().refreshToken = refreshToken
        userRepository.save(user.get())

        return ResponseEntity.ok(AuthLoginRes(refreshToken = refreshToken, accessToken = accessToken))
    }

    private fun register(authKakaoInfoRes: AuthKakaoInfoRes): ResponseEntity<AuthLoginRes>{

        val accessToken = jwtTokenProvider.createAccessToken(authKakaoInfoRes.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(authKakaoInfoRes.id)

        val user = User(id = authKakaoInfoRes.id, name = authKakaoInfoRes.properties.nickname, refreshToken = refreshToken)
        userRepository.save(user)

        return ResponseEntity.ok(AuthLoginRes(refreshToken = refreshToken, accessToken = accessToken))
    }

    private fun getIdByKakao(token: String): AuthKakaoInfoRes? {

        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
        httpHeaders.add("Authorization", "Bearer $token")

        return restTemplate.exchange(
            RequestEntity<Any>(httpHeaders, HttpMethod.POST, URI.create(jsonReqUrl)),
            AuthKakaoInfoRes::class.java
        ).body
    }



    private fun getKakaoAccessToken(code: String):String?{

        val restTemplate = RestTemplate()

        val httpHeaders = HttpHeaders()

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")

        val httpBody = LinkedMultiValueMap<String, String>()

        httpBody.add("grant_type","authorization_code")
        httpBody.add("client_id", clientId)
        httpBody.add("redirect_uri", redirectUri)
        httpBody.add("code",code)

        return restTemplate.postForObject(accessTokenReqUrl, HttpEntity(httpBody, httpHeaders), AuthKakaoAccessRes::class.java)?.access_token
    }
}