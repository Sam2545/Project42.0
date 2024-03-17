public class BehaviorAction extends Action{

    /** World in which the action occurs. */
    private final World world;
    /** Image data that may be used by the action. */
    private final ImageLibrary imageLibrary;

    /**
     * Constructs an Action object with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' method are used to create specific kinds.
     *
     * @param entity       The entity enacting the action.
     * @param world        The world in which the action occurs.
     * @param imageLibrary The image data that may be used by the action.
     */
    public BehaviorAction(Entity entity, World world, ImageLibrary imageLibrary) {
        super(entity);
        this.world = world;
        this.imageLibrary = imageLibrary;
    }

    @Override
    /** Performs 'Behavior' specific logic. */
    public void execute(EventScheduler scheduler) {
        ((BehaviorActor)getEntity()).executeBehavior(world, imageLibrary, scheduler);
    }
}
