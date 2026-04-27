package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chronoshatter.entities.Player;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.utils.UIFactory;

public class GameOverState extends GameState {

    private Stage stage;
    private Skin  skin;

    private enum Action { NONE, RETRY, MENU }
    private Action pending = Action.NONE;

    public GameOverState(GameStateManager gsm) {
        super(gsm);
        Gdx.input.setInputProcessor(null);

        skin  = UIFactory.createSkin();
        stage = new Stage(new FitViewport(1280, 720));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(stage);

        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label title = new Label("GAME OVER", skin);
        title.setFontScale(3.5f);
        title.setColor(new Color(1f, 0.2f, 0.2f, 1f));

        Label sub = new Label("You were eliminated", skin, "small");
        sub.setColor(new Color(0.8f, 0.5f, 0.5f, 1f));

        TextButton btnRetry = new TextButton("  Play Again  [R]  ", skin);
        TextButton btnMenu  = new TextButton("  Main Menu  [M]  ", skin);

        btnRetry.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.RETRY; } });
        btnMenu .addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.MENU; } });

        root.add(title).padBottom(10).row();
        root.add(sub).padBottom(70).row();
        root.add(btnRetry).width(350).height(68).padBottom(16).row();
        root.add(btnMenu).width(350).height(68);

        stage.addActor(root);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) pending = Action.RETRY;
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) pending = Action.MENU;

        switch (pending) {
            case RETRY: gsm.setState(new PlayState(gsm, new Player(), 1)); break;
            case MENU:  gsm.setState(new MenuState(gsm));                  break;
            default: break;
        }
        pending = Action.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.sr.setProjectionMatrix(stage.getCamera().combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);

        Assets.sr.setColor(new Color(0.18f, 0.02f, 0.02f, 0.72f));
        Assets.sr.rect(340, 160, 600, 400);
        Assets.sr.setColor(new Color(0.9f, 0.15f, 0.15f, 0.5f));
        Assets.sr.rect(340, 160, 3, 400);
        Assets.sr.rect(937, 160, 3, 400);

        Assets.sr.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}