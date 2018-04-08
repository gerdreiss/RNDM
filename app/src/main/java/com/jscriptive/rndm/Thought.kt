package com.jscriptive.rndm

import java.util.*

data class Thought(
        val username: String,
        val timestamp: Date,
        val thoughtTxt: String,
        val numLikes: Int,
        val numComments: Int,
        val documentId: String
)