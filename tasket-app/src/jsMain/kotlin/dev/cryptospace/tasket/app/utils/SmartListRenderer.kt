package dev.cryptospace.tasket.app.utils

import dev.cryptospace.tasket.payloads.ResponsePayload
import external.Sortable
import external.jsonObject
import io.kvision.core.Component
import io.kvision.core.Container
import org.w3c.dom.HTMLElement

private const val ID_PROPERTY = "data-id"
private const val ANIMATION_DURATION_IN_MILLIS = 200

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
    val entries = mutableListOf<SmartListEntry<T>>()

    fun makeSortable(onDraggingEnded: (List<SmartListEntry<T>>) -> Unit): SmartListRenderer<T> {
        container.addAfterInsertHook { node ->
            Sortable.create(
                element = node.elm!!.unsafeCast<HTMLElement>(),
                options = jsonObject {
                    animation = ANIMATION_DURATION_IN_MILLIS
                    onEnd = { event ->
                        val oldIndex = event.oldIndex
                        val newIndex = event.newIndex

                        if (oldIndex != newIndex) {
                            val item = entries[oldIndex]
                            val lowerIndex = minOf(oldIndex, newIndex)
                            val upperIndex = maxOf(oldIndex, newIndex)
                            val onOrderingChanged = getItemsBetween(lowerIndex, upperIndex)

                            onOrderingChanged.forEachIndexed { index, entry ->
                                val newIndex = if (oldIndex < newIndex) {
                                    lowerIndex + index - 1
                                } else {
                                    lowerIndex + index + 1
                                }
                                entry.index = newIndex
                            }

                            item.index = newIndex

                            onDraggingEnded(onOrderingChanged)
                        }
                    }
                },
            )
        }
        return this
    }

    /**
     * Sets the [items] in the list. This will remove all items that are not in the new list and add all items that are
     * not in the current list. This will also update the items that are already in the list, i.e., it will rerender
     * them.
     */
    fun setItems(items: List<T>) {
        val currentEntries = entries
        var processedItemCount = 0

        items.forEachIndexed { index, item ->
            val currentEntry = currentEntries.getOrNull(index)

            if (currentEntry == null) {
                addItem(item)
            } else if (currentEntry.needsUpdate(item)) {
                updateItem(index, item)
            }

            processedItemCount += 1
        }

        if (processedItemCount < currentEntries.size) {
            val nonProcessedEntries = currentEntries.subList(processedItemCount, currentEntries.size)
            nonProcessedEntries.forEachIndexed { index, _ ->
                removeItemsAtIndices(processedItemCount + index until currentEntries.size)
            }
        }
    }

    private fun addItem(item: T) {
        val id = item.metaInformation.id

        val component = container.itemRenderer(item)
        container.add(component)
        component.addAfterInsertHook { node ->
            val element = node as? HTMLElement
            element?.setAttribute(ID_PROPERTY, id)
        }
        val index = entries.size
        entries.add(index, SmartListEntry(id, component, item, index))
    }

    private fun removeItemsAtIndices(indices: IntRange) {
        println("Removing items at indices $indices")
        for (index in indices.reversed()) {
            val entry = entries[index]
            val component = entry.component
            container.remove(component)
            component.dispose()
            entries.removeAt(index)
        }
    }

    private fun updateItem(index: Int, item: T) {
        val id = item.metaInformation.id
        container.removeAt(index)
        val component = container.itemRenderer(item)
        component.addAfterInsertHook { node ->
            val element = node as? HTMLElement
            element?.setAttribute(ID_PROPERTY, id)
        }
        container.add(index, component)
        entries[index] = SmartListEntry(id, component, item, index = index)
    }

    private fun getItemsBetween(index1: Int, index2: Int): List<SmartListEntry<T>> {
        val lowerIndex = minOf(index1, index2)
        val upperIndex = maxOf(index1, index2)
        return entries.subList(lowerIndex, upperIndex + 1)
    }
}
