package com.chronoshatter.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private final Vector2 pos;
    private final Vector2 vel;
    private final float   damage;
    private final boolean fromPlayer;
    private boolean alive = true;
    private static final float SPEED    = 580f;
    private static final int   SIZE     = 8;
    private static final float MAX_LIFE = 2.5f;
    private float life = 0f;

    public Bullet(float fx, float fy, float tx, float ty, float damage, boolean fromPlayer) {
        this.pos        = new Vector2(fx, fy);
        this.damage     = damage;
        this.fromPlayer = fromPlayer;
        Vector2 dir = new Vector2(tx - fx, ty - fy).nor();
        this.vel = dir.scl(SPEED);
    }

    public void update(float dt) {
        pos.add(vel.x * dt, vel.y * dt);
        life += dt;
        if (life >= MAX_LIFE) alive = false;
    }

    public void killOnWall() { alive = false; }

    public boolean isAlive()      { return alive; }
    public boolean isFromPlayer() { return fromPlayer; }
    public float   getDamage()    { return damage; }
    public Vector2 getPosition()  { return pos; }

    public Rectangle getBounds() {
        return new Rectangle(pos.x - SIZE/2f, pos.y - SIZE/2f, SIZE, SIZE);
    }

    public void draw(ShapeRenderer sr) {
        sr.setColor(fromPlayer ? Color.YELLOW : new Color(1f, 0.35f, 0.35f, 1f));
        sr.circle(pos.x, pos.y, SIZE/2f);
    }
}
