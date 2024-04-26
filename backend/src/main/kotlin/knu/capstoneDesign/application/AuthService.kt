package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.auth.res.AuthLoginRes
import org.springframework.http.ResponseEntity

interface AuthService {

    fun getLogin(code: String): ResponseEntity<AuthLoginRes>
    fun patchLogin(refreshToken: String): ResponseEntity<AuthLoginRes>
}