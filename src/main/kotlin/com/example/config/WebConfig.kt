package com.example.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${file.storage.path:./storage}") private val storagePath: String
) : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val location = "file:" + storagePath.trimEnd('/') + "/"
        registry.addResourceHandler("/storage/**")
            .addResourceLocations(location, "classpath:/static/")
    }
}