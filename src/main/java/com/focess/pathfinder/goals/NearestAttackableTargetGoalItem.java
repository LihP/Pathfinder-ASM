package com.focess.pathfinder.goals;

import com.focess.pathfinder.core.goal.NMSGoalItem;
import com.focess.pathfinder.core.util.NMSManager;
import com.focess.pathfinder.wrapped.WrappedEntityLiving;
import com.focess.pathfinder.wrapped.WrappedNMSPredicate;

import java.util.function.Predicate;

public class NearestAttackableTargetGoalItem extends NMSGoalItem {
    public static final int RECIPROCAL_CHANCE = 10;
    public static final boolean CHECK_CAN_NAVIGATE = false;
    public static final WrappedNMSPredicate<WrappedEntityLiving> TARGET_PREDICATE = null;
    private final PointerWriter booleanWriter = new PointerWriter(3, 2);

    protected NearestAttackableTargetGoalItem() {
        super(NMSManager.getNMSClass("PathfinderGoalNearestAttackableTarget", true), 6, NMSManager.getNMSClass("EntityInsentient", true), Class.class, int.class, boolean.class, boolean.class, Predicate.class);
    }

    public NearestAttackableTargetGoalItem writeEntityInsentient(com.focess.pathfinder.wrapped.WrappedEntityInsentient arg) {
        this.write(0, arg);
        return this;
    }

    public NearestAttackableTargetGoalItem writeClass(Class<?> arg) {
        this.write(1, arg);
        return this;
    }

    public NearestAttackableTargetGoalItem writeInt(int arg) {
        this.write(2, arg);
        return this;
    }

    public NearestAttackableTargetGoalItem writeBoolean(boolean arg) {
        booleanWriter.write(arg);
        return this;
    }

    public NearestAttackableTargetGoalItem writePredicate(WrappedNMSPredicate<com.focess.pathfinder.wrapped.WrappedEntityLiving> arg) {
        this.write(5, arg);
        return this;
    }

    @Override
    public NearestAttackableTargetGoalItem clear() {
        booleanWriter.clear();
        return this;
    }

}