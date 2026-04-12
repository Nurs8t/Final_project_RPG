package com.chronoshatter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.chronoshatter.map.GameMap;
import com.chronoshatter.objects.Bullet;   // ← ЭТОГО НЕ БЫЛО

public abstract class Enemy {
    protected Vector2 pos;
    protected float hp, maxHp;
    protected float meleeDmg;
    protected int   reward;
    protected float speed;
    protected float meleeTimer    = 0f;
    protected float meleeCooldown = 1f;
    protected float facing        = 0f; // угол поворота к игроку
    public static final float SIZE = 32f;

    protected Enemy(float x, float y, float hp, float meleeDmg, int reward, float speed) {
        this.pos = new Vector2(x, y);
        this.hp  = this.maxHp = hp;
        this.meleeDmg = meleeDmg;
        this.reward   = reward;
        this.speed    = speed;
    }

    public void update(float dt, Player player, GameMap map) {
        // Угол поворота к игроку
        float dx = player.getPosition().x + Player.SIZE / 2f - (pos.x + SIZE / 2f);
        float dy = player.getPosition().y + Player.SIZE / 2f - (pos.y + SIZE / 2f);
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist > 1f) {
            facing = (float) Math.toDegrees(Math.atan2(dy, dx));
        }

        // Движение к игроку
        if (dist > SIZE) {
            float nx = dx / dist * speed * dt;
            float ny = dy / dist * speed * dt;
            if (!map.isSolidRect(pos.x + nx, pos.y, SIZE, SIZE)) pos.x += nx;
            if (!map.isSolidRect(pos.x, pos.y + ny, SIZE, SIZE)) pos.y += ny;
        }
    }

    public void meleeAttack(float dt, Player player) {
        meleeTimer += dt;
        if (meleeTimer >= meleeCooldown) {
            player.takeDamage(meleeDmg);
            meleeTimer = 0;
        }
    }

    public void takeDamage(float dmg) { hp -= dmg; if (hp < 0) hp = 0; }
    public boolean isDead()           { return hp <= 0; }
    public Vector2 getPosition()      { return pos; }
    public int     getReward()        { return reward; }

    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, SIZE, SIZE);
    }

    /** Текстура конкретного вида врага */
    protected abstract Texture getTexture();

    /** Размер отрисовки (Boss переопределяет) */
    protected float getDrawSize() { return SIZE; }

    /** Рисует спрайт врага через SpriteBatch — исправленный метод */
    public void draw(SpriteBatch batch) {
        Texture tex = getTexture();
        float   sz  = getDrawSize();
        if (tex == null) return;
        batch.draw(
            tex,
            pos.x, pos.y,
            sz / 2f, sz / 2f,
            sz, sz,
            1f, 1f,
            facing,
            0, 0, tex.getWidth(), tex.getHeight(),
            false, false
        );
    }

    /** HP-бар — теперь PUBLIC, вызывается из PlayState */
    public void drawHpBar(ShapeRenderer sr) {
        float sz = getDrawSize();
        sr.setColor(0.35f, 0f, 0f, 1f);
        sr.rect(pos.x, pos.y + sz + 4, sz, 5);
        sr.setColor(0.1f, 0.85f, 0.1f, 1f);
        sr.rect(pos.x, pos.y + sz + 4, sz * (hp / maxHp), 5);
    }

    public Bullet tryShoot(Player player) { return null; }
}