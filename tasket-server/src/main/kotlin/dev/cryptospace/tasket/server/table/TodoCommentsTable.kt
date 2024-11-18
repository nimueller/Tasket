package dev.cryptospace.tasket.server.table

object TodoCommentsTable : BaseTable("tasket.todo_comments") {
    val todoId = reference("todo_id", TodosTable.id)
    val comment = text("comment")
}
