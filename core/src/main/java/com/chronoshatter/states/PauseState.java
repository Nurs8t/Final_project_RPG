package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.utils.UIFactory;

public class PauseState extends GameState {

    private Stage  stage;
    private Skin   skin;
    private TextButton btnSound;

    private enum Action { NONE, RESUME, TOGGLE_SOUND, MENU }
    private Action pending = Action.NONE;

    public PauseState(GameStateManager gsm) {
        super(gsm);

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

        Label title = new Label("PAUSED", skin);
        title.setFontScale(3.2f);
        title.setColor(Color.WHITE);

        TextButton btnResume = new TextButton("   Resume  [ESC]  ", skin);
        btnSound             = new TextButton(soundLabel(), skin);
        TextButton btnMenu   = new TextButton("  Main Menu  [M]  ", skin);

        btnResume.addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.RESUME; } });
        btnSound .addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.TOGGLE_SOUND; } });
        btnMenu  .addListener(new ClickListener() { @Override public void clicked(InputEvent e, float x, float y) { pending = Action.MENU; } });

        root.add(title).padBottom(55).row();
        root.add(btnResume).width(350).height(68).padBottom(16).row();
        root.add(btnSound).width(350).height(68).padBottom(16).row();
        root.add(btnMenu).width(350).height(68);

        stage.addActor(root);
    }

    private String soundLabel() {
        return "   Sound:  " + (Assets.soundEnabled ? "ON " : "OFF") + "  [S]  ";
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.P))      pending = Action.RESUME;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S))      pending = Action.TOGGLE_SOUND;
        if (Gdx.input.isKeyJustPressed(Input.Keys.M))      pending = Action.MENU;

        switch (pending) {
            case RESUME:
                gsm.popState();
                break;
            case TOGGLE_SOUND:
                Assets.sounds.setEnabled(!Assets.soundEnabled);
                btnSound.setText(soundLabel());
                break;
            case MENU:
                gsm.popState();
                gsm.setState(new MenuState(gsm));
                break;
            default: break;
        }
        pending = Action.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Assets.sr.setProjectionMatrix(stage.getCamera().combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);

        Assets.sr.setColor(0f, 0f, 0f, 0.6f);
        Assets.sr.rect(0, 0, 1280, 720);

        Assets.sr.setColor(new Color(0.05f, 0.08f, 0.2f, 0.88f));
        Assets.sr.rect(380, 190, 520, 340);
        Assets.sr.setColor(new Color(0.45f, 0.65f, 1f, 0.5f));
        Assets.sr.rect(380, 190, 2, 340);
        Assets.sr.rect(898, 190, 2, 340);

        Assets.sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}