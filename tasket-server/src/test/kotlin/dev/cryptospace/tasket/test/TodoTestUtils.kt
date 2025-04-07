package dev.cryptospace.tasket.test

import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.transactions.transaction

fun insertTodo(owner: TestUser, label: String = "Test"): String {
    return transaction {
        exec(
            stmt = """
                INSERT INTO tasket.todos (label,  owner_id)
                VALUES ('$label', '${owner.id.value}')
                RETURNING id
            """.trimIndent(),
            // the INSERT statement returns the id of the inserted row, so hint a return value to Exposed, otherwise
            // it will implicitly only assume an INSERT without a return value
            explicitStatementType = StatementType.SELECT,
            transform = { resultSet ->
                assert(resultSet.next())
                resultSet.getString("id")
            },
        ) ?: error("Inserting todo failed")
    }
}
