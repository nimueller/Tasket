package dev.cryptospace.tasket.app.utils

import dev.cryptospace.tasket.payloads.ResponsePayload
import io.kvision.core.Component
import io.kvision.core.Container

/**
 * A helper class for rendering a list of items in a smart way. The usual clear + refresh may cause flickering and a
 * general bad user experience. This class will only add or remove items that are actually new or removed. This
 * allows a way smoother user experience, as only the necessary changes are made and the overall layout is not
 * changed.
 */
class SmartListRenderer<T : ResponsePayload>(
    private val container: Container,
    private val itemRenderer: Container.(T) -> Component,
) {
    private val items = mutableMapOf<String, Component>()

    fun update(newItems: List<T>) {
        val newIds = newItems.map { it.metaInformation.id }.toSet()
        val currentIds = items.keys.toSet()

        (currentIds - newIds).forEach { id ->
            items[id]?.let { item ->
                container.remove(item)
                item.dispose()
                items.remove(id)
            }
        }

        newItems.forEach { item ->
            val id = item.metaInformation.id

            if (!items.containsKey(id)) {
                val component = container.itemRenderer(item)
                container.add(component)
                items[id] = component
            }
        }
    }

    fun clear() {
        items.values.forEach { it.dispose() }
        items.clear()
    }
}
