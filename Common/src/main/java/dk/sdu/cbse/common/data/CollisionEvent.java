package dk.sdu.cbse.common.data;

public class CollisionEvent {

    private final Entity entity1;
    private final Entity entity2;
    private final World world;

    public CollisionEvent(Entity entity1, Entity entity2, World world) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.world = world;
    }

    public Entity getEntity1() {
        return entity1;
    }

    public Entity getEntity2() {
        return entity2;
    }

    public World getWorld() {
        return world;
    }
}
