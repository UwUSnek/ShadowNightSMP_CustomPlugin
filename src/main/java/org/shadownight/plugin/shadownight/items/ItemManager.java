package org.shadownight.plugin.shadownight.items;


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


    private final IM_CustomItem value;
    ItemManager(IM_CustomItem item){
        value = item;
    }
    public IM_CustomItem getValue() {
        return value;
    }
    public static IM_CustomItem getValueFromId(CustomItemId id) {
        return lookupTable.get(id.getValue());
    }
    public static IM_CustomItem getValueFromId(long id) {
        return lookupTable.get(id);
    }


    private static final Map<Long, IM_CustomItem> lookupTable;
    static {
        HashMap<Long, IM_CustomItem> lookupTableTmp = new HashMap<>();
        for(ItemManager item : values()) {
            lookupTableTmp.put(item.value.getCustomId().getValue(), item.value);
        }
        lookupTable = Collections.unmodifiableMap(lookupTableTmp);
    }
}
