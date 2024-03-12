package org.uwu_snek.shadownight.customItems;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.implementations.bow.IM_HellfireBow;
import org.uwu_snek.shadownight.customItems.implementations.dagger.*;
import org.uwu_snek.shadownight.customItems.implementations.scythe.*;
import org.uwu_snek.shadownight.customItems.implementations.spear.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


// Initialize custom items
public enum ItemManager {
    // No stone version
    // No golden version
    KlaueScythe    (new IM_KlaueScythe()),
    NetheriteScythe(new IM_NetheriteScythe()),
    DiamondScythe  (new IM_DiamondScythe()),
    IronScythe     (new IM_IronScythe()),

    NetheriteDagger(new IM_NetheriteDagger()),
    DiamondDagger  (new IM_DiamondDagger()),
    IronDagger     (new IM_IronDagger()),
    StoneDagger    (new IM_StoneDagger()),
    GoldenDagger   (new IM_GoldenDagger()),

    NetheriteSpear (new IM_NetheriteSpear()),
    DiamondSpear   (new IM_DiamondSpear()),
    IronSpear      (new IM_IronSpear()),
    StoneSpear     (new IM_StoneSpear()),
    GoldenSpear    (new IM_GoldenSpear()),

    HellfireBow    (new IM_HellfireBow()),
    ;


    private final IM value;
    ItemManager(final @NotNull IM item){
        value = item;
    }

    /**
     * Returns the instance of this Item Manager.
     * This can be used to retrieve item-specific data
     * @return The manager instance
     */
    public IM getInstance() {
        return value;
    }


    private static final Map<Long, IM> lookupTable;
    static {
        final HashMap<Long, IM> lookupTableTmp = new HashMap<>();
        for(ItemManager item : values()) {
            lookupTableTmp.put(item.value.getCustomItemId().getNumericalValue(), item.value);
        }
        lookupTable = Collections.unmodifiableMap(lookupTableTmp);
    }


    /**
     * Returns the ItemManager instance with CustomItemId <id>.
     * @param id The id to search for
     * @return The manger instance
     */
    public static IM getValueFromId(final @NotNull _custom_item_id id) {
        return lookupTable.get(id.getNumericalValue());
    }
    /**
     * Returns the ItemManager instance with CustomItemId <id>.
     * @param id The id to search for
     * @return The manger instance
     */
    public static IM getValueFromId(final long id) {
        return lookupTable.get(id);
    }
}
