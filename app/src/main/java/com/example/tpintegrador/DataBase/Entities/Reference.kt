package com.example.tpintegrador.DataBase.Entities

import java.util.Date

data class Reference (
    val id: Long,
    val description: String,
    val openingDate: Date,
    val closingDate: Date,
    val close: Boolean
) {
    constructor(
        description: String,
        openingDate: Date,
        closingDate: Date,
        close: Boolean
    ) : this(-1,description,openingDate,closingDate,close)
}