package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseState implements GameState {

    private GameStateManager gsm;
    private float alpha = 0f;

    public PauseState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void enter() {
        alpha = 0f;
    }

    @Override
    public void update(float delta) {
        if (alpha < 0.6f) alpha += delta * 2f;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.popState();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gsm.setState(new MenuState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        batch.begin();
        batch.end();
    }

    @Override
    public void exit() { }

    @Override
    public void dispose() { }
}
