package com.chronoshatter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private Sound shoot, hit, death, coin, buy, win, lose;
    private Music backgroundMusic;

    public SoundManager() {
        shoot = load("sounds/shoot.mp3");       // ← .mp3
        hit   = load("sounds/hit.wav");
        death = load("sounds/zombie_death.wav");
        coin  = load("sounds/coin_pickup.wav");
        buy   = load("sounds/buy.wav");
        win   = load("sounds/win.wav");
        lose  = load("sounds/lose.wav");

        // Фоновая музыка
        try {
            backgroundMusic = Gdx.audio.newMusic(
                Gdx.files.internal("sounds/background.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.4f);
            backgroundMusic.play();
        } catch (Exception e) {
            System.out.println("background.mp3 not found");
        }
    }

    private Sound load(String path) {
        try { return Gdx.audio.newSound(Gdx.files.internal(path)); }
        catch (Exception e) { return null; }
    }

    private void play(Sound s) {
        if (Assets.soundEnabled && s != null) s.play(0.8f);
    }

    public void setEnabled(boolean on) {
        Assets.soundEnabled = on;
        if (backgroundMusic != null) {
            if (on) backgroundMusic.play();
            else    backgroundMusic.pause();
        }
    }

    public void playShoot() { play(shoot); }
    public void playHit()   { play(hit); }
    public void playDeath() { play(death); }
    public void playCoin()  { play(coin); }
    public void playBuy()   { play(buy); }
    public void playWin()   { play(win); }
    public void playLose()  { play(lose); }

    public void dispose() {
        Sound[] all = {shoot, hit, death, coin, buy, win, lose};
        for (Sound s : all) if (s != null) s.dispose();
        if (backgroundMusic != null) backgroundMusic.dispose();
    }
}