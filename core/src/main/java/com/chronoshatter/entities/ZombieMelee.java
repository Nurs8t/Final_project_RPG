package com.chronoshatter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.chronoshatter.utils.Assets;

public class ZombieMelee extends Enemy {

    public ZombieMelee(float x, float y) {
        super(x, y, 40f, 5f, 10, 80f);
        meleeCooldown = 1f;
    }

    @Override
    protected Texture getTexture() { return Assets.zombieMelee; }
}