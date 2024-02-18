package org.shadownight.plugin.shadownight.items;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.scythe.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


// Initialize custom items
public enum ItemManager {
    IronScythe     (new IM_IronScythe()),
    DiamondScythe  (new IM_DiamondScythe()),
    NetheriteScythe(new IM_NetheriteScythe()),
    KlaueScythe    (new IM_KlaueScythe()),
    ;


    private final IM value;
    ItemManager(@NotNull final IM item){
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
            lookupTableTmp.put(item.value.getCustomId().getNumericalValue(), item.value);
        }
        lookupTable = Collections.unmodifiableMap(lookupTableTmp);
    }


    /**
     * Returns the ItemManager instance with CustomItemId <id>.
     * @param id The id to search for
     * @return The manger instance
     */
    public static IM getValueFromId(@NotNull final CustomItemId id) {
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
