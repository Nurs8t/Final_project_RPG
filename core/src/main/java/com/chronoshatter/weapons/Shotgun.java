package com.chronoshatter.weapons;

import com.chronoshatter.objects.Bullet;
import java.util.ArrayList;
import java.util.List;

public class Shotgun extends Weapon {
    public Shotgun() { super("Shotgun", 15f, 1.0f, 150); }

    @Override
    protected List<Bullet> createBullets(float fx, float fy, float tx, float ty) {
        List<Bullet> list = new ArrayList<>();
        double base   = Math.atan2(ty - fy, tx - fx);
        double spread = 0.15;
        for (int i = -2; i <= 2; i++) {
            double a = base + i * spread;
            list.add(new Bullet(fx, fy,
                fx + (float)Math.cos(a)*300,
                fy + (float)Math.sin(a)*300,
                damage, true));
        }
        return list;
    }
}
