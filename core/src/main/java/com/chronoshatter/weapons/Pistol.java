package com.chronoshatter.weapons;

import com.chronoshatter.objects.Bullet;
import java.util.Collections;
import java.util.List;

public class Pistol extends Weapon {
    public Pistol() { super("Pistol", 15f, 0.45f, 0); }

    @Override
    protected List<Bullet> createBullets(float fx, float fy, float tx, float ty) {
        return Collections.singletonList(new Bullet(fx, fy, tx, ty, damage, true));
    }
}
