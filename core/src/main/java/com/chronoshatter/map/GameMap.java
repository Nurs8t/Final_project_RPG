package com.chronoshatter.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public static final int TILE_W = 16;
    public static final int TILE_H = 16;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private int mapPixelW;
    private int mapPixelH;

    private final List<Rectangle> collisionRects = new ArrayList<>();

    public GameMap(int level) {
        String path = "maps/level" + level + ".tmx";

        try {
            map = new TmxMapLoader().load(path);
            renderer = new OrthogonalTiledMapRenderer(map);

            loadMapSize();
            loadCollisionObjects();

        } catch (Exception e) {
            e.printStackTrace();

            mapPixelW = 1600;
            mapPixelH = 900;
        }
    }

    private void loadMapSize() {
        MapProperties props = map.getProperties();

        int width = props.get("width", Integer.class);
        int height = props.get("height", Integer.class);
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        mapPixelW = width * tileWidth;
        mapPixelH = height * tileHeight;

        if (mapPixelW <= 0 || mapPixelH <= 0) {
            mapPixelW = 1600;
            mapPixelH = 900;
        }
    }

    private void loadCollisionObjects() {
        collisionRects.clear();

        if (map == null) {
            return;
        }

        MapLayer collisionLayer = map.getLayers().get("collision");

        if (collisionLayer == null) {
            System.out.println("No collision layer found in map");
            return;
        }

        for (MapObject object : collisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                collisionRects.add(new Rectangle(
                    rect.x,
                    rect.y,
                    rect.width,
                    rect.height
                ));
            }
        }

        System.out.println("Loaded collision rectangles: " + collisionRects.size());
    }

    public boolean isSolid(float worldX, float worldY) {
        for (Rectangle rect : collisionRects) {
            if (rect.contains(worldX, worldY)) {
                return true;
            }
        }

        return false;
    }

    public boolean isSolidRect(float x, float y, float w, float h) {
        Rectangle playerRect = new Rectangle(x, y, w, h);

        for (Rectangle rect : collisionRects) {
            if (playerRect.overlaps(rect)) {
                return true;
            }
        }

        return false;
    }

    public void render(OrthographicCamera camera) {
        if (renderer == null) {
            return;
        }

        renderer.setView(camera);
        renderer.render();
    }

    public void renderFallback(ShapeRenderer sr) {
        if (map != null) {
            return;
        }

        sr.setColor(new Color(0.15f, 0.15f, 0.2f, 1f));
        sr.rect(0, 0, mapPixelW, mapPixelH);
    }

    public int getPixelWidth() {
        return mapPixelW;
    }

    public int getPixelHeight() {
        return mapPixelH;
    }

    public void dispose() {
        if (map != null) {
            map.dispose();
        }
    }
    public float[] getValidSpawnPosition(float entitySize) {
    java.util.Random rand = new java.util.Random();
    float margin = entitySize + 30f;
    float minX = margin, maxX = mapPixelW - margin - entitySize;
    float minY = margin, maxY = mapPixelH - margin - entitySize;
    for (int attempt = 0; attempt < 200; attempt++) {
        float x = minX + rand.nextFloat() * (maxX - minX);
        float y = minY + rand.nextFloat() * (maxY - minY);
        if (!isSolidRect(x, y, entitySize, entitySize))
            return new float[]{x, y};
    }
    return new float[]{mapPixelW / 2f, mapPixelH / 2f};
}
}
