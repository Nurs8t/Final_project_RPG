package com.chronoshatter.weapons;

import com.chronoshatter.objects.Bullet;
import java.util.Collections;
import java.util.List;

public class AK47 extends Weapon {
    public AK47() { super("AK-47", 20f, 0.12f, 100); } // ← цена 100
    @Override
    protected List<Bullet> createBullets(float fx, float fy, float tx, float ty) {
        return Collections.singletonList(new Bullet(fx, fy, tx, ty, damage, true));
    }
}
