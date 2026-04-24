package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.chronoshatter.entities.*;
import com.chronoshatter.hud.HUD;
import com.chronoshatter.map.GameMap;
import com.chronoshatter.objects.Bullet;
import com.chronoshatter.objects.Money;
import com.chronoshatter.objects.Portal;
import com.chronoshatter.systems.ChronoAbility;
import com.chronoshatter.utils.Assets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayState extends GameState {

    private final OrthographicCamera camera;
    private final Player             player;
    private final GameMap            map;
    private final HUD                hud;
    private final ChronoAbility      chrono;
    private final Portal             portal;
    private final int                currentLevel;

    private final List<Enemy>  enemies = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Money>  coins   = new ArrayList<>();

    private boolean levelComplete = false;

    public PlayState(GameStateManager gsm, Player player, int level) {
        super(gsm);
        Gdx.input.setInputProcessor(null);

        this.player       = player;
        this.currentLevel = level;
        this.map          = new GameMap(level);
        this.hud          = new HUD();
        this.chrono       = new ChronoAbility();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, map.getPixelWidth(), map.getPixelHeight());
        portal = new Portal(map.getPixelWidth() / 2f, map.getPixelHeight() / 2f + 100f);
        player.getPosition().set(800, 450);

        spawnEnemies(level);
    }

    private void spawnEnemies(int level) {
        int mel = 2 + level * 2;
    int gun  = (level >= 2) ? level * 2 : 0;
    boolean boss = (level == 3);
    for (int i = 0; i < mel; i++) {
        float[] pos = map.getValidSpawnPosition(Enemy.SIZE);
        enemies.add(new ZombieMelee(pos[0], pos[1]));
    }
    for (int i = 0; i < gun; i++) {
        float[] pos = map.getValidSpawnPosition(Enemy.SIZE);
        enemies.add(new ZombieGunner(pos[0], pos[1]));
    }
    if (boss) {
        float[] pos = map.getValidSpawnPosition(Boss.SZ);
        enemies.add(new Boss(pos[0], pos[1]));
    }
}
    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pushState(new PauseState(gsm));
            return;
        }

        chrono.update(dt);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) chrono.activate();

        float slow = chrono.getSlowMultiplier();

        List<Bullet> fired = player.update(dt, map, camera);
        bullets.addAll(fired);

        for (Enemy e : enemies) {
            e.update(dt * slow, player, map);
            if (e.getBounds().overlaps(player.getBounds()))
                e.meleeAttack(dt * slow, player);
            Bullet eb = e.tryShoot(player);
            if (eb != null) bullets.add(eb);
        }

        Iterator<Bullet> bIt = bullets.iterator();
        while (bIt.hasNext()) {
            Bullet b = bIt.next();
            b.update(b.isFromPlayer() ? dt : dt * slow);

            if (!b.isAlive()) { bIt.remove(); continue; }
            if (map.isSolid(b.getPosition().x, b.getPosition().y)) {
                b.killOnWall(); bIt.remove(); continue;
            }

            if (b.isFromPlayer()) {
                boolean hit = false;
                Iterator<Enemy> eIt = enemies.iterator();
                while (eIt.hasNext()) {
                    Enemy e = eIt.next();
                    if (b.getBounds().overlaps(e.getBounds())) {
                        e.takeDamage(b.getDamage());
                        if (e.isDead()) {
                            coins.add(new Money(e.getPosition().x, e.getPosition().y, e.getReward()));
                            Assets.sounds.playDeath();
                            eIt.remove();
                        }
                        hit = true;
                        break;
                    }
                }
                if (hit) bIt.remove();
            } else {
                if (b.getBounds().overlaps(player.getBounds())) {
                    player.takeDamage(b.getDamage());
                    Assets.sounds.playHit();
                    bIt.remove();
                }
            }
        }

        Iterator<Money> mIt = coins.iterator();
        while (mIt.hasNext()) {
            Money m = mIt.next();
            if (m.checkPickup(player.getPosition())) {
                player.addMoney(m.getAmount());
                Assets.sounds.playCoin();
                mIt.remove();
            }
        }

        if (enemies.isEmpty() && !levelComplete) {
            levelComplete = true;
            portal.activate();
        }
        if (levelComplete
            && portal.checkInteraction(player.getPosition())
            && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (currentLevel >= 3) {
                Assets.sounds.playWin();
                gsm.setState(new WinState(gsm));
            } else {
                gsm.setState(new ShopState(gsm, player, currentLevel + 1));
            }
        }

        if (player.isDead()) {
            Assets.sounds.playLose();
            gsm.setState(new GameOverState(gsm));
        }

        float camW = camera.viewportWidth;
        float camH = camera.viewportHeight;
        float mapW = map.getPixelWidth();
        float mapH = map.getPixelHeight();

        float targetX = (mapW <= camW)
            ? mapW / 2f
            : MathUtils.clamp(player.getPosition().x + Player.SIZE / 2f, camW / 2f, mapW - camW / 2f);

        float targetY = (mapH <= camH)
            ? mapH / 2f
            : MathUtils.clamp(player.getPosition().y + Player.SIZE / 2f, camH / 2f, mapH - camH / 2f);

        camera.position.set(targetX, targetY, 0);
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.14f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1. Карта
        map.render(camera);

        // 2. Спрайты врагов и игрока через SpriteBatch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Enemy e : enemies) e.draw(batch);
        player.draw(batch, camera);
        batch.end();

        // 3. HP бары врагов, монеты, пули, портал — через ShapeRenderer
        Assets.sr.setProjectionMatrix(camera.combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);
        map.renderFallback(Assets.sr);
        portal.draw(Assets.sr);
        for (Money  m : coins)   m.draw(Assets.sr);
        for (Enemy  e : enemies) e.drawHpBar(Assets.sr);
        for (Bullet b : bullets) b.draw(Assets.sr);
        Assets.sr.end();

        // 4. HUD
        hud.render(batch, player, enemies.size(), chrono, currentLevel);

        // 5. Текст по центру снизу
        if (levelComplete) {
            batch.begin();

            String msg = "All enemies defeated!   Approach the portal and press E";

            // Тёмная тень для читаемости
            Assets.font.getData().setScale(2.2f);
            Assets.font.setColor(0f, 0f, 0f, 0.75f);
            com.badlogic.gdx.graphics.g2d.GlyphLayout layout =
                new com.badlogic.gdx.graphics.g2d.GlyphLayout(Assets.font, msg);
            float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
            Assets.font.draw(batch, msg, x + 2f, 52f);

            // Основной текст — зелёный, крупнее
            Assets.font.setColor(0.15f, 1f, 0.3f, 1f);
            Assets.font.draw(batch, msg, x, 54f);

            Assets.font.getData().setScale(1.5f);
            Assets.font.setColor(1f, 1f, 1f, 1f);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
