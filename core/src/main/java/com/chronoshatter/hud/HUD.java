package com.chronoshatter.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.chronoshatter.entities.Player;
import com.chronoshatter.systems.ChronoAbility;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.weapons.AK47;
import com.chronoshatter.weapons.Shotgun;

public class HUD {
    private final OrthographicCamera hudCam;
    private final GlyphLayout        layout = new GlyphLayout();

    public HUD() {
        hudCam = new OrthographicCamera();
    }

    public void render(SpriteBatch batch, Player player, int enemiesLeft,
                       ChronoAbility chrono, int level) {

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        hudCam.setToOrtho(false, sw, sh);
        hudCam.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer sr = Assets.sr;
        sr.setProjectionMatrix(hudCam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // ════════════════════════════════════════════════════════════════
        //  ЛЕВАЯ ПАНЕЛЬ  (300 × 130 px)
        //  Шкалы занимают всю ширину панели; текст рисуется ВНУТРИ шкал
        // ════════════════════════════════════════════════════════════════
        float LP_W = 300f, LP_H = 130f;
        float barX = 8f,   barW = LP_W - barX * 2;   // 284 px — шкала почти во всю панель

        sr.setColor(0f, 0f, 0f, 0.72f);
        sr.rect(0, sh - LP_H, LP_W, LP_H);
        // нижняя акцентная линия
        sr.setColor(0.25f, 0.45f, 0.85f, 0.6f);
        sr.rect(0, sh - LP_H, LP_W, 2f);

        // ── HP bar  (10 → 36 px от верха, высота 26 px) ─────────────────
        float hpBot = sh - 36f, hpH = 26f;
        sr.setColor(0.28f, 0f, 0f, 1f);
        sr.rect(barX, hpBot, barW, hpH);
        float hpFill = player.getHp() / player.getMaxHp();
        sr.setColor(0.88f, 0.13f, 0.13f, 1f);
        sr.rect(barX, hpBot, barW * hpFill, hpH);
        // блик
        sr.setColor(1f, 0.42f, 0.42f, 0.28f);
        sr.rect(barX, hpBot + hpH - 5f, barW * hpFill, 5f);

        // ── Armor bar  (54 → 74 px, высота 20 px, отступ 18 px) ─────────
        float armBot = sh - 74f, armH = 20f;
        sr.setColor(0f, 0f, 0.32f, 1f);
        sr.rect(barX, armBot, barW, armH);
        float armFill = player.getMaxArmor() > 0 ? player.getArmor() / player.getMaxArmor() : 0f;
        sr.setColor(0.28f, 0.48f, 0.92f, 1f);
        sr.rect(barX, armBot, barW * armFill, armH);
        sr.setColor(0.55f, 0.75f, 1f, 0.28f);
        sr.rect(barX, armBot + armH - 4f, barW * armFill, 4f);

        // ── Chrono bar  (92 → 106 px, высота 14 px, отступ 18 px) ───────
        float chrBot = sh - 106f, chrH = 14f;
        sr.setColor(0.15f, 0.13f, 0f, 1f);
        sr.rect(barX, chrBot, barW, chrH);
        float cf = chrono.getReadyFraction();
        if      (chrono.isActive()) sr.setColor(1f, 0.95f, 0.15f, 1f);
        else if (chrono.isReady())  sr.setColor(0.75f, 0.75f, 0.12f, 1f);
        else                        sr.setColor(0.42f, 0.42f, 0.08f, 1f);
        sr.rect(barX, chrBot, barW * cf, chrH);

        // ════════════════════════════════════════════════════════════════
        //  ПРАВАЯ ПАНЕЛЬ — чёрный фон только под текст
        //  Иконка оружия той же высоты, торчит ЛЕВЕЕ чёрного фона
        // ════════════════════════════════════════════════════════════════
        float RP_W = 178f;   // ширина чёрного фона (только текст)
        float RP_H = 130f;   // высота чёрного фона
        float rpX  = sw - RP_W;

        sr.setColor(0f, 0f, 0f, 0.72f);
        sr.rect(rpX, sh - RP_H, RP_W, RP_H);
        // левая золотая линия панели
        sr.setColor(Color.GOLD);
        sr.rect(rpX, sh - RP_H, 2f, RP_H);
        // нижняя акцентная линия
        sr.setColor(0.25f, 0.45f, 0.85f, 0.6f);
        sr.rect(rpX, sh - RP_H, RP_W, 2f);

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // ════════════════════════════════════════════════════════════════
        //  СПРАЙТЫ + ТЕКСТ
        // ════════════════════════════════════════════════════════════════
        batch.setProjectionMatrix(hudCam.combined);
        batch.begin();

        // ── Текст ВНУТРИ шкал (по центру каждой шкалы) ──────────────────
        Assets.font.getData().setScale(1.05f);

        // HP текст: белая надпись поверх красной шкалы
        String hpStr = "HP   " + (int)player.getHp() + " / " + (int)player.getMaxHp();
        layout.setText(Assets.font, hpStr);
        float hpTxtX = barX + (barW - layout.width) / 2f;
        float hpTxtY = hpBot + hpH / 2f + layout.height / 2f;
        // тёмная тень для читаемости
        Assets.font.setColor(0f, 0f, 0f, 0.7f);
        Assets.font.draw(batch, hpStr, hpTxtX + 1f, hpTxtY - 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, hpStr, hpTxtX, hpTxtY);

        // Armor текст
        String armStr = "ARM   " + (int)player.getArmor() + " / " + (int)player.getMaxArmor();
        layout.setText(Assets.font, armStr);
        float armTxtX = barX + (barW - layout.width) / 2f;
        float armTxtY = armBot + armH / 2f + layout.height / 2f;
        Assets.font.setColor(0f, 0f, 0f, 0.7f);
        Assets.font.draw(batch, armStr, armTxtX + 1f, armTxtY - 1f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, armStr, armTxtX, armTxtY);

        // Chrono статус (под шкалой, внутри панели)
        Assets.font.getData().setScale(1.0f);
        if (chrono.isActive()) {
            Assets.font.setColor(Color.YELLOW);
            Assets.font.draw(batch, "CHRONO ACTIVE!", barX, chrBot - 5f);
        } else if (!chrono.isReady()) {
            Assets.font.setColor(new Color(0.55f, 0.55f, 0.55f, 1f));
            Assets.font.draw(batch, "SHIFT  recharging...", barX, chrBot - 5f);
        } else {
            Assets.font.setColor(new Color(0.88f, 0.88f, 0.22f, 1f));
            Assets.font.draw(batch, "SHIFT  Chrono ready", barX, chrBot - 5f);
        }

        // ── Иконка оружия — та же высота что RP_H, левее чёрного фона ───
        Texture wIcon;
        if (player.getWeapon() instanceof AK47)         wIcon = Assets.iconAK47;
        else if (player.getWeapon() instanceof Shotgun) wIcon = Assets.iconShotgun;
        else                                            wIcon = Assets.iconPistol;

        if (wIcon != null) {
            // Высота иконки = высота чёрного фона (RP_H)
            float iH    = RP_H;
            float ratio = (float) wIcon.getWidth() / wIcon.getHeight();
            float iW    = iH * ratio;   // для 2:1 → iW = 260px

            // Иконка расположена ЛЕВЕЕ rpX (левого края чёрного фона)
            float iX = rpX - iW;
            float iY = sh - RP_H;   // совпадает с верхом/низом панели

            batch.draw(wIcon, iX, iY, iW, iH);
        }

        // ── Текст внутри чёрного фона (правая панель) ───────────────────
        float txtX = rpX + 8f;
        Assets.font.getData().setScale(1.2f);
        Assets.font.setColor(Color.WHITE);
        Assets.font.draw(batch, "Level: "   + level,        txtX, sh - 14f);
        Assets.font.draw(batch, "Enemies: " + enemiesLeft,  txtX, sh - 40f);
        Assets.font.draw(batch, player.getWeapon().getName(), txtX, sh - 66f);
        Assets.font.setColor(Color.GOLD);
        Assets.font.draw(batch, "Coins: " + player.getMoney(), txtX, sh - 92f);
        Assets.font.setColor(new Color(0.42f, 0.45f, 0.6f, 1f));
        Assets.font.draw(batch, "ESC - Pause", txtX, sh - 118f);

        // сброс
        Assets.font.getData().setScale(1.5f);
        Assets.font.setColor(Color.WHITE);
        batch.end();
    }
}