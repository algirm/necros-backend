package app.necros.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Wallet(
    val id: Int,
    val name: String
)

object Wallets : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(id)
}