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
import com.chronoshatter.systems.ShopSystem;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.utils.UIFactory;

public class ShopState extends GameState {

    private Stage  stage;
    private Skin   skin;
    private Label  moneyLabel, feedbackLabel;
    private float  feedbackTimer = 0f;

    private final Player     player;
    private final int        nextLevel;
    private final ShopSystem shop;

    private enum Action { NONE, BUY_MED, BUY_ARMOR, BUY_AK, BUY_SG, START }
    private Action pending = Action.NONE;

    public ShopState(GameStateManager gsm, Player player, int nextLevel) {
        super(gsm);
        Gdx.input.setInputProcessor(null);

        this.player    = player;
        this.nextLevel = nextLevel;
        this.shop      = new ShopSystem(player);

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

        Label title = new Label("SHOP  —  Level " + nextLevel, skin);
        title.setFontScale(2.2f);
        title.setColor(new Color(0.35f, 0.9f, 1f, 1f));

        moneyLabel = new Label("Coins:  " + player.getMoney(), skin);
        moneyLabel.setFontScale(1.8f);
        moneyLabel.setColor(Color.GOLD);

        Table items = new Table();
        items.defaults().padTop(6).padBottom(6);

        items.add(mkLabel("[Key]",  new Color(0.45f,0.5f,0.65f,1f))).width(80).center();
        items.add(mkLabel("Item",   new Color(0.45f,0.5f,0.65f,1f))).width(360).left().padLeft(16);
        items.add(mkLabel("Price",  new Color(0.45f,0.5f,0.65f,1f))).width(130).right();
        items.row();

        Object[][] rows = {
            {Action.BUY_MED,   "[1]", "Medkit      +25 HP",           " 50 coins"},
            {Action.BUY_ARMOR, "[2]", "Armor       +50 armor",        "100 coins"},
            {Action.BUY_AK,    "[3]", "AK-47       fast fire 20 dmg", "200 coins"}, // ← было 100
            {Action.BUY_SG,    "[4]", "Shotgun     5 pellets 15 dmg", "100 coins"}, // ← было 150
        };

        for (Object[] row : rows) {
            final Action act = (Action) row[0];
            Label keyLbl   = mkLabel((String)row[1], new Color(0.4f,0.9f,1f,1f));
            TextButton btn = new TextButton("  " + row[2] + "  ", skin);
            Label priceL   = mkLabel((String)row[3], Color.GOLD);

            btn.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) { pending = act; }
            });

            items.add(keyLbl).width(80).center();
            items.add(btn).width(360).height(56).left().padLeft(10);
            items.add(priceL).width(130).right().padLeft(10);
            items.row();
        }

        feedbackLabel = new Label("", skin, "small");

        TextButton btnStart = new TextButton("   Start Level " + nextLevel + "  [ENTER]  ", skin);
        btnStart.getLabel().setColor(new Color(0.3f, 1f, 0.45f, 1f));
        btnStart.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { pending = Action.START; }
        });

        root.add(title).padBottom(8).row();
        root.add(moneyLabel).padBottom(22).row();
        root.add(items).padBottom(8).row();
        root.add(feedbackLabel).height(26).padBottom(16).row();
        root.add(btnStart).width(560).height(66);

        stage.addActor(root);
    }

    private Label mkLabel(String text, Color color) {
        Label l = new Label(text, skin, "small");
        l.setColor(color);
        return l;
    }

    private void handleResult(String shopResult, String successMsg) {
        if (shopResult == null) {
            feedbackLabel.setText(successMsg);
            feedbackLabel.setColor(Color.GREEN);
        } else {
            feedbackLabel.setText(shopResult);
            feedbackLabel.setColor(
                (shopResult.contains("full") || shopResult.contains("maximum")
                    || shopResult.contains("already"))
                    ? Color.YELLOW
                    : Color.RED
            );
        }
        feedbackTimer = 2.5f;
        moneyLabel.setText("Coins:  " + player.getMoney());
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if (feedbackTimer > 0) {
            feedbackTimer -= dt;
            if (feedbackTimer <= 0) feedbackLabel.setText("");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) pending = Action.BUY_MED;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) pending = Action.BUY_ARMOR;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) pending = Action.BUY_AK;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) pending = Action.BUY_SG;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) ||
            Gdx.input.isKeyJustPressed(Input.Keys.SPACE))  pending = Action.START;

        switch (pending) {
            case BUY_MED:   handleResult(shop.buyMedkit(),  "Medkit purchased!  +25 HP");   break;
            case BUY_ARMOR: handleResult(shop.buyArmor(),   "Armor purchased!  +50 armor"); break;
            case BUY_AK:    handleResult(shop.buyAK47(),    "AK-47 equipped!");              break;
            case BUY_SG:    handleResult(shop.buyShotgun(), "Shotgun equipped!");            break;
            case START:     gsm.setState(new PlayState(gsm, player, nextLevel));             break;
            default: break;
        }
        pending = Action.NONE;
    }

    @Override
    public void render(SpriteBatch batch) {
        Assets.sr.setProjectionMatrix(stage.getCamera().combined);
        Assets.sr.begin(ShapeRenderer.ShapeType.Filled);

        Assets.sr.setColor(new Color(0.04f, 0.1f, 0.06f, 0.75f));
        Assets.sr.rect(170, 50, 940, 620);
        Assets.sr.setColor(new Color(0.2f, 0.9f, 0.4f, 0.5f));
        Assets.sr.rect(170, 50, 3, 620);
        Assets.sr.rect(1107, 50, 3, 620);

        Assets.sr.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}