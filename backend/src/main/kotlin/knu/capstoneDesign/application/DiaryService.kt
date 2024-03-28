package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.diary.DiaryPostReq
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

interface DiaryService {
    fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode>


}