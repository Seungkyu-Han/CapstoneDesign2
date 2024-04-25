package knu.capstoneDesign.config.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}")
    val secretKey: String
) {

    private final val accessTokenValidTime = Duration.ofHours(2).toMillis()
    private final val refreshTokenValidTime = Duration.ofDays(7).toMillis()

    fun getId(token: String): Int?{
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body.get("id", java.lang.Integer::class.java).toInt()
    }

    fun isAccessToken(token: String): Boolean{
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .header["type"].toString() == "access"
    }

    fun createAccessToken(id: Int): String{
        return createJwtToken(id,  "access", accessTokenValidTime)
    }

    fun createRefreshToken(id: Int): String{
        return createJwtToken(id,  "refresh", refreshTokenValidTime)
    }

    fun createJwtToken(id: Int, type: String, tokenValidTime: Long): String{
        val claims = Jwts.claims()
        claims["d"] = id

        return Jwts.builder()
            .setHeaderParam("type", type)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

}