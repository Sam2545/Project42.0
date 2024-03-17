public interface Repositionable {

    boolean moveToTarget(World world, Entity target, EventScheduler scheduler);

    Point nextPositionTarget(World world, Point destination);

}
