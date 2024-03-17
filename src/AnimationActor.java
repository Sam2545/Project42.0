import processing.core.PImage;

import java.util.List;

public abstract class AnimationActor extends BehaviorActor {

    /** Positive (non-zero) time delay between the entity's animations. */
    private double animationPeriod;

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
     */
    public AnimationActor(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, behaviorPeriod);
        this.animationPeriod = animationPeriod;
    }

    /** Called when an animation action occurs. */
    public void updateImage() {

        setImageIndex(getImageIndex() + 1);

    }

    @Override
    /** Called to begin animation and/or behavior for an entity. */
    public void scheduleActions(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduleAnimation(scheduler, world, imageLibrary);
        scheduleBehavior(scheduler, world, imageLibrary);

    }

    /** Begins all animation updates for the entity. */
    public void scheduleAnimation(EventScheduler scheduler, World world, ImageLibrary imageLibrary) {
        scheduler.scheduleEvent(this, new AnimationAction(this, 0), animationPeriod);
    }

    public double getAnimationPeriod() {

        return animationPeriod;

    }




}
