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
import com.chronoshatter.utils.Assets;
import com.chronoshatter.utils.UIFactory;

public class SettingsState extends GameState {

    private Stage  stage;
    private Skin   skin;
    private TextButton btnSound;

    private enum Action { NONE, TOGGLE_SOUND, BACK }
    private Action pending = Action.NONE;

    public SettingsState(GameStateManager gsm) {
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

        Label title = new Label("SETTINGS", skin);
        title.setFontScale(2.4f);
        title.setColor(new Color(0.35f, 0.9f, 1f, 1f));

        btnSound = new TextButton(soundLabel(), skin);
        btnSound.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { pending = Action.TOGGLE_SOUND; }
        });

        Table ctrl = new Table();
        ctrl.defaults().padTop(6).padBottom(6);

        String[][] rows = {
            {"W  A  S  D",      "Move"},
            {"Left Mouse",      "Shoot toward cursor"},
            {"SHIFT",           "Chrono — slows enemies x3 for 5 s"},
            {"E",               "Enter portal  (all enemies must be dead)"},
            {"ESC",             "Pause game"},
        };

        for (String[] row : rows) {
            Label key = new Label(row[0], skin, "small");
            key.setColor(new Color(0.45f, 0.95f, 1f, 1f));

            Label sep = new Label("—", skin, "small");
            sep.setColor(new Color(0.4f, 0.45f, 0.55f, 1f));

            Label act = new Label(row[1], skin, "small");
            act.setColor(new Color(0.88f, 0.9f, 1f, 1f));

            ctrl.add(key).right().padRight(14);
            ctrl.add(sep).padRight(14);
            ctrl.add(act).left().row();
        }

        TextButton btnBack = new TextButton("   Back  [B]  ", skin);
        btnBack.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { pending = Action.BACK; }
        });

        root.add(title).padBottom(36).row();
        root.add(btnSound).width(350).height(64).padBottom(36).row();

        Label ctrlHeader = new Label("Controls", skin, "small");
        ctrlHeader.setColor(new Color(0.6f, 0.7f, 1f, 1f));
        root.add(ctrlHeader).padBottom(10).row();
        root.add(ctrl).padBottom(40).row();
        root.add(btnBack).width(350).height(64);

        stage.addActor(root);
    }

    private String soundLabel() {
        return "   Sound:  " + (Assets.soundEnabled ? "ON " : "OFF") + "   [S]  ";
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.S))      pending = Action.TOGGLE_SOUND;
        if (Gdx.input.isKeyJustPressed(Input.Keys.B) ||
            Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pending = Action.BACK;

        switch (pending) {
            case TOGGLE_SOUND:
                Assets.sounds.setEnabled(!Assets.soundEnabled);
                btnSound.setText(soundLabel());
                break;
            case BACK:
                gsm.setState(new MenuState(gsm));
                break;
            default: break;
        }
        pending = Action.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.sr.setProjectionMatrix(stage.getCamera().combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);

        Assets.sr.setColor(new Color(0.04f, 0.06f, 0.17f, 0.7f));
        Assets.sr.rect(230, 70, 820, 580);
        Assets.sr.setColor(new Color(0.3f, 0.8f, 1f, 0.5f));
        Assets.sr.rect(230, 70, 3, 580);
        Assets.sr.rect(1047, 70, 3, 580);

        Assets.sr.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        // НЕ вызываем setInputProcessor(null) — это затёрло бы processor
        // нового стейта, который уже установлен к моменту вызова dispose().
        stage.dispose();
        skin.dispose();
    }
}