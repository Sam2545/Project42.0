import processing.core.PImage;

import java.util.List;

public class Sapling extends HealthActor implements Transform{

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final double SAPLING_BEHAVIOR_PERIOD = 2.0;
    public static final double SAPLING_ANIMATION_PERIOD = 0.01; // Very small to react to health changes
    public static final int SAPLING_PARSE_PROPERTY_COUNT = 0;


    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id              The entity's identifier.
     * @param position        The entity's x/y position in the world.
     * @param images          The entity's inanimate (singular) or animation (multiple) images.
     */
    public Sapling(String id, Point position, List<PImage> images) {
        super(id, position, images, SAPLING_ANIMATION_PERIOD, SAPLING_BEHAVIOR_PERIOD, 0);
    }

    @Override
    /** Called when an animation action occurs. */
    public void updateImage() {

        if (getHealth() <= 0) {
            setImageIndex(0);
        } else if (getHealth() < SAPLING_HEALTH_LIMIT) {
            setImageIndex(getImages().size() * getHealth() / Sapling.SAPLING_HEALTH_LIMIT);
        } else {
            setImageIndex(getImages().size() - 1);
        }

    }

    @Override
    /** Executes Sapling specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        setHealth(getHealth() + 1);
        if (!transform(world, scheduler, imageLibrary)) {
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }

    @Override
    /** Checks the Sapling's health and transforms accordingly, returning true if successful. */
    public boolean transform(World world, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (getHealth() <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + getId(), getPosition(), imageLibrary.get(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (getHealth() >= Sapling.SAPLING_HEALTH_LIMIT) {
            AnimationActor tree = new Tree(
                    Tree.TREE_KEY + "_" + getId(),
                    getPosition(),
                    imageLibrary.get(Tree.TREE_KEY),
                    NumberUtil.getRandomDouble(Tree.TREE_RANDOM_ANIMATION_PERIOD_MIN, Tree.TREE_RANDOM_ANIMATION_PERIOD_MAX), NumberUtil.getRandomDouble(Tree.TREE_RANDOM_BEHAVIOR_PERIOD_MIN, Tree.TREE_RANDOM_BEHAVIOR_PERIOD_MAX),
                    NumberUtil.getRandomInt(Tree.TREE_RANDOM_HEALTH_MIN, Tree.TREE_RANDOM_HEALTH_MAX)
            );

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageLibrary);

            return true;
        }

        return false;
    }
}
