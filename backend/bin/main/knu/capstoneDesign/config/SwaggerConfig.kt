package knu.capstoneDesign.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI? {
        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER).name("Authorization")
        val securityRequirement = SecurityRequirement().addList("Bearer")
        return OpenAPI()
            .info(Info().title("STT Diary"))
            .components(Components().addSecuritySchemes("Bearer", securityScheme))
            .security(listOf(securityRequirement))
            .addServersItem(Server().url("/"))
    }
}