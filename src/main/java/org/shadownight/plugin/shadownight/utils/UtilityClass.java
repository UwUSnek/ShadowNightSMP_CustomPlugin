package org.shadownight.plugin.shadownight.utils;



public abstract class UtilityClass {
    public UtilityClass(){
        throw new RuntimeException("Tried to instantiate utility class \"" + this.getClass().getName() + "\"");
    }
}
