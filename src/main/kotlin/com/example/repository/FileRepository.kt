package com.example.repository

import com.example.model.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {
}
