package com.chronoshatter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chronoshatter.states.GameStateManager;
import com.chronoshatter.states.MenuState;
import com.chronoshatter.utils.Assets;

public class ChronoShatterGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private GameStateManager gsm;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Assets.load();
        gsm = new GameStateManager();
        gsm.pushState(new MenuState(gsm));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        gsm.dispose();
        Assets.dispose();
    }
}
