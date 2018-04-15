package com.jscriptive.rndm.domain

import java.util.*

data class Comment(
        val username: String,
        val timestamp: Date,
        val commentTxt: String,
        val documentId: String,
        val userId: String
)