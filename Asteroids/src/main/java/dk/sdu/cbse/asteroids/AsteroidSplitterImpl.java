package dk.sdu.cbse.asteroids;

import dk.sdu.cbse.common.asteroids.Asteroid;
import dk.sdu.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.World;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private static final float MIN_SIZE = 10;

    @Override
    public void createSplitAsteroids(Entity e, World world) {
        if (!(e instanceof Asteroid)) {
            return;
        }
        world.removeEntity(e);

        if (e.getRadius() <= MIN_SIZE) {
            return;
        }

        float newSize = (float) e.getRadius() / 2;

        for (int i = 0; i < 2; i++) {
            double splitRotation = e.getRotation() + (i == 0 ? 45 : -45);
            double angle = Math.toRadians(splitRotation);
            Asteroid split = new Asteroid();
            split.setPolygonCoordinates(
                     newSize * 0.5,  -newSize,
                     newSize,        -newSize * 0.4,
                     newSize * 0.8,   newSize * 0.5,
                     newSize * 0.3,   newSize,
                    -newSize * 0.5,   newSize * 0.8,
                    -newSize,         newSize * 0.2,
                    -newSize * 0.7,  -newSize * 0.6,
                    -newSize * 0.2,  -newSize
            );
            split.setX(e.getX() + Math.cos(angle) * newSize * 2);
            split.setY(e.getY() + Math.sin(angle) * newSize * 2);
            split.setRadius(newSize);
            split.setRotation(splitRotation);
            split.setColor("gray");
            split.setType(EntityType.ASTEROID);
            world.addEntity(split);
        }
    }
}