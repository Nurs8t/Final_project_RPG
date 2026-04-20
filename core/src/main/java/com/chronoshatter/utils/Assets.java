package com.chronoshatter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Assets {
    public static BitmapFont  font;
    public static ShapeRenderer sr;
    public static SoundManager  sounds;
    public static boolean soundEnabled = true;

    // Спрайты игрока
    public static Texture playerPistol;
    public static Texture playerAK;
    public static Texture playerSG;

    // Иконки HUD
    public static Texture iconHeart;
    public static Texture iconArmor;   // ← добавлено
    public static Texture iconPistol;
    public static Texture iconAK47;
    public static Texture iconShotgun;

    // Спрайты врагов
    public static Texture zombieMelee;
    public static Texture zombieGunner;
    public static Texture zombieBoss;

    public static void load() {
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        sr     = new ShapeRenderer();
        sounds = new SoundManager();

        playerPistol  = tex("player_pistol.png");
        playerAK      = tex("player_ak47.png");
        playerSG      = tex("player_shotgun.png");
        zombieMelee   = tex("zombie_melee.png");
        zombieGunner  = tex("zombie_gunner.png");
        zombieBoss    = tex("zombie_boss.png");
        iconHeart     = tex("icon_heart.png");
        iconArmor     = tex("icon_armor.png");   // ← добавлено
        iconPistol    = tex("icon_pistol.png");
        iconAK47      = tex("icon_ak47.png");
        iconShotgun   = tex("icon_shotgun.png");
    }

    /** Безопасная загрузка — возвращает null если файл не найден */
    private static Texture tex(String path) {
        try {
            return new Texture(Gdx.files.internal(path));
        } catch (Exception e) {
            System.err.println("Missing texture: " + path);
            return null;
        }
    }

    public static void dispose() {
        if (font   != null) font.dispose();
        if (sr     != null) sr.dispose();
        if (sounds != null) sounds.dispose();

        Texture[] all = {
            playerPistol, playerAK, playerSG,
            iconHeart, iconArmor, iconPistol, iconAK47, iconShotgun,
            zombieMelee, zombieGunner, zombieBoss
        };
        for (Texture t : all) if (t != null) t.dispose();
    }
}