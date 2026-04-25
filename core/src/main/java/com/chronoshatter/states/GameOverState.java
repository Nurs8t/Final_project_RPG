package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverState implements GameState {

    private GameStateManager gsm;
    private float timer    = 0f;
    private boolean canInput = false;

    public GameOverState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void enter() {
        timer    = 0f;
        canInput = false;
    }

    @Override
    public void update(float delta) {
        timer += delta;
        if (timer >= 1f) canInput = true;

        if (canInput) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gsm.setState(new PlayState(gsm));
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                gsm.setState(new MenuState(gsm));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.1f, 0.02f, 0.02f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();
    }

    @Override
    public void exit() { }

    @Override
    public void dispose() { }
}
