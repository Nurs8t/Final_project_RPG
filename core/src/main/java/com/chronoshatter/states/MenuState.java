package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chronoshatter.entities.Player;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.utils.UIFactory;

public class MenuState extends GameState {

    private Stage stage;
    private Skin  skin;

    private enum Action { NONE, NEW_GAME, SETTINGS, EXIT }
    private Action pending = Action.NONE;

    public MenuState(GameStateManager gsm) {
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

        Label title = new Label("CHRONO SHATTER", skin);
        title.setFontScale(2.6f);
        title.setColor(new Color(0.35f, 0.9f, 1f, 1f));

        Label sub = new Label("Top-Down Shooter", skin, "small");
        sub.setColor(new Color(0.55f, 0.72f, 1f, 1f));

        TextButton btnNew  = new TextButton("   New Game  [N]  ", skin);
        TextButton btnSett = new TextButton("   Settings  [S]  ", skin);
        TextButton btnExit = new TextButton("     Exit   [ESC] ", skin);

        btnNew .addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.NEW_GAME; } });
        btnSett.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.SETTINGS; } });
        btnExit.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.EXIT; } });

        Label hint = new Label("WASD - Move  |  LMB - Shoot  |  SHIFT - Chrono  |  E - Portal  |  ESC - Pause", skin, "small");
        hint.setColor(new Color(0.4f, 0.45f, 0.6f, 1f));

        root.add(title).padBottom(6).row();
        root.add(sub).padBottom(75).row();
        root.add(btnNew).width(350).height(68).padBottom(16).row();
        root.add(btnSett).width(350).height(68).padBottom(16).row();
        root.add(btnExit).width(350).height(68).padBottom(75).row();
        root.add(hint).row();

        stage.addActor(root);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.N))      pending = Action.NEW_GAME;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S))      pending = Action.SETTINGS;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pending = Action.EXIT;

        switch (pending) {
            case NEW_GAME: gsm.setState(new PlayState(gsm, new Player(), 1)); break;
            case SETTINGS: gsm.setState(new SettingsState(gsm));              break;
            case EXIT:     Gdx.app.exit();                                    break;
            default: break;
        }
        pending = Action.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.sr.setProjectionMatrix(stage.getCamera().combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);

        Assets.sr.setColor(new Color(0.05f, 0.07f, 0.18f, 0.65f));
        Assets.sr.rect(340, 175, 600, 370);
        Assets.sr.setColor(new Color(0.3f, 0.8f, 1f, 0.55f));
        Assets.sr.rect(340, 175, 3, 370);
        Assets.sr.rect(937, 175, 3, 370);
        Assets.sr.setColor(new Color(0.15f, 0.5f, 0.8f, 0.35f));
        Assets.sr.rect(0, 717, 1280, 3);

        Assets.sr.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}