package com.chronoshatter.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Money {
    private final Vector2 pos;
    private final int amount;
    private static final float SIZE = 14f;

    public Money(float x, float y, int amount) {
        this.pos    = new Vector2(x + 10, y + 10);
        this.amount = amount;
    }

    public boolean checkPickup(Vector2 playerPos) {
        return playerPos.dst(pos) < 30f;
    }

    public int getAmount() { return amount; }

    public void draw(ShapeRenderer sr) {
        sr.setColor(Color.GOLD);
        sr.circle(pos.x, pos.y, SIZE/2f);
        sr.setColor(new Color(0.7f, 0.6f, 0f, 1f));
        sr.circle(pos.x, pos.y, SIZE/2f - 2);
    }
}
