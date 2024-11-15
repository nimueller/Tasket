package dev.cryptospace.tasket.server.table

object TodoCommentsTable : BaseTable("todo_comments") {
    val comment = text("comment")
}
