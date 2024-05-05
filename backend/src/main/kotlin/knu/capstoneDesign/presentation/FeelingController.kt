package knu.capstoneDesign.presentation

import io.swagger.v3.oas.annotations.tags.Tag
import knu.capstoneDesign.application.FeelingService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feeling")
@Tag(name = "감정")
class FeelingController(private val feelingService: FeelingService) {


}