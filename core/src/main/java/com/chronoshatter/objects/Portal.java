package com.chronoshatter.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Portal {
    private final Vector2 pos;
    private boolean active = false;
    private static final float RADIUS = 28f;

    public Portal(float x, float y) {
        this.pos = new Vector2(x, y);
    }

    public void activate() { active = true; }
    public boolean isActive() { return active; }

    public boolean checkInteraction(Vector2 playerPos) {
        return active && playerPos.dst(pos) < 50f;
    }

    public Vector2 getPosition() { return pos; }

    public void draw(ShapeRenderer sr) {
        sr.setColor(active ? Color.CYAN : new Color(0.3f, 0.3f, 0.3f, 1f));
        sr.circle(pos.x, pos.y, RADIUS);
        sr.setColor(active ? new Color(0f, 0.5f, 0.7f, 1f) : new Color(0.2f, 0.2f, 0.2f, 1f));
        sr.circle(pos.x, pos.y, RADIUS - 6);
        if (active) {
            sr.setColor(Color.WHITE);
            sr.circle(pos.x, pos.y, 5);
        }
    }
}
