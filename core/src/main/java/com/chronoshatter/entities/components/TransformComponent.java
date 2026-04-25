package com.chronoshatter.entities.components;

public class TransformComponent implements Component {

    public float x, y;
    public float rotation;
    public float scaleX, scaleY;

    public TransformComponent(float x, float y) {
        this.x      = x;
        this.y      = y;
        this.rotation = 0f;
        this.scaleX = 1f;
        this.scaleY = 1f;
    }
}
