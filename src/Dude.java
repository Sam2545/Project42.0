import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Dude extends AnimationActor implements Transform, Repositionable{

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int DUDE_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int DUDE_PARSE_PROPERTY_RESOURCE_LIMIT_INDEX = 2;
    public static final int DUDE_PARSE_PROPERTY_COUNT = 3;

    /** Number of resources collected by the entity. */
    private int resourceCount;

    /** Total number of resources the entity may hold. */
    private int resourceLimit;

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
     * @param resourceCount   The number of resources held by the entity.
     * @param resourceLimit   The total number of resources the entity may hold.
     */
    public Dude(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int resourceCount, int resourceLimit) {
        super(id, position, images, animationPeriod, behaviorPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    @Override
    /** Executes Dude specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> dudeTarget = findDudeTarget(world);
        if (dudeTarget.isEmpty() || !moveToTarget(world, dudeTarget.get(), scheduler) || !transform(world, scheduler, imageLibrary)) {
            scheduleBehavior(scheduler, world, imageLibrary);
        }
    }

    /** Returns the (optional) entity a Dude will path toward. */
    public Optional<Entity> findDudeTarget(World world) {
        List<Class<?>> potentialTargets;

        if (resourceCount == resourceLimit) {
            potentialTargets = List.of(House.class);
        } else {
            potentialTargets = List.of(Tree.class, Sapling.class);
        }

        return world.findNearest(getPosition(), potentialTargets);
    }

    /** Changes the Dude's graphics. */
    public boolean transform(World world, EventScheduler scheduler, ImageLibrary imageLibrary) {
        if (resourceCount < resourceLimit) {
            resourceCount += 1;
            if (resourceCount == resourceLimit) {
                AnimationActor dude = new Dude(getId(), getPosition(), imageLibrary.get(Dude.DUDE_KEY + "_carry"), getAnimationPeriod(), getBehaviorPeriod(), resourceCount, resourceLimit);

                world.removeEntity(scheduler, this);

                world.addEntity(dude);
                (dude).scheduleActions(scheduler, world, imageLibrary);

                return true;
            }
        } else {
            AnimationActor dude = new Dude(getId(), getPosition(), imageLibrary.get(Dude.DUDE_KEY), getAnimationPeriod(), getBehaviorPeriod(), 0, resourceLimit);

            world.removeEntity(scheduler, this);

            world.addEntity(dude);
            (dude).scheduleActions(scheduler, world, imageLibrary);

            return true;
        }

        return false;
    }

    /** Attempts to move the Dude toward a target, returning True if already adjacent to it. */
    public boolean moveToTarget(World world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacentTo(target.getPosition())) {
            if (target instanceof Tree) {
                ((Tree)target).setHealth(((Tree)target).getHealth() - 1);
            }
            else if(target instanceof Sapling) {
                ((Sapling)target).setHealth(((Sapling)target).getHealth() - 1);
            }
            return true;
        } else {
            Point nextPos = nextPositionTarget(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }

            return false;
        }
    }

    /** Determines a Dude's next position when moving. */
    public Point nextPositionTarget(World world, Point destination) {
        /*// Differences between the destination and current position along each axis
        int deltaX = destination.x - getPosition().x;
        int deltaY = destination.y - getPosition().y;

        // Positions one step toward the destination along each axis
        Point horizontalPosition = new Point(getPosition().x + Integer.signum(deltaX), getPosition().y);
        Point verticalPosition = new Point(getPosition().x, getPosition().y + Integer.signum(deltaY));


        // Assumes all destinations are within bounds of the world
        // If this is not the case, also check 'World.inBounds()'
        boolean horizontalOccupied = world.isOccupied(horizontalPosition) && !(world.getOccupant(horizontalPosition).get() instanceof Stump);
        boolean verticalOccupied = world.isOccupied(verticalPosition) && !(world.getOccupant(verticalPosition).get() instanceof Stump);


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
            return world.inBounds(p) && (!world.isOccupied(p) || (world.getOccupant(p).get() instanceof Stump));
        };

        BiPredicate<Point, Point> withinReach = Point::adjacentTo;

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
