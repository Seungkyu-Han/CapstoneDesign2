package knu.capstoneDesign.data.dto.auth.res

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthKakaoInfoRes(
    @JsonProperty("id") val id: Long,
    @JsonProperty("connected_at") val connectedAt: String,
    @JsonProperty("properties") val properties: Properties,
    @JsonProperty("kakao_account") val kakaoAccount: KakaoAccount
)

data class Properties(
    @JsonProperty("nickname") val nickname: String
)

data class KakaoAccount(
    @JsonProperty("profile_nickname_needs_agreement") val profileNicknameNeedsAgreement: Boolean,
    @JsonProperty("profile") val profile: Profile
)

data class Profile(
    @JsonProperty("nickname") val nickname: String,
    @JsonProperty("is_default_nickname") val isDefaultNickname: Boolean
)