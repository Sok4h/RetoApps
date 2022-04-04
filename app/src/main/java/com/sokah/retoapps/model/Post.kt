package com.sokah.retoapps.model

import java.util.*

data class Post(

    var userId :String,
    var postMessage :String,
    var city :String,
    var date: Calendar,
    var img : String
)
