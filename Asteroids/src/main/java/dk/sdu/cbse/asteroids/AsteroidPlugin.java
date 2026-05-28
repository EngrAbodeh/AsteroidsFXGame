package dk.sdu.cbse.asteroids;

import dk.sdu.cbse.common.asteroids.Asteroid;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < 5; i++) {
            world.addEntity(createAsteroid(gameData));
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            world.removeEntity(asteroid);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = new Asteroid();
        Random rnd = new Random();
        int size = rnd.nextInt(20) + 25;
        asteroid.setPolygonCoordinates(
                 size * 0.5,  -size,
                 size,        -size * 0.4,
                 size * 0.8,   size * 0.5,
                 size * 0.3,   size,
                -size * 0.5,   size * 0.8,
                -size,         size * 0.2,
                -size * 0.7,  -size * 0.6,
                -size * 0.2,  -size
        );
        asteroid.setRadius(size);
        asteroid.setColor("gray");
        asteroid.setType(EntityType.ASTEROID);
        asteroid.setRotation(rnd.nextInt(360));

        // Spawn on a random edge so asteroids never start on top of the player
        int edge = rnd.nextInt(4);
        switch (edge) {
            case 0 -> { asteroid.setX(rnd.nextInt(gameData.getDisplayWidth())); asteroid.setY(0); }
            case 1 -> { asteroid.setX(rnd.nextInt(gameData.getDisplayWidth())); asteroid.setY(gameData.getDisplayHeight()); }
            case 2 -> { asteroid.setX(0); asteroid.setY(rnd.nextInt(gameData.getDisplayHeight())); }
            default -> { asteroid.setX(gameData.getDisplayWidth()); asteroid.setY(rnd.nextInt(gameData.getDisplayHeight())); }
        }

        return asteroid;
    }
}