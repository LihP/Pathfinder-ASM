package com.focess.pathfinder.goals;

import com.focess.pathfinder.core.goal.NMSGoalItem;
import com.focess.pathfinder.core.util.NMSManager;

public class LookAtPlayerGoalItem extends NMSGoalItem {
    public static final float CHANCE = 0.02f;
    private final PointerWriter floatWriter = new PointerWriter(2, 2);

    protected LookAtPlayerGoalItem() {
        super(NMSManager.getNMSClass("PathfinderGoalLookAtPlayer", true), 4, NMSManager.getNMSClass("EntityInsentient", true), Class.class, float.class, float.class);
    }

    public LookAtPlayerGoalItem writeEntityInsentient(com.focess.pathfinder.wrapped.WrappedEntityInsentient arg) {
        this.write(0, arg);
        return this;
    }

    public LookAtPlayerGoalItem writeClass(Class<?> arg) {
        this.write(1, arg);
        return this;
    }

    public LookAtPlayerGoalItem writeFloat(float arg) {
        floatWriter.write(arg);
        return this;
    }

    @Override
    public LookAtPlayerGoalItem clear() {
        floatWriter.clear();
        return this;
    }
}