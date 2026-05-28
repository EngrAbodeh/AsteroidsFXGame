package dk.sdu.cbse.common.bullet;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;

public interface BulletSPI {

    // Creates a bullet fired from the given shooter entity
    // Player calls this without knowing who will implement it
    Entity createBullet(Entity shooter, GameData gameData);
}
