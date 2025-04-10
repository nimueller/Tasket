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
    private val currentComponents = mutableMapOf<String, Component>()
    private val currentComponentPositions = mutableMapOf<String, Int>()
    private val currentItems = mutableMapOf<String, T>()

    fun update(newItems: List<T>) {
        val removedItemIds = getRemovedItemIds(newItems)
        val updatedItems = getUpdatedItems(newItems)

        removedItemIds.forEach { id ->
            removeItemId(id)
        }

        newItems.forEach { item ->
            addItem(item)
        }

        updatedItems.forEach { id ->
            updateItem(id)
        }
    }

    private fun getRemovedItemIds(newItems: List<T>): Set<String> {
        val newIds = newItems.map { it.metaInformation.id }.toSet()
        val toSet = currentComponents.keys.toSet()
        val currentIds = toSet
        val removedItems = currentIds - newIds
        return removedItems
    }

    private fun getUpdatedItems(newItems: List<T>): Set<T> {
        val result = mutableSetOf<T>()

        for (item in newItems) {
            if (currentItems.containsKey(item.metaInformation.id)) {
                val currentItem = currentItems[item.metaInformation.id]
                if (currentItem != null && currentItem != item) {
                    result.add(item)
                }
            }
        }

        return result
    }

    private fun addItem(item: T) {
        val id = item.metaInformation.id

        if (!currentComponents.containsKey(id)) {
            val component = container.itemRenderer(item)
            container.add(component)
            currentComponentPositions[id] = currentComponentPositions.size
            currentComponents[id] = component
            currentItems[id] = item
        }
    }

    private fun removeItemId(id: String) {
        currentComponents[id]?.let { item ->
            container.remove(item)
            item.dispose()
            currentComponentPositions.remove(id)
            currentComponents.remove(id)
            currentItems.remove(id)
        }
    }

    fun updateItem(item: T) {
        val id = item.metaInformation.id
        val index = currentComponentPositions[id] ?: return
        container.removeAt(index)
        val component = container.itemRenderer(item)
        container.add(index, component)
        currentComponentPositions[id] = index
        currentComponents[id] = component
        currentItems[id] = item
    }

    fun clear() {
        currentComponents.values.forEach { it.dispose() }
        currentComponents.clear()
    }
}
