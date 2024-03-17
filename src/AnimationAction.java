public class AnimationAction extends Action{

    /** Number of animation repeats. A zero indicates indefinite repeats. */
    private int repeatCount;

    /**
     * Constructs an Action object with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' method are used to create specific kinds.
     *
     * @param entity       The entity enacting the action.
     * @param repeatCount  The number of animation repeats. A zero indicates indefinite repeats.
     */
    public AnimationAction(Entity entity, int repeatCount) {
        super( entity);
        this.repeatCount = repeatCount;
    }

    @Override
    /** Performs 'Animation' specific logic. */
    public void execute(EventScheduler scheduler) {
        ((AnimationActor)getEntity()).updateImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(getEntity(), new AnimationAction(this.getEntity(), Math.max(this.repeatCount - 1, 0)), ((AnimationActor)getEntity()).getAnimationPeriod());
        }
    }
}
