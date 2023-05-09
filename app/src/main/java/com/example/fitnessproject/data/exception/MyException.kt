package com.example.fitnessproject.data.exception

data class MyException(
    val error: ErrorException
) : Exception()

enum class ErrorException(val error: String, val statusCode: Int) {
    UNKNOWN_EXCEPTION(error = "", statusCode = 999999),
    SQLITE_ERROR(error = "Error sqlite", statusCode = 1),
    NETWORK_ERROR(error = "Error network", statusCode = 2)
}
