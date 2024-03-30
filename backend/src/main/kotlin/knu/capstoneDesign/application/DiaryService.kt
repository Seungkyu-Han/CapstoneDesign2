package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import java.time.LocalDate

interface DiaryService {
    fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode>
    fun get(userId: Int, date: LocalDate):ResponseEntity<DiaryGetRes>
}