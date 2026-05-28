package dk.sdu.cbse.bullet;


import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.EntityType;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity bullet : world.getEntities()) {

            if (bullet instanceof Bullet) {

                // Move the bullet forward in the direction it was fired
                double changeX = Math.cos(Math.toRadians(bullet.getRotation() - 90));
                double changeY = Math.sin(Math.toRadians(bullet.getRotation() - 90));
                bullet.setX(bullet.getX() + changeX * 5);
                bullet.setY(bullet.getY() + changeY * 5);

                // Remove bullet if it goes off the screen
                if (bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth()
                        || bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight()) {
                    world.removeEntity(bullet);
                }
            }
        }
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        // Create a new bullet at the shooter's position
        Bullet bullet = new Bullet();

        // Small square shape for the bullet
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);

        // Position bullet at the nose of the shooter
        double changeX = Math.cos(Math.toRadians(shooter.getRotation() - 90));
        double changeY = Math.sin(Math.toRadians(shooter.getRotation() - 90));
        bullet.setX(shooter.getX() + changeX * 10);
        bullet.setY(shooter.getY() + changeY * 10);

        // Bullet travels in the same direction the shooter is facing
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);
        bullet.setColor(shooter.getType() == EntityType.ENEMY ? "orange" : "yellow");

        return bullet;
    }
}
