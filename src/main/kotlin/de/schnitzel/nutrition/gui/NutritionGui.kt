package de.schnitzel.nutrition.gui

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack


class NewNutritionGui() : ChestGui(3, ComponentHolder.of(Component.text("Nahrungsmittel Auswahl"))) {

    init {
        val pane = StaticPane(1, 1, 9, 1, Pane.Priority.HIGH)
        val outlinePane = OutlinePane(0, 0, 9, 3, Pane.Priority.LOW)

        outlinePane.addItem(
            GuiItem(ItemStack(Material.BLACK_STAINED_GLASS_PANE)
            )
        )

        this.setOnGlobalClick { event: InventoryClickEvent ->
            event.isCancelled =
                true
        }

        outlinePane.setRepeat(true)

        this.addPane(pane)
        this.addPane(outlinePane)

    }

    // Hier können Methoden zum Hinzufügen von Items oder zum Verarbeiten von Events hinzugefügt werden
}