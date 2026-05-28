package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

import java.util.ServiceLoader;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double MOVE_SPEED = 1.0;
    private static final double SHOOT_CHANCE = 0.005;

    @Override
    public void process(GameData gameData, World world) {
        if (world.getEntities(Enemy.class).isEmpty()) {
            world.addEntity(Enemy.createDefault(gameData));
            return;
        }

        for (Entity enemy : world.getEntities(Enemy.class)) {
            move(enemy, gameData);
            shoot(enemy, world, gameData);
        }
    }

    private void move(Entity enemy, GameData gameData) {
        double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
        double changeY = Math.sin(Math.toRadians(enemy.getRotation()));
        enemy.setX(enemy.getX() + changeX * MOVE_SPEED);
        enemy.setY(enemy.getY() + changeY * MOVE_SPEED);

        if (enemy.getX() < 0) enemy.setX(gameData.getDisplayWidth());
        if (enemy.getX() > gameData.getDisplayWidth()) enemy.setX(0);
        if (enemy.getY() < 0) enemy.setY(gameData.getDisplayHeight());
        if (enemy.getY() > gameData.getDisplayHeight()) enemy.setY(0);
    }

    private void shoot(Entity enemy, World world, GameData gameData) {
        if (Math.random() < SHOOT_CHANCE) {
            double savedRotation = enemy.getRotation();
            enemy.setRotation(savedRotation + 90);
            ServiceLoader.load(ModuleLayer.boot(), BulletSPI.class)
                    .findFirst()
                    .ifPresent(spi -> world.addEntity(spi.createBullet(enemy, gameData)));
            enemy.setRotation(savedRotation);
        }
    }
}
