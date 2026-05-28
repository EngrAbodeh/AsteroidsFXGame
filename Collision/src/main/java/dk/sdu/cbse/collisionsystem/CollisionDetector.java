package dk.sdu.cbse.collisionsystem;

import dk.sdu.cbse.common.data.CollisionEvent;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionDetector implements IPostEntityProcessingService, ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    public CollisionDetector() {
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        Set<String> processed = new HashSet<>();

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity entity1 = entities.get(i);
                Entity entity2 = entities.get(j);

                String pairKey = entity1.getID() + entity2.getID();
                if (processed.contains(pairKey)) continue;
                processed.add(pairKey);

                if (collides(entity1, entity2)) {
                    if (entity1.getType() == EntityType.ASTEROID && entity2.getType() == EntityType.ASTEROID) {
                        bounceAsteroids(entity1, entity2);
                    } else {
                        world.removeEntity(entity1);
                        world.removeEntity(entity2);
                        if (publisher != null) {
                            publisher.publishEvent(new CollisionEvent(entity1, entity2, world));
                        }
                    }
                }
            }
        }
    }

    private void bounceAsteroids(Entity a1, Entity a2) {
        double dx = a2.getX() - a1.getX();
        double dy = a2.getY() - a1.getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;
        double nx = dx / dist;
        double ny = dy / dist;

        double v1x = Math.cos(Math.toRadians(a1.getRotation()));
        double v1y = Math.sin(Math.toRadians(a1.getRotation()));
        double v2x = Math.cos(Math.toRadians(a2.getRotation()));
        double v2y = Math.sin(Math.toRadians(a2.getRotation()));

        // Equal-mass elastic collision:
        double dot = (v1x - v2x) * nx + (v1y - v2y) * ny;
        a1.setRotation(Math.toDegrees(Math.atan2(v1y - dot * ny, v1x - dot * nx)));
        a2.setRotation(Math.toDegrees(Math.atan2(v2y + dot * ny, v2x + dot * nx)));

        // This will push apart to prevent sticking on the next frame
        double overlap = (a1.getRadius() + a2.getRadius()) - dist;
        a1.setX(a1.getX() - nx * overlap / 2);
        a1.setY(a1.getY() - ny * overlap / 2);
        a2.setX(a2.getX() + nx * overlap / 2);
        a2.setY(a2.getY() + ny * overlap / 2);
    }

    public Boolean collides(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < (entity1.getRadius() + entity2.getRadius());
    }
}
