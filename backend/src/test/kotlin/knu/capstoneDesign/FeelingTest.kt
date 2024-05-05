package knu.capstoneDesign

import knu.capstoneDesign.application.impl.FeelingServiceImpl
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class FeelingTest(
    @Autowired
    @Qualifier("feelingServiceImpl") private val feelingService: FeelingServiceImpl
) {

}