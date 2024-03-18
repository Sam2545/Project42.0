import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Farmer extends AnimationActor implements Repositionable{

    public static final String FARMER_KEY = "farmer";
    public static final int FARMER_PARSE_PROPERTY_BEHAVIOR_PERIOD_INDEX = 0;
    public static final int FARMER_PARSE_PROPERTY_ANIMATION_PERIOD_INDEX = 1;
    public static final int FARMER_PARSE_PROPERTY_RESOURCE_LIMIT_INDEX = 2;
    public static final int FARMER_PARSE_PROPERTY_COUNT = 3;

    /** Number of resources collected by the entity. */
    private int appleCount;

    /** Total number of resources the entity may hold. */
    private int appleLimit;

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
     * @param appleCount   The number of resources held by the entity.
     * @param appleLimit   The total number of resources the entity may hold.
     */
    public Farmer(String id, Point position, List<PImage> images, double animationPeriod, double behaviorPeriod, int appleCount, int appleLimit) {
        super(id, position, images, animationPeriod, behaviorPeriod);
        this.appleLimit = appleLimit;
        this.appleCount = appleCount;
    }

    @Override
    /** Executes Farmer specific Logic. */
    public void executeBehavior(World world, ImageLibrary imageLibrary, EventScheduler scheduler) {
        Point farmerTarget = findNearestDestroyedBackground(getPosition(), world);
        //Optional<Entity> farmerTarget = world.findNearest(getPosition(), new ArrayList<>(List.of(Water.class)));

        if (farmerTarget != null) {
            //System.out.println("WORKING");
            Point tgtPos = farmerTarget;

            if (moveToDestroyedTile(world, farmerTarget, scheduler)) {

                Background background = new Background("grass", imageLibrary.get("grass"), 0);
                world.setBackgroundCell(tgtPos, background);
                if (!world.isOccupied(tgtPos) && (int)(Math.random() * 3) == 0) {

                    Entity sapling = new Sapling(Sapling.SAPLING_KEY + "_stump_", tgtPos, imageLibrary.get(Sapling.SAPLING_KEY));

                    world.addEntity(sapling);
                    ((Sapling) sapling).scheduleActions(scheduler, world, imageLibrary);
                }

            }
        }

        scheduleBehavior(scheduler, world, imageLibrary);
    }

    public Point findNearestDestroyedBackground(Point start, World world) {

        //List<Background> destroyedTiles = new ArrayList<>();
        int rows = world.getNumRows();
        int cols = world.getNumCols();
        Point target = null;
        int distance = Integer.MAX_VALUE;
        System.out.println(rows + " " + cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                Point temp = new Point(j, i);
                if (world.hasBackground(temp) && world.getBackgroundCell(temp).getId().equals("stone")) {
                    int distanceTo = start.manhattanDistanceTo(temp);
                    if (distanceTo < distance) {
                        distance = distanceTo;
                        target = temp;
                    }
                }
            }
        }

        return target;
    }

    /** Attempts to move the Dude toward a target, returning True if already adjacent to it. */
    public boolean moveToDestroyedTile(World world, Point target, EventScheduler scheduler) {
        //System.out.println("moving");
        if (getPosition().adjacentTo(target)) {
            return true;
        } else {
            //System.out.println("next");
            Point nextPos = nextPositionTarget(world, target);
            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    /** Attempts to move the Dude toward a target, returning True if already adjacent to it. */
    public boolean moveToTarget(World world, Entity target, EventScheduler scheduler) {
        //System.out.println("moving");
        if (getPosition().adjacentTo(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            //System.out.println("next");
            Point nextPos = nextPositionTarget(world, target.getPosition());
            if (!getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    /** Determines a Dude's next position when moving. */
    public Point nextPositionTarget(World world, Point destination) {

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

