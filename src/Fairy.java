import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Fairy extends AnimationActor implements Repositionable{
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int FAIRY_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int FAIRY_PARSE_PROPERTY_COUNT = 2;


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
    public Fairy(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }

    @Override
    /** Executes Fairy specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveToTarget(world, fairyTarget.get(), scheduler)) {
                Entity sapling = new Sapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageLibrary.get(Sapling.SAPLING_KEY));

                world.addEntity(sapling);
                ((Sapling)sapling).scheduleActions(scheduler, world, imageLibrary);
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    @Override
    /** Attempts to move the Fairy toward a target, returning True if already adjacent to it. */
    public boolean moveToTarget(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionTarget(world, target.getPosition());
            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    @Override
    /** Determines a Fairy's next position when moving. */
    public Point nextPositionTarget(World world, Point destination) {
        /*// Differences between the destination and current position along each axis
        int deltaX = destination.x - getPosition().x;
        int deltaY = destination.y - getPosition().y;

        // Positions one step toward the destination along each axis
        Point horizontalPosition = new Point(getPosition().x + Integer.signum(deltaX), getPosition().y);
        Point verticalPosition = new Point(getPosition().x, getPosition().y + Integer.signum(deltaY));

        // Assumes all destinations are within bounds of the world
        // If this is not the case, also check 'World.withinBounds()'
        boolean horizontalOccupied = world.isOccupied(horizontalPosition);
        boolean verticalOccupied = world.isOccupied(verticalPosition);

        // Move along the farther direction, preferring horizontal
        if (Math.abs(deltaX) >= Math.abs(deltaY)) {
            if (!horizontalOccupied) {
                return horizontalPosition;
            } else if (!verticalOccupied) {
                return verticalPosition;
            }
        } else {
            if (!verticalOccupied) {
                return verticalPosition;
            } else if (!horizontalOccupied) {
                return horizontalPosition;
            }
        }*/

        PathingStrategy pathingStrategy = new AStarPathingStrategy();

        // destination = end
        // current point = start
        Predicate<Point> canPassThrough = (p) -> {
            return world.inBounds(p) && (!world.isOccupied(p));
        };

        BiPredicate<Point, Point> withinReach = (p1, p2) -> {
            return p1.adjacentTo(p2);
        };

        List<Point> path = pathingStrategy.computePath(getPosition(), destination, canPassThrough, withinReach, SingleStepPathingStrategy.CARDINAL_NEIGHBORS);



        if (path.isEmpty()) {
            if (getPosition().adjacentTo(destination)) {
                return getPosition();
            }
        }
        else{
            Point nextPoint = path.get(0);
            if (nextPoint != destination || nextPoint != getPosition()) {
                return nextPoint;
            }
        }


        return getPosition();
    }
}
