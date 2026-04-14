package com.chronoshatter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;   // ← ЭТОГО НЕ БЫЛО
import com.chronoshatter.map.GameMap;
import com.chronoshatter.objects.Bullet;
import com.chronoshatter.utils.Assets;

public class Boss extends Enemy {
    public  static final float SZ         = 52f;
    private static final float SHOOT_RANGE= 400f;
    private static final float SHOOT_CD   = 1.2f;
    private float shootTimer = 0f;

    public Boss(float x, float y) {
        super(x, y, 250f, 30f, 100, 45f);
        meleeCooldown = 0.8f;
    }

    @Override
    public void update(float dt, Player player, GameMap map) {
        super.update(dt, player, map);
        shootTimer += dt;
    }

    @Override
    public Bullet tryShoot(Player player) {
        float dx = player.getPosition().x - pos.x;
        float dy = player.getPosition().y - pos.y;
        if ((float) Math.sqrt(dx*dx + dy*dy) < SHOOT_RANGE && shootTimer >= SHOOT_CD) {
            shootTimer = 0;
            return new Bullet(pos.x + SZ/2, pos.y + SZ/2,
                              player.getPosition().x, player.getPosition().y,
                              15f, false);
        }
        return null;
    }

    @Override protected Texture getTexture()   { return Assets.zombieBoss; }
    @Override protected float   getDrawSize()  { return SZ; }

    // Boss имеет бо́льший HP-бар
    @Override
    public void drawHpBar(ShapeRenderer sr) {
        sr.setColor(0.35f, 0f, 0f, 1f);
        sr.rect(pos.x, pos.y + SZ + 4, SZ, 7);
        sr.setColor(0.1f, 0.85f, 0.1f, 1f);
        sr.rect(pos.x, pos.y + SZ + 4, SZ * (hp / maxHp), 7);
    }
}