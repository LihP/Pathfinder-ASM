package com.focess.pathfinder.core.goal;

import com.focess.pathfinder.core.builder.PathfinderClassLoader;
import com.focess.pathfinder.core.util.NMSManager;
import com.focess.pathfinder.entity.FocessEntity;
import com.focess.pathfinder.goal.Goal;
import com.focess.pathfinder.goal.GoalItem;
import com.focess.pathfinder.goal.GoalSelector;
import com.focess.pathfinder.goal.WrappedGoal;
import com.focess.pathfinder.goals.Goals;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SimpleGoalSelector implements GoalSelector {

    private final FocessEntity entity;
    private final List<WrappedGoal> wrappedGoals = Lists.newArrayList();

    public SimpleGoalSelector(FocessEntity focessEntity) {
        this.entity = focessEntity;
    }

    private void update() {
        this.wrappedGoals.clear();
        Object nmsEntity = NMSManager.getNMSEntity(this.entity.getBukkitEntity());
        Field goalSelector = NMSManager.getField(NMSManager.EntityInsentient, "goalSelector");
        Field targetSelector = NMSManager.getField(NMSManager.EntityInsentient, "targetSelector");
        try {
            Collection<?> nmsWrappedGoals = (Collection<?>) NMSManager.PathfinderGoalsField.get(goalSelector.get(nmsEntity));
            Collection<?> nmsWrappedGoals2 = (Collection<?>) NMSManager.PathfinderGoalsField.get(targetSelector.get(nmsEntity));
            for (Object nmsWrappedGoal : nmsWrappedGoals) {
                Object nmsGoal = NMSManager.PathfinderGoalItema.get(nmsWrappedGoal);
                int priority = (int) NMSManager.PathfinderGoalItemb.get(nmsWrappedGoal);
                if (nmsGoal.getClass().getName().equals("com.focess.pathfinder.core.goal.NMSGoal")) {
                    Field goalField = PathfinderClassLoader.NMSGoal.getDeclaredField("goal");
                    goalField.setAccessible(true);
                    Goal goal = (Goal) goalField.get(nmsGoal);
                    wrappedGoals.add(new WrappedGoal(goal.getGoalItem(), nmsGoal,goal, priority, false));
                    continue;
                }
                List<GoalItem> goalItems = Goals.getNMSGoalItem(nmsGoal.getClass());
                wrappedGoals.add(new WrappedGoal(goalItems, nmsGoal, priority, false));
            }
            for (Object nmsWrappedGoal : nmsWrappedGoals2) {
                Object nmsGoal = NMSManager.PathfinderGoalItema.get(nmsWrappedGoal);
                int priority = (int) NMSManager.PathfinderGoalItemb.get(nmsWrappedGoal);
                if (nmsGoal.getClass().getName().equals("com.focess.pathfinder.core.goal.NMSGoal")) {
                    Field goalField = PathfinderClassLoader.NMSGoal.getDeclaredField("goal");
                    goalField.setAccessible(true);
                    Goal goal = (Goal) goalField.get(nmsGoal);
                    wrappedGoals.add(new WrappedGoal(goal.getGoalItem(), nmsGoal,goal, priority, true));
                    continue;
                }
                List<GoalItem> goalItems = Goals.getNMSGoalItem(nmsGoal.getClass());
                wrappedGoals.add(new WrappedGoal(goalItems, nmsGoal, priority, true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<GoalItem> getGoalItems() {
        update();
        Set<GoalItem> items = Sets.newHashSet();
        for (WrappedGoal goal : wrappedGoals)
            items.addAll(goal.getGoalItems());
        return items;
    }

    @Override
    public void removeGoal(GoalItem goalItem) {
        update();
        for (WrappedGoal goal : wrappedGoals)
            if (goal.getGoalItems().contains(goalItem))
                removeExactGoal(goal);
    }

    @Override
    public boolean containsGoal(GoalItem goalItem) {
        update();
        for (WrappedGoal goal : wrappedGoals)
            if (goal.getGoalItems().contains(goalItem))
                return true;
        return false;
    }

    @Override
    public void removeExactGoal(WrappedGoal goal) {
        Object nmsEntity = NMSManager.getNMSEntity(this.entity.getBukkitEntity());
        try {
            if (goal.getControls().contains(Goal.Control.TARGET)) {
                Object targetSelector = NMSManager.getField(NMSManager.EntityInsentient, "targetSelector").get(nmsEntity);
                NMSManager.PathfinderGoalSelectorRemove.invoke(targetSelector, goal.toNMS());
            } else {
                Object goalSelector = NMSManager.getField(NMSManager.EntityInsentient, "goalSelector").get(nmsEntity);
                NMSManager.PathfinderGoalSelectorRemove.invoke(goalSelector, goal.toNMS());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGoal(WrappedGoal goal) {
        try {
            Object nmsEntity = NMSManager.getNMSEntity(this.entity.getBukkitEntity());
            if (goal.getControls().contains(Goal.Control.TARGET)) {
                Object targetSelector = NMSManager.getField(NMSManager.EntityInsentient, "targetSelector").get(nmsEntity);
                NMSManager.PathfinderGoalSelectorAdd.invoke(targetSelector, goal.getPriority(), goal.toNMS());
            } else {
                Object goalSelector = NMSManager.getField(NMSManager.EntityInsentient, "goalSelector").get(nmsEntity);
                NMSManager.PathfinderGoalSelectorAdd.invoke(goalSelector, goal.getPriority(), goal.toNMS());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsExactGoal(WrappedGoal goal) {
        update();
        for (WrappedGoal g : wrappedGoals)
            if (g.equals(goal))
                return true;
        return false;
    }

    @Override
    public List<WrappedGoal> getGoal(GoalItem goalItem) {
        update();
        List<WrappedGoal> foundGoals = Lists.newArrayList();
        for (WrappedGoal goal : wrappedGoals)
            if (goal.getGoalItems().contains(goalItem))
                foundGoals.add(goal);
        return foundGoals;
    }

    @Override
    public List<WrappedGoal> getGoals() {
        update();
        return this.wrappedGoals;
    }

}
