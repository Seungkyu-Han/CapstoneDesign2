package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.service.DiaryService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/diary")
@Tag(name = "일기")
class DiaryController(private val diaryService: DiaryService) {

}