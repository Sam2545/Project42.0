import processing.core.PImage;

import java.util.List;

public class House extends Entity{

    public static final int HOUSE_PARSE_PROPERTY_COUNT = 0;
    public static final String HOUSE_KEY = "house";

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id       The entity's identifier.
     * @param position The entity's x/y position in the world.
     * @param images   The entity's inanimate (singular) or animation (multiple) images.
     */
    public House(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
