package app.necros.model

import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    val id: Long,
    val name: String
)
