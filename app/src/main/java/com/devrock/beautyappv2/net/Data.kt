package com.devrock.beautyappv2.net

import javax.sql.StatementEvent

data class Status(
    val info: Info
)

data class Info(
    val status: String,
    val trackingId: String
)

data class SessionResponse(
    val info: Info,
    val payload: SessionPayload
)

data class SessionPayload(
    val text: String?,
    val session: String?,
    val registered: Boolean?
)

data class AuthConfirmBody(
    val phone: String,
    val code: String
)

data class AccountBody (
    val name: String,
    val surname: String,
    val gender: String = "Female"
)

data class Geo (
    val city: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class SalonListItem (
    val id: Int,
    val name: String,
    val geo: Geo,
    val hourRentEnd: String?,
    val hourRentStart: String?,
    val rating: Int,
    val thumbnailPhoto: String,
    val daysRentStart: String?,
    val daysType: List<String>?
)

data class SalonListResponse (
    val payload: SalonListPayload,
    val info: Info
)

data class SalonListPayload (
        val list: List<SalonListItem>?,
        val text: String?
)
