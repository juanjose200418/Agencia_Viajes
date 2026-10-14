package com.example.model

data class RecommendationSearchResponse(
    val destinations: List<Destination>,
    val explanation: String
)