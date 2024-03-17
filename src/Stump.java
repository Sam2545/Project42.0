import processing.core.PImage;

import java.util.List;

public class Stump extends Entity{
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_PARSE_PROPERTY_COUNT = 0;

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id       The entity's identifier.
     * @param position The entity's x/y position in the world.
     * @param images   The entity's inanimate (singular) or animation (multiple) images.
     */
    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
