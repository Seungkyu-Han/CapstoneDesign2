package knu.capstoneDesign

import knu.capstoneDesign.application.impl.AnalysisServiceImpl
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class AnalysisTest(
    @Autowired
    @Qualifier("analysisServiceImpl") private val analysisService: AnalysisServiceImpl
) {

}