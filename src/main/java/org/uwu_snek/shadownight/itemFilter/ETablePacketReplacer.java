package org.uwu_snek.shadownight.itemFilter;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight._generated.EnchantmentOverrideCodes;

import java.lang.reflect.InvocationTargetException;
import java.util.List;




public class ETablePacketReplacer extends PacketAdapter {
    public ETablePacketReplacer(){
        super(ShadowNight.plugin, ListenerPriority.LOWEST, PacketType.Play.Server.WINDOW_DATA);
    }
    /*

    @Override
    public void onPacketSending(PacketEvent e){
        if(e.getPacketType() == PacketType.Play.Server.WINDOW_DATA){
            PacketContainer packet = e.getPacket();
            List<Integer> data = packet.getIntegers().getValues();

            //PacketContainer openSign = ShadowNight.protocolManager.createPacket(PacketType.Play.Server.WINDOW_DATA);
            // Data[1]: Enchantment hover slot
            // Data[2]: Enchantment name ID
            if((data.get(1) == 4 || data.get(1) == 5 || data.get(1) == 6) && data.get(2) != 37){
                e.setCancelled(true);
                packet.getIntegers().write(2, EnchantmentOverrideCodes.);

                try {
                    ShadowNight.protocolManager.sendServerPacket(e.getPlayer(), packet);
                }
                catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }*/
}
