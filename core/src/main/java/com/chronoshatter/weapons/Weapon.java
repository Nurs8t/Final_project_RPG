package com.chronoshatter.weapons;

import com.chronoshatter.objects.Bullet;
import java.util.Collections;
import java.util.List;

public abstract class Weapon {
    protected final String name;
    protected final float  damage;
    protected final float  cooldown;
    protected final int    price;
    private float timer;

    protected Weapon(String name, float damage, float cooldown, int price) {
        this.name = name; this.damage = damage;
        this.cooldown = cooldown; this.price = price;
        this.timer = cooldown; // готов сразу
    }

    public void update(float dt) {
        timer = Math.min(timer + dt, cooldown);
    }

    public boolean isReady() { return timer >= cooldown; }

    public final List<Bullet> fire(float fx, float fy, float tx, float ty) {
        if (!isReady()) return Collections.emptyList();
        timer = 0;
        return createBullets(fx, fy, tx, ty);
    }

    protected abstract List<Bullet> createBullets(float fx, float fy, float tx, float ty);

    public String getName()          { return name; }
    public int    getPrice()         { return price; }
    public float  getReadyFraction() { return timer / cooldown; }
}
