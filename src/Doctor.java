
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Doctor extends AnimationActor implements Repositionable{
    public static final String DOCTOR_KEY = "doctor";
    public static final int DOCTOR_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int DOCTOR_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int DOCTOR_PARSE_PROPERTY_COUNT = 2;

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
    public Doctor(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod) {
        super(id, position, images, animationPeriod, behaviorPeriod);
    }
    @Override
    /** Executes Doctor specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Optional<Entity> doctorTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Grave.class)));

        if (doctorTarget.isPresent()) {
            Point tgtPos = doctorTarget.get().getPosition();

            if (moveToTarget(world, doctorTarget.get(), scheduler)) {
                Dude dude = new Dude(Dude.DUDE_KEY + "_" + doctorTarget.get().getId(), tgtPos, imageLibrary.get(Dude.DUDE_KEY), 0.3, 1, 0, 5);
                world.addEntity(dude);
                world.removeEntity(scheduler, this);
                dude.scheduleActions(scheduler, world, imageLibrary);
            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }
    @Override
    /** Attempts to move the Doctor toward a target, returning True if already adjacent to it. */
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
    /** Determines a Soldier's next position when moving. */
    public Point nextPositionTarget(World world, Point destination) {
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

