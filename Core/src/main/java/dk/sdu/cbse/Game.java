package dk.sdu.cbse;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final List<IGamePluginService> gamePluginServices;
    private final List<IEntityProcessingService> entityProcessingServices;
    private final List<IPostEntityProcessingService> postEntityProcessingServices;
    private final CollisionEventHandler collisionEventHandler;
    private Text scoreText;

    Game(List<IGamePluginService> gamePluginServices,
         List<IEntityProcessingService> entityProcessingServices,
         List<IPostEntityProcessingService> postEntityProcessingServices,
         CollisionEventHandler collisionEventHandler) {
        this.gamePluginServices = gamePluginServices;
        this.entityProcessingServices = entityProcessingServices;
        this.postEntityProcessingServices = postEntityProcessingServices;
        this.collisionEventHandler = collisionEventHandler;
    }

    public void start(Stage window) throws Exception {
        collisionEventHandler.resetScore();
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        scoreText = new Text("Score: 0");
        scoreText.setX(gameData.getDisplayWidth() - 100);
        scoreText.setY(20);
        scoreText.setFill(Color.WHITE);
        gameWindow.getChildren().add(scoreText);

        gameWindow.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(gameWindow, Color.BLACK);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        for (IGamePluginService plugin : gamePluginServices) {
            plugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            applyColor(polygon, entity);
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        window.setScene(scene);
        window.setTitle("AsteroidsFX 2D-Game");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        for (IEntityProcessingService processor : entityProcessingServices) {
            processor.process(gameData, world);
        }
        for (IPostEntityProcessingService postProcessor : postEntityProcessingServices) {
            postProcessor.process(gameData, world);
        }
    }

    private void applyColor(Polygon polygon, Entity entity) {
        if (entity.getColor() != null) {
            polygon.setFill(Color.web(entity.getColor()));
            polygon.setStroke(Color.web(entity.getColor()).darker());
            polygon.setStrokeWidth(2);
        }
    }

    private void draw() {
        scoreText.setText("Score: " + collisionEventHandler.getScore());
        for (Entity entity : polygons.keySet()) {
            if (!world.getEntities().contains(entity)) {
                Polygon removedPolygon = polygons.get(entity);
                polygons.remove(entity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                applyColor(polygon, entity);
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
    }
}
