package com.example.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component("_CorsFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter(
    @Value("\${cors.allowed-origins:http://localhost:3000}") private val allowedOrigins: String
) : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val response = res as HttpServletResponse
        val request = req as HttpServletRequest

        val origin = request.getHeader("Origin")
        if (origin != null && allowedOrigins.split(",").any { it.trim() == origin }) {
            response.setHeader("Access-Control-Allow-Origin", origin)
            response.setHeader("Vary", "Origin")
        }
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS")
        response.setHeader(
            "Access-Control-Allow-Headers",
            "X-Requested-With, Authorization, Content-Type, X-Pager-Page, X-Pager-Page-Size"
        )
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Expose-Headers", "X-Total-Count")
        response.setHeader("Access-Control-Allow-Credentials", "true")

        if ("OPTIONS".equals(request.method, ignoreCase = true)) {
            response.status = HttpServletResponse.SC_OK
        } else {
            chain.doFilter(req, res)
        }

    }
}
