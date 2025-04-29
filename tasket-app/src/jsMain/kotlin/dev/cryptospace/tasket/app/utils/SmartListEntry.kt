package dev.cryptospace.tasket.app.utils

import io.kvision.core.Component

data class SmartListEntry<T>(val id: String, val component: Component, val item: T, var index: Int) {

    fun needsUpdate(newItem: T): Boolean = item != newItem
}
