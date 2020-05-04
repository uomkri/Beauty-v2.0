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
    val session: String,
    val registered: Boolean
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