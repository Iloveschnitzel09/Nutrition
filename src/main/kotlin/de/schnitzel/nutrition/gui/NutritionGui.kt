package de.schnitzel.nutrition.gui

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.mainDispatcher
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import de.schnitzel.nutrition.database.DatabaseService
import de.schnitzel.nutrition.util.NutritionData
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import kotlinx.coroutines.withContext

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack


class NutritionGui(player: Player) : ChestGui(6, ComponentHolder.of(Component.text("Nahrungsmittel Auswahl"))) {

    init {
        val pane = StaticPane(1, 1, 7, 6, Pane.Priority.HIGH)
        val outlinePane = OutlinePane(0, 0, 9, 6, Pane.Priority.LOW)

        outlinePane.addItem(
            GuiItem(
                createItemStack(Material.BLACK_STAINED_GLASS_PANE, " ")
            )
        )

        pane.addItem(GuiItem(ItemStack(Material.MILK_BUCKET)), 0, 0)
        pane.addItem(GuiItem(ItemStack(Material.SUGAR)), 0, 1)
        pane.addItem(GuiItem(ItemStack(Material.WHEAT)), 0, 2)
        pane.addItem(GuiItem(ItemStack(Material.APPLE)), 0, 3)
        pane.addItem(GuiItem(ItemStack(Material.COOKED_BEEF)), 0, 4)


        plugin.launch {
            val data = DatabaseService.loadNutritionData(player.uniqueId)

            withContext(plugin.entityDispatcher(player)) {
                val values = createArray(data).map { it.coerceIn(0, 6) }

                values.forEachIndexed { index, height ->
                    val x = 1
                    for (j in 0 until height) {
                        val y = index
                        val yy = 5 - j
                        pane.addItem(
                            GuiItem(ItemStack(createItemStack(Material.LIME_STAINED_GLASS_PANE, "Progress"))),
                            x + j,
                            index
                        )
                    }
                }

                update()
            }
        }

        this.setOnGlobalClick { event: InventoryClickEvent ->
            event.isCancelled =
                true
        }

        outlinePane.setRepeat(true)

        this.addPane(pane)
        this.addPane(outlinePane)

    }

    fun createArray(data: NutritionData): Array<Int> {
        return arrayOf(
            (data.dairy / 10.0 * 6).toInt(),
            (data.sugar / 10.0 * 6).toInt(),
            (data.cereals / 10.0 * 6).toInt(),
            (data.fruit / 10.0 * 6).toInt(),
            (data.meat / 10.0 * 6).toInt()
        )
    }

    fun createItemStack(itemType: Material, name: String) = buildItem(itemType, 1) {
        displayName {
            primary(
                PlainTextComponentSerializer.plainText().serialize(Component.text(name)),
                TextDecoration.ITALIC
            )
        }
    }
}