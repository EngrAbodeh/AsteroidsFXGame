package dk.sdu.cbse.player;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        if (world.getEntities(Player.class).isEmpty()) {
            world.addEntity(Player.createDefault(gameData));
            return;
        }

        for (Entity player : world.getEntities(Player.class)) {

            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - 5);


            }
            if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + 5);
            }
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                double changeX = Math.cos(Math.toRadians(player.getRotation() - 90));
                double changeY = Math.sin(Math.toRadians(player.getRotation() - 90));
                player.setX(player.getX() + changeX * 3);
                player.setY(player.getY() + changeY * 3);
            }
            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(player, gameData))
                );
            }

            if (player.getX() < 0) player.setX(gameData.getDisplayWidth());
            if (player.getX() > gameData.getDisplayWidth()) player.setX(0);
            if (player.getY() < 0) player.setY(gameData.getDisplayHeight());
            if (player.getY() > gameData.getDisplayHeight()) player.setY(0);
        }
    }

    private List<BulletSPI> getBulletSPIs() {
        List<BulletSPI> list = new ArrayList<>();
        ServiceLoader.load(ModuleLayer.boot(), BulletSPI.class).forEach(list::add);
        return list;
    }
}