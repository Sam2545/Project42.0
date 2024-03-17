import processing.core.PImage;

import java.util.List;

public class Tree extends HealthActor implements Transform{
    public static final String TREE_KEY = "tree";
    public static final int TREE_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int TREE_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int TREE_PARSE_PROPERTY_HEALTH_INDEX = 2;
    public static final int TREE_PARSE_PROPERTY_COUNT = 3;
    public static final double TREE_RANDOM_BEHAVIOR_PERIOD_MIN = 0.01;
    public static final double TREE_RANDOM_BEHAVIOR_PERIOD_MAX = 0.10;
    public static final double TREE_RANDOM_ANIMATION_PERIOD_MIN = 0.1;
    public static final double TREE_RANDOM_ANIMATION_PERIOD_MAX = 1.0;
    public static final int TREE_RANDOM_HEALTH_MIN = 1;
    public static final int TREE_RANDOM_HEALTH_MAX = 3;

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id              The entity's identifier.
     * @param position        The entity's x/y position in the world.
     * @param images          The entity's inanimate (singular) or animation (multiple) images.
     * @param animationPeriod The positive (non-zero) time delay between the entity's animations.
     * @param behaviorPeriod  The positive (non-zero) time delay between the entity's behaviors.
     * @param health          The entity's current health level.
     */
    public Tree(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int health) {
        super(id, position, images, animationPeriod, behaviorPeriod, health);
    }

    @Override
    /** Executes Tree specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        if (!transform(world, scheduler, imageLibrary)) {
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }

    @Override
    /** Checks the Tree's health and transforms accordingly, returning true if successful. */
    public boolean transform(World world, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (getHealth() <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + getId(), getPosition(), imageLibrary.get(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }




}
