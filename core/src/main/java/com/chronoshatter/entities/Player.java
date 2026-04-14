package com.chronoshatter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.chronoshatter.map.GameMap;
import com.chronoshatter.objects.Bullet;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.weapons.AK47;
import com.chronoshatter.weapons.Pistol;
import com.chronoshatter.weapons.Shotgun;
import com.chronoshatter.weapons.Weapon;
import java.util.Collections;
import java.util.List;

public class Player {
    private final Vector2 pos;
    public static final float SIZE   = 36f;
    private static final float SPEED = 280f;

    private float hp = 100f, maxHp = 100f;
    private float armor = 0f, maxArmor = 50f;
    private int money = 50;
    private Weapon weapon;

    public Player() {
        pos    = new Vector2(100, 100);
        weapon = new Pistol();
    }

    public List<Bullet> update(float dt, GameMap map, OrthographicCamera cam) {
        weapon.update(dt);

        float dx = 0, dy = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += SPEED * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= SPEED * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= SPEED * dt;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += SPEED * dt;

        if (dx != 0 && !map.isSolidRect(pos.x + dx, pos.y, SIZE, SIZE)) pos.x += dx;
        if (dy != 0 && !map.isSolidRect(pos.x, pos.y + dy, SIZE, SIZE)) pos.y += dy;

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            cam.unproject(m);
            List<Bullet> fired = weapon.fire(pos.x + SIZE/2, pos.y + SIZE/2, m.x, m.y);
            if (!fired.isEmpty()) Assets.sounds.playShoot();
            return fired;
        }
        return Collections.emptyList();
    }

    public void takeDamage(float dmg) {
        if (armor > 0) {
            float abs = Math.min(armor, dmg);
            armor -= abs; dmg -= abs;
        }
        hp -= dmg; if (hp < 0) hp = 0;
    }

    public void heal(float a)        { hp = Math.min(hp + a, maxHp); }
    public void addArmor(float a)    { armor = Math.min(armor + a, maxArmor); }
    public void addMoney(int a)      { money += a; }
    public boolean spendMoney(int a) { if (money < a) return false; money -= a; return true; }
    public void setWeapon(Weapon w)  { weapon = w; }
    public Weapon getWeapon()        { return weapon; }
    public boolean isDead()          { return hp <= 0; }

    public float   getHp()       { return hp; }
    public float   getMaxHp()    { return maxHp; }
    public float   getArmor()    { return armor; }
    public float   getMaxArmor() { return maxArmor; }
    public int     getMoney()    { return money; }
    public Vector2 getPosition() { return pos; }

    public Rectangle getBounds() {
        return new Rectangle(pos.x, pos.y, SIZE, SIZE);
    }

    public void draw(SpriteBatch batch, OrthographicCamera cam) {
        // Выбираем спрайт по текущему оружию
        com.badlogic.gdx.graphics.Texture tex;
        if (weapon instanceof AK47)         tex = Assets.playerAK;
        else if (weapon instanceof Shotgun) tex = Assets.playerSG;
        else                                tex = Assets.playerPistol;

        // Поворачиваем игрока в сторону мыши
        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(m);
        float cx = pos.x + SIZE / 2f;
        float cy = pos.y + SIZE / 2f;
        float angle = (float) Math.toDegrees(Math.atan2(m.y - cy, m.x - cx));

        batch.draw(
            tex,
            pos.x, pos.y,       // позиция
            SIZE / 2f, SIZE / 2f, // точка вращения (центр)
            SIZE, SIZE,          // размер
            1f, 1f,              // масштаб
            angle,               // угол поворота
            0, 0,                // srcX, srcY
            tex.getWidth(), tex.getHeight(), // srcW, srcH
            false, false         // flipX, flipY
        );
    }
}