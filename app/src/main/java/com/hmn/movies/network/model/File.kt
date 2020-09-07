package com.hmn.movies.network.model

import android.net.Uri

data class File(

    var uri: Uri,
    var name:String,
    var size:String,
    var deletePath:String,
    var time:String



)
