package com.example.crud

import com.example.CrudApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [CrudApplication::class])
@ActiveProfiles("test")
class CrudApplicationTests {

	@Test
	fun contextLoads() {
	}

}
