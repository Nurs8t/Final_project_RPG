package com.chronoshatter.utils;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIFactory {

    public static Skin createSkin() {
        Skin skin = new Skin();

        // --- Fonts ---
        BitmapFont fontMed = new BitmapFont();
        fontMed.getData().setScale(1.9f);
        skin.add("font-med", fontMed);

        BitmapFont fontSmall = new BitmapFont();
        fontSmall.getData().setScale(1.3f);
        skin.add("font-small", fontSmall);

        // --- Button textures (1x1 Pixmap trick) ---
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pm.setColor(new Color(0.12f, 0.16f, 0.28f, 1f)); pm.fill();
        Texture upTex = new Texture(pm);

        pm.setColor(new Color(0.2f, 0.28f, 0.48f, 1f)); pm.fill();
        Texture overTex = new Texture(pm);

        pm.setColor(new Color(0.06f, 0.08f, 0.15f, 1f)); pm.fill();
        Texture downTex = new Texture(pm);

        pm.dispose();

        // --- TextButton default style ---
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font          = fontMed;
        btnStyle.fontColor     = Color.WHITE;
        btnStyle.overFontColor = new Color(0.4f, 0.95f, 1f, 1f);  // cyan on hover
        btnStyle.downFontColor = Color.LIGHT_GRAY;
        btnStyle.up   = new TextureRegionDrawable(new TextureRegion(upTex));
        btnStyle.over = new TextureRegionDrawable(new TextureRegion(overTex));
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(downTex));
        skin.add("default", btnStyle);

        // --- Label default style ---
        Label.LabelStyle lStyle = new Label.LabelStyle();
        lStyle.font      = fontMed;
        lStyle.fontColor = Color.WHITE;
        skin.add("default", lStyle);

        // --- Label small style ---
        Label.LabelStyle lSmall = new Label.LabelStyle();
        lSmall.font      = fontSmall;
        lSmall.fontColor = Color.WHITE;
        skin.add("small", lSmall);

        return skin;
    }
}
