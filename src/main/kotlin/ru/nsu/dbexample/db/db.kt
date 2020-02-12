package ru.nsu.dbexample.db

import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource

class Db {

    val dataSource = PGSimpleDataSource().apply {
        serverName = "localhost"
        portNumber = 5432
        databaseName = "postgres"
        user = "postgres"
        password = "mysecretpassword"
    }

    init {

        val flyway = Flyway
            .configure()
            .dataSource(dataSource)
            .load()

        // Start the migration
        flyway.migrate()
    }

}

fun <T> transaction(ds: UnivDataSource, body: () -> T): T {
    ds.realGetConnection().use {
        it.autoCommit = false
        ds.setConnection(it)
        try {
            val res = body()
            it.commit()
            return res
        } catch (e: Exception) {
            it.rollback()
            throw e
        } finally {

            ds.setConnection(null)
        }
    }
}
