module Core {
    requires Common;
    requires CommonAsteroids;
    requires javafx.graphics;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires java.net.http;

    exports dk.sdu.cbse;
    opens dk.sdu.cbse to javafx.graphics, spring.core;
    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.cbse.common.services.IPostEntityProcessingService;
    uses dk.sdu.cbse.common.asteroids.IAsteroidSplitter;
}
