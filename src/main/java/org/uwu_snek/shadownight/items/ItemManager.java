package org.uwu_snek.shadownight.items;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.bow.IM_HellfireBow;
import org.uwu_snek.shadownight.items.dagger.*;
import org.uwu_snek.shadownight.items.scythe.*;
import org.uwu_snek.shadownight.items.spear.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


// Initialize custom items
public enum ItemManager {
    // No stone version
    // No golden version
    IronScythe     (new IM_IronScythe()),
    DiamondScythe  (new IM_DiamondScythe()),
    NetheriteScythe(new IM_NetheriteScythe()),
    KlaueScythe    (new IM_KlaueScythe()),

    GoldenDagger   (new IM_GoldenDagger()),
    StoneDagger    (new IM_StoneDagger()),
    IronDagger     (new IM_IronDagger()),
    DiamondDagger  (new IM_DiamondDagger()),
    NetheriteDagger(new IM_NetheriteDagger()),

    GoldenSpear   (new IM_GoldenSpear()),
    StoneSpear    (new IM_StoneSpear()),
    IronSpear     (new IM_IronSpear()),
    DiamondSpear  (new IM_DiamondSpear()),
    NetheriteSpear(new IM_NetheriteSpear()),

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
    public static IM getValueFromId(final @NotNull CustomItemId id) {
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
