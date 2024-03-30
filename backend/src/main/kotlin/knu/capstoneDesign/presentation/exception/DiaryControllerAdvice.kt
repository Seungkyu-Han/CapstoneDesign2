package knu.capstoneDesign.presentation.exception

import knu.capstoneDesign.presentation.DiaryController
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice(basePackageClasses = [DiaryController::class])
class DiaryControllerAdvice {

    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun sqlIntegrityConstraintViolationExceptionHandler():ResponseEntity<HttpStatusCode>{
        return ResponseEntity.status(HttpStatus.CONFLICT.ordinal).build()
    }
}