package com.chronoshatter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chronoshatter.states.GameStateManager;
import com.chronoshatter.states.MenuState;

public class ChronoShatterGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private GameStateManager gsm;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm   = GameStateManager.getInstance();
        gsm.pushState(new MenuState(gsm));
    }

    @Override
    public void render() {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        gsm.update(delta);
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gsm.dispose();
    }
}
