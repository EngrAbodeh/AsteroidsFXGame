package dk.sdu.cbse.bullet;

import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class BulletPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        // Bullets are created dynamically by the player.
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove all bullets from the world on the cleanup
        for (Entity e : world.getEntities()) {
            if (e instanceof Bullet) {
                world.removeEntity(e);
            }
        }
    }
}
