// Generated by UwU_Snek's silly little Python script



package org.uwu_snek.shadownight._generated._mob_presets;
import org.uwu_snek.shadownight.customMobs.MOB;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
public abstract class _mob_preset_debug_mob extends MOB {
    protected Bone main = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);

    public _mob_preset_debug_mob() {
        super();
        root.addChild(main);
    }
}