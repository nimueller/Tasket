package dev.cryptospace.tasket.app

import dev.cryptospace.tasket.payloads.TodoPayload
import io.kvision.state.ObservableListWrapper

object TodoState {

    val todos = ObservableListWrapper<TodoPayload>()

}
