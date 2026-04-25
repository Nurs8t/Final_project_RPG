package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState implements GameState {

    private GameStateManager gsm;

    public PlayState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void enter() { }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gsm.pushState(new PauseState(gsm));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            gsm.pushState(new BattleState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();
    }

    @Override
    public void exit() { }

    @Override
    public void dispose() { }
}
