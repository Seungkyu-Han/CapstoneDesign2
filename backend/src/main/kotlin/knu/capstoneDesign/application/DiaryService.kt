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
    fun delete(id: Int): ResponseEntity<HttpStatusCode>
    fun getList(userId: Long, startDate: LocalDate, endDate: LocalDate): ResponseEntity<List<DiaryGetListRes>>
    fun getMonth(userId: Long, year: Int, month: Int): ResponseEntity<List<DiaryGetListRes>>
    fun getAll(userId: Long): ResponseEntity<List<DiaryGetListRes>>
}