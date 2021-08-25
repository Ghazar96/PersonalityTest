package com.example.personalitytest.mapper

interface Mapper<SOURCE, RESULT> {
    fun map(s: SOURCE): RESULT

    fun map(s: List<SOURCE>): List<RESULT> {
        return s.map { map(it) }
    }
}
