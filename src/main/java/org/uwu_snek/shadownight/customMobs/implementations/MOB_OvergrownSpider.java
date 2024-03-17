package org.uwu_snek.shadownight.customMobs.implementations;

import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.customMobs.MOB;




public class MOB_OvergrownSpider extends MOB {
    Bone center = new DisplayBone(_mob_part_type.DEBUG_MOB_PART, 1.02f, 1.02f);

    public MOB_OvergrownSpider() {
        super();
        root.addChild(center);
    }
}
