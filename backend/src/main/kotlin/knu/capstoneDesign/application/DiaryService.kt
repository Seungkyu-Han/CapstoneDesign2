package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import java.time.LocalDate

interface DiaryService {
    fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode>
    fun get(userId: Int, date: LocalDate):ResponseEntity<DiaryGetRes>
    fun patch(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode>
    fun delete(userId: Int, date: LocalDate): ResponseEntity<HttpStatusCode>
    fun getList(userId: Int, startDate: LocalDate, endDate: LocalDate): ResponseEntity<List<DiaryGetListRes>>
}