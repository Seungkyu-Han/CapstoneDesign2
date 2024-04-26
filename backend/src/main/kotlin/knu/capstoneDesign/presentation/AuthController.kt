package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.application.AuthService
import knu.capstoneDesign.data.dto.auth.res.AuthLoginRes
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "인증")
class AuthController(private val authService: AuthService) {

    @GetMapping("/login")
    @Operation(summary = "로그인 API", description = "회원이 아니면 회원가입을 진행합니다.")
    @Parameters(
        Parameter(name = "code", description = "카카오 로그인 code")
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = AuthLoginRes::class))))
    )
    fun getLogin(@RequestParam code: String): ResponseEntity<AuthLoginRes> {
        return authService.getLogin(code)
    }

    @PatchMapping("/login")
    @Operation(summary = "AccessToken 갱신 API", description = "RefreshToken 사용해서 AccessToken 갱신")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "성공", content = arrayOf(Content(schema = Schema(implementation = AuthLoginRes::class))))
    )
    fun patchLogin(@Parameter(hidden = true) @RequestHeader("Authorization") refreshToken: String):ResponseEntity<AuthLoginRes>{
        return authService.patchLogin(refreshToken)
    }
}