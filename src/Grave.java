import processing.core.PImage;

import java.util.List;

public class Grave extends Entity{
    public static final String GRAVE_KEY = "grave";
    public static final int GRAVE_PARSE_PROPERTY_COUNT = 0;

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id       The entity's identifier.
     * @param position The entity's x/y position in the world.
     * @param images   The entity's inanimate (singular) or animation (multiple) images.
     */
    public Grave(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
