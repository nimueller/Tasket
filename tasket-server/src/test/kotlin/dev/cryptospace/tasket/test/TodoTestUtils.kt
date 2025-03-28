package dev.cryptospace.tasket.test

import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction

fun insertTodo(label: String = "Test", owner: TestUser): String {
    return transaction {
        exec(
            stmt = """
                INSERT INTO tasket.todos (label,  owner_id)
                VALUES ('Test', '${owner.id.value}')
                RETURNING id
            """.trimIndent(),
            // the INSERT statement returns the id of the inserted row, so hint a return value to Exposed, otherwise
            // it will implicitly only assume an INSERT without a return value
            explicitStatementType = StatementType.SELECT,
            transform = { resultSet ->
                assert(resultSet.next())
                resultSet.getString("id")
            }
        ) ?: throw IllegalStateException("Inserting todo failed")
    }
}
