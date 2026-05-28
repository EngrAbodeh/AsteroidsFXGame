package dk.sdu.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {
    // Unique identifier it tells entities apart from each other
    private final UUID ID = UUID.randomUUID();
    // The shape of the entity as x,y coordinate pairs, will be used by the renderer
    private double[] polygonCoordinates;
    // Current horizontal position on screen
    private double x;
    // Current vertical position on screen

    private double y;
    // Direction the entity is facing in degrees will be used to calculate movement

    private double rotation;


    private double radius;
    private String color;
    private EntityType type;

    // Returns the unique ID as a String

    public String getID(){
        return ID.toString();
    }

    public void setPolygonCoordinates(double... coordinates) {
        this.polygonCoordinates = coordinates;
    }


    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getX() {
        return x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getY() {
        return y;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
    public double getRadius() {
        return this.radius;
    }

    public void setColor(String color) { this.color = color; }
    public String getColor() { return color; }

    public void setType(EntityType type) { this.type = type; }
    public EntityType getType() { return type; }
}