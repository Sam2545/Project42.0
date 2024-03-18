import processing.core.PImage;

import java.util.List;

public class Water extends Entity{

    public static final String WATER_KEY = "water";
    public static final int WATER_PARSE_PROPERTY_COUNT = 0;

    public static final int WATER_PARSE_BEHAVIOR_PERIOD_INDEX = 0;

    //private boolean farmed;

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id       The entity's identifier.
     * @param position The entity's x/y position in the world.
     * @param images   The entity's inanimate (singular) or animation (multiple) images.
     */
    public Water(String id, Point position, List<PImage> images) {

        super(id, position, images);
        //this.farmed = false;
    }

    /*public void setFarmed(boolean farmed) {
        this.farmed = farmed;
    }*/

    /*@Override
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        if (this.farmed == true) {
            Background background = new Background("grass", imageLibrary.get("grass"), 0);
            world.setBackgroundCell(getPosition(), background);
        }
    }*/

    /*
    * Background background = new Background("grass", imageLibrary.get("grass"), 0);
            world.setBackgroundCell(target.getPosition(), background); */
}
