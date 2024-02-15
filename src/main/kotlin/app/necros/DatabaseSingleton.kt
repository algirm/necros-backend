package app.necros

import app.necros.model.Wallets
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val dbDriver = config.property("database.driver").getString()
        val dbUrl = config.property("database.url").getString()
        val dbUser = config.property("database.user").getString()
        val dbPassword = config.property("database.password").getString()
        
        val database =  Database.connect(
            url = dbUrl,
            driver = dbDriver,
            user = dbUser,
            password = dbPassword
        )
        transaction(database) {
            SchemaUtils.create(Wallets)
        }
    }
    
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}