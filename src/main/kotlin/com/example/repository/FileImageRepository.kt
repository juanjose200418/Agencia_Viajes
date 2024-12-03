package com.example.repository

import com.example.model.ImageFile
import org.springframework.data.jpa.repository.JpaRepository

interface FileImageRepository : JpaRepository <ImageFile, Long> {
}
