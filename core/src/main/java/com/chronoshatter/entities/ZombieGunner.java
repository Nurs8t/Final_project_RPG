package com.chronoshatter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.chronoshatter.map.GameMap;
import com.chronoshatter.objects.Bullet;
import com.chronoshatter.utils.Assets;

public class ZombieGunner extends Enemy {
    private float shootTimer  = 0f;
    private static final float SHOOT_CD    = 2f;
    private static final float SHOOT_RANGE = 280f;

    public ZombieGunner(float x, float y) {
        super(x, y, 35f, 0f, 15, 60f);
        meleeCooldown = 999f;
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
            return new Bullet(pos.x + SIZE/2, pos.y + SIZE/2,
                              player.getPosition().x, player.getPosition().y,
                              10f, false);
        }
        return null;
    }

    @Override
    protected Texture getTexture() { return Assets.zombieGunner; }
}