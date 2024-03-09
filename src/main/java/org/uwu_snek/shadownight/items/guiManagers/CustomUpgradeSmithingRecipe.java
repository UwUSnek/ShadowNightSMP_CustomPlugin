package org.uwu_snek.shadownight.items.guiManagers;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import static org.uwu_snek.shadownight.items.IM.itemIdKey;




public final class CustomUpgradeSmithingRecipe {
    private final IM base_IM;
    private final ItemStack base;
    private final Material template;
    private final Material material;
    private final IM result_IM;
    private final ItemStack result;

    public CustomUpgradeSmithingRecipe(IM base, Material template, Material material, IM result) {
        this.base     = base.createDefaultItemStack();
        this.base_IM  = base;
        this.template = template;
        this.material = material;
        this.result   = result.createDefaultItemStack();
        this.result_IM = result;
    }




    public static void onPrepareSmithing(final @NotNull PrepareSmithingEvent event) {
        SmithingInventory inv = event.getInventory();
        final ItemStack item = inv.getInputEquipment();
        Long id = ItemUtils.getCustomItemId(item);

        if(id != null) {
            CustomUpgradeSmithingRecipe recipe = ItemManager.getValueFromId(id).getUpgradeRecipe();

            if(
                recipe != null &&
                inv.getInputMineral() != null &&
                inv.getInputMineral().getType() == recipe.material &&
                inv.getInputTemplate() != null &&
                inv.getInputTemplate().getType() == recipe.template
            ) {
                ItemStack b = recipe.base.clone();
                b.setType(recipe.result.getType());


                // Retrieve and validate item metas
                ItemMeta b_meta = b.getItemMeta();
                ItemMeta r_meta = recipe.result.getItemMeta();


                // Keep display name if different from the default one. Use default name of the new CustomItem otherwise
                Component b_displayName = b_meta.displayName();
                Component b_default_displayName = Component.text(recipe.base_IM.getDisplayName());
                if(b_displayName == null || b_displayName.equals(b_default_displayName)) {
                    b_meta.displayName(Component.text(recipe.result_IM.getDisplayName()));
                }


                // Set the new Attack Speed attribute
                b_meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
                var base_modifiers = r_meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED);
                if(base_modifiers != null) for(AttributeModifier a : base_modifiers) {
                    b_meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, a);
                }


                // Set the new CustomItemId
                PersistentDataContainer container = b_meta.getPersistentDataContainer();
                container.set(itemIdKey, PersistentDataType.LONG, recipe.result_IM.getCustomItemId().getNumericalValue());


                // Set the new CustomModelData
                b_meta.setCustomModelData(r_meta.getCustomModelData());


                // Set the custom result (and override the vanilla one)
                b.setItemMeta(b_meta);
                event.setResult(b);
            }
        }
    }
}
