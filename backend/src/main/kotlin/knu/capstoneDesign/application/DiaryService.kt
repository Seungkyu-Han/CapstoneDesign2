package knu.capstoneDesign.application

import knu.capstoneDesign.data.dto.diary.req.DiaryPatchReq
import knu.capstoneDesign.data.dto.diary.req.DiaryPostReq
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.DiaryGetRes
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import java.time.LocalDate

interface DiaryService {
    fun post(diaryPostReq: DiaryPostReq): ResponseEntity<HttpStatusCode>
    fun get(id: Int):ResponseEntity<DiaryGetRes>
    fun patch(diaryPatchReq: DiaryPatchReq): ResponseEntity<HttpStatusCode>
    fun delete(userId: Int, date: LocalDate): ResponseEntity<HttpStatusCode>
    fun getList(userId: Int, startDate: LocalDate, endDate: LocalDate): ResponseEntity<List<DiaryGetListRes>>
    fun getMonth(userId: Int, year: Int, month: Int): ResponseEntity<List<DiaryGetListRes>>
    fun getAll(userId: Int): ResponseEntity<List<DiaryGetListRes>>
}