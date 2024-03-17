import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mushroom extends BehaviorActor{

    public static final String MUSHROOM_KEY = "mushroom";
    public static final int MUSHROOM_PARSE_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int MUSHROOM_PARSE_PROPERTY_COUNT = 1;

    /**
     * Constructs an Entity with specified characteristics.
     * In the base program, this is not called directly.
     * Instead, the encapsulated 'create' methods are used to create specific types of entities.
     *
     * @param id              The entity's identifier.
     * @param position        The entity's x/y position in the world.
     * @param images          The entity's inanimate (singular) or animation (multiple) images.
     * @param behaviorPeriod  The positive (non-zero) time delay between the entity's behaviors.
     */
    public Mushroom(String id, Point position, List<PImage> images, double behaviorPeriod) {
        super(id, position, images, behaviorPeriod);
    }

    @Override
    /** Executes Mushroom specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        List<Point> adjacentPositions = new ArrayList<>(List.of(
                new Point(getPosition().x - 1, getPosition().y),
                new Point(getPosition().x + 1, getPosition().y),
                new Point(getPosition().x, getPosition().y - 1),
                new Point(getPosition().x, getPosition().y + 1)
        ));
        Collections.shuffle(adjacentPositions);

        List<Point> mushroomBackgroundPositions = new ArrayList<>();
        List<Point> mushroomEntityPositions = new ArrayList<>();
        for (Point adjacentPosition : adjacentPositions) {
            if (world.inBounds(adjacentPosition) && !world.isOccupied(adjacentPosition) && world.hasBackground(adjacentPosition)) {
                Background bg = world.getBackgroundCell(adjacentPosition);
                if (bg.getId().equals("grass")) {
                    mushroomBackgroundPositions.add(adjacentPosition);
                } else if (bg.getId().equals("grass_mushrooms")) {
                    mushroomEntityPositions.add(adjacentPosition);
                }
            }
        }

        if (!mushroomBackgroundPositions.isEmpty()) {
            Point position = mushroomBackgroundPositions.get(0);

            Background background = new Background("grass_mushrooms", imageLibrary.get("grass_mushrooms"), 0);
            world.setBackgroundCell(position, background);
        } else if (!mushroomEntityPositions.isEmpty()) {
            Point position = mushroomEntityPositions.get(0);

            Entity mushroom = new Mushroom(Mushroom.MUSHROOM_KEY, position, imageLibrary.get(Mushroom.MUSHROOM_KEY), this.getBehaviorPeriod() * 4.0);

            world.addEntity(mushroom);
            ((Mushroom)mushroom).scheduleActions(scheduler, world, imageLibrary);
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }
}
