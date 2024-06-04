package knu.capstoneDesign.data.dto.auth.res

data class AuthLoginRes(
    val refreshToken: String,
    val accessToken: String
)