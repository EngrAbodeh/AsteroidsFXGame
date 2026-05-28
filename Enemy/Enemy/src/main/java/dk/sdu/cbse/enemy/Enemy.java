package dk.sdu.cbse.enemy;

import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.GameData;

public class Enemy extends dk.sdu.cbse.common.data.Entity {

    static Enemy createDefault(GameData gameData) {
        Enemy enemy = new Enemy();
        int size = 12;

        switch ((int) (Math.random() * 4)) {
            case 0 -> { enemy.setX(Math.random() * gameData.getDisplayWidth()); enemy.setY(0); }
            case 1 -> { enemy.setX(Math.random() * gameData.getDisplayWidth()); enemy.setY(gameData.getDisplayHeight()); }
            case 2 -> { enemy.setX(0); enemy.setY(Math.random() * gameData.getDisplayHeight()); }
            default -> { enemy.setX(gameData.getDisplayWidth()); enemy.setY(Math.random() * gameData.getDisplayHeight()); }
        }

        enemy.setRotation(Math.random() * 360);
        enemy.setRadius(size);
        enemy.setColor("red");
        enemy.setPolygonCoordinates(
                 size * 1.5,  0,
                 size * 0.3, -size,
                -size,       -size * 0.4,
                -size,        size * 0.4,
                 size * 0.3,  size
        );
        enemy.setType(EntityType.ENEMY);
        return enemy;
    }
}
