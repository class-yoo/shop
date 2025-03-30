package com.shoptest.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
    val message: String,
    val status: Int
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(e.message ?: "알 수 없는 오류", HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(e.message ?: "잘못된 요청입니다.", HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElement(e: NoSuchElementException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(e.message ?: "요청한 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
            HttpStatus.NOT_FOUND
        )
    }
}
