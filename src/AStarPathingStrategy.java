import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


// public class
public class AStarPathingStrategy implements PathingStrategy {

    /**
     * Return a list containing a single point representing the next step toward a goal
     * If the start is within reach of the goal, the returned list is empty.
     *
     * @param start the point to begin the search from
     * @param end the point to search for a point within reach of
     * @param canPassThrough a function that returns true if the given point is traversable
     * @param withinReach a function that returns true if both points are within reach of each other
     * @param potentialNeighbors a function that returns the neighbors of a given point, as a stream
     */
    public List<Point> computePath(
            Point start,
            Point end,
            Predicate<Point> canPassThrough,
            BiPredicate<Point, Point> withinReach,
            Function<Point, Stream<Point>> potentialNeighbors
    ) {
        List<Point> openSet = new ArrayList<Point>();
        openSet.add(start);
        List<Point> closedSet = new ArrayList<Point>();
        Map<Point, Point> cameThrough = new HashMap<>();
        Map<Point, Integer> gscore = new HashMap<>();
        gscore.put(start, 0);
        Map<Point, Integer> hscore = new HashMap<>();
        hscore.put(start, start.manhattanDistanceTo(end));
        Map<Point, Integer> fscore = new HashMap<>();
        fscore.put(start, start.manhattanDistanceTo(end));

        while(!withinReach.test(openSet.get(0), end)) {

            Point curr = openSet.get(0);
            List<Point> neighbors = potentialNeighbors.apply(curr)
                    .filter(canPassThrough)
                    .filter((point -> !closedSet.contains(point)))
                    .toList();

            for (int i = 0; i < neighbors.size(); i++) {

                if (!gscore.containsKey(neighbors.get(i)) || (gscore.get(neighbors.get(i)) > gscore.get(curr) + 1)) {

                    if (openSet.contains(neighbors.get(i))) openSet.remove(neighbors.get(i));


                    openSet.add(neighbors.get(i));
                    gscore.put(neighbors.get(i), gscore.get(curr) + 1);
                    hscore.put(neighbors.get(i), neighbors.get(i).manhattanDistanceTo(end));
                    fscore.put(neighbors.get(i), gscore.get(neighbors.get(i)) + hscore.get(neighbors.get(i)));
                    cameThrough.put(neighbors.get(i), curr);
                }

            }

            closedSet.add(curr);
            openSet.remove(curr);

            if (openSet.isEmpty()) {
                return List.of();
            }

            // gets lowest f score then adds to the open set
            int lowestfscore = Integer.MAX_VALUE;
            Point lowestfPoint = null;
            for (int i = 0; i < openSet.size(); i++) {
                if(fscore.get(openSet.get(i)) < lowestfscore) {
                    lowestfscore = fscore.get(openSet.get(i));
                    lowestfPoint = openSet.get(i);
                }
            }


            openSet.remove(lowestfPoint);
            openSet.add(0, lowestfPoint);


            //System.out.println(true);

            // sort openset with lowest f score at the top of the list at the end of the loop
        }

        List<Point> finalPath = new ArrayList<>();
        Point curr = openSet.get(0);
        while(curr != start) {
            finalPath.add(curr);
            curr = cameThrough.get(curr);
        }
        finalPath = finalPath.reversed();

        return finalPath;
    }
}
