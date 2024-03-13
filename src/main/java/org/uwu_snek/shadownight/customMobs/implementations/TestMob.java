package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.CustomMob;




public class TestMob extends CustomMob {
    public TestMob() {
        super(EntityType.HUSK, true);
        root = new Bone(1);
    }
}
