/** A scheduled action to be carried out by a specific entity. */
public abstract class Action {

    /** Entity enacting the action. */
    private final Entity entity;


    /**
     * Constructs an Action object with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' method are used to create specific kinds.
     *
     * @param entity The entity enacting the action.
     */
    public Action(Entity entity) {
        this.entity = entity;
    }

    /** Called when the action's scheduled time occurs. */
    public abstract void execute(EventScheduler scheduler);

    public Entity getEntity() {
        return entity;
    }

}
