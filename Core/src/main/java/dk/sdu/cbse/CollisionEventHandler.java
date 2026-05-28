package dk.sdu.cbse;

import dk.sdu.cbse.common.asteroids.Asteroid;
import dk.sdu.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.cbse.common.data.CollisionEvent;
import dk.sdu.cbse.common.data.Entity;
import org.springframework.context.event.EventListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ServiceLoader;

class CollisionEventHandler {

    private static final String SCORING_URL = "http://localhost:8080/score?point=";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private volatile long baseScore = 0;
    private volatile long currentScore = 0;

    long getScore() {
        return currentScore - baseScore;
    }

    void resetScore() {
        // Read whatever the ScoringSystem currently holds and use it as baseline score.
        // This makes the displayed score always start at 0 regardless of what
        // the ScoringSystem has accumulated across previous runs.
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/score?point=0"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            baseScore = Long.parseLong(response.body());
            currentScore = baseScore;
        } catch (Exception e) {
            System.err.println("Scoring service unavailable: " + e.getMessage());
        }
    }

    @EventListener
    public void onCollision(CollisionEvent event) {
        ServiceLoader.load(ModuleLayer.boot(), IAsteroidSplitter.class)
                .forEach(splitter -> {
                    splitter.createSplitAsteroids(event.getEntity1(), event.getWorld());
                    splitter.createSplitAsteroids(event.getEntity2(), event.getWorld());
                });

        addScore(event.getEntity1());
        addScore(event.getEntity2());
    }

    private void addScore(Entity entity) {
        if (entity instanceof Asteroid asteroid) {
            long points = (long) (asteroid.getRadius() / 2);
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(SCORING_URL + points))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                currentScore = Long.parseLong(response.body());
            } catch (Exception e) {
                System.err.println("Scoring service unavailable: " + e.getMessage());
            }
        }
    }
}
