package dk.sdu.cbse.player;

import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.GameData;

public class Player extends dk.sdu.cbse.common.data.Entity {

    static Player createDefault(GameData gameData) {
        Player player = new Player();
        player.setX(gameData.getDisplayWidth() / 2.0);
        player.setY(gameData.getDisplayHeight() / 2.0);
        player.setRadius(8);
        player.setRotation(0);
        player.setPolygonCoordinates(
                0,  -14,
                -8,   8,
                -3,   3,
                 0,   8,
                 3,   3,
                 8,   8
        );
        player.setColor("cyan");
        player.setType(EntityType.PLAYER);
        return player;
    }
}
