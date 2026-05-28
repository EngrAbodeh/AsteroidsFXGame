package dk.sdu.cbse.collisionsystem;

import dk.sdu.cbse.common.data.CollisionEvent;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollisionDetectorTest {

    private CollisionDetector detector;
    private GameData gameData;
    private World world;

    @BeforeEach
    void setUp() {
        detector = new CollisionDetector();
        gameData = new GameData();
        world = new World();
    }

    @Test
    void collides_whenEntitiesOverlap_returnsTrue() {
        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(5);

        Entity e2 = new Entity();
        e2.setX(3); e2.setY(4); e2.setRadius(5);

        assertTrue(detector.collides(e1, e2));
    }

    @Test
    void collides_whenEntitiesApart_returnsFalse() {
        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(5);

        Entity e2 = new Entity();
        e2.setX(100); e2.setY(100); e2.setRadius(5);

        assertFalse(detector.collides(e1, e2));
    }

    @Test
    void collides_exactlyAtBoundary_returnsFalse() {
        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(5);

        Entity e2 = new Entity();
        e2.setX(10); e2.setY(0); e2.setRadius(5);

        assertFalse(detector.collides(e1, e2));
    }

    @Test
    void process_removesCollidingEntities() {
        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(10);

        Entity e2 = new Entity();
        e2.setX(5); e2.setY(0); e2.setRadius(10);

        world.addEntity(e1);
        world.addEntity(e2);

        detector.process(gameData, world);

        assertEquals(0, world.getEntities().size());
    }

    @Test
    void process_doesNotRemoveNonCollidingEntities() {
        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(5);

        Entity e2 = new Entity();
        e2.setX(100); e2.setY(100); e2.setRadius(5);

        world.addEntity(e1);
        world.addEntity(e2);

        detector.process(gameData, world);

        assertEquals(2, world.getEntities().size());
    }

    @Test
    void process_publishesEventOnCollision() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        detector.setApplicationEventPublisher(publisher);

        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(10);

        Entity e2 = new Entity();
        e2.setX(5); e2.setY(0); e2.setRadius(10);

        world.addEntity(e1);
        world.addEntity(e2);

        detector.process(gameData, world);

        verify(publisher, atLeastOnce()).publishEvent(any(CollisionEvent.class));
    }

    @Test
    void process_doesNotPublishEventWhenNoCollision() {
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        detector.setApplicationEventPublisher(publisher);

        Entity e1 = new Entity();
        e1.setX(0); e1.setY(0); e1.setRadius(5);

        Entity e2 = new Entity();
        e2.setX(100); e2.setY(100); e2.setRadius(5);

        world.addEntity(e1);
        world.addEntity(e2);

        detector.process(gameData, world);

        verify(publisher, never()).publishEvent(any());
    }
}
