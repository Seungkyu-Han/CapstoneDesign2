package knu.capstoneDesign.config.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtExceptionFilter: OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try{
            filterChain.doFilter(request, response)
        }
        catch (malformedJwtException: MalformedJwtException){
            setResponseStatus(response, HttpStatus.FORBIDDEN.value(), "해당 토큰으로 인증에 실패했습니다.")
        }
        catch (expiredJwtException: ExpiredJwtException){
            setResponseStatus(response, HttpStatus.FORBIDDEN.value(), "만료된 토큰입니다.")
        }
    }

    private fun setResponseStatus(httpServletResponse: HttpServletResponse, status:Int, message: String){
        httpServletResponse.status = status
        httpServletResponse.contentType = "application/text"
        httpServletResponse.characterEncoding = "UTF-8"
        httpServletResponse.writer.write(message)
    }
}