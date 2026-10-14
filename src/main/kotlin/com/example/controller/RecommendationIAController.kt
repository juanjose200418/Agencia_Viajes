package com.example.controller

import com.example.model.RecommendationSearchResponse
import com.example.service.RecommendationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RecommendationController(
    @Autowired
    private val recommendationService: RecommendationService
) {

    @GetMapping("/recommendations")
    fun getRecommendations(query: String): String {
        return recommendationService.getDestinationRecommendations(query)
    }

    @GetMapping("/recommendations/search")
    fun searchDestinations(query: String): RecommendationSearchResponse {
        return recommendationService.searchDestinations(query)
    }
}