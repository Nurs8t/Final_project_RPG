//package com.chronoshatter.objects;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Vector2;
//
//public class Falcon {
//    private final Vector2 position;
//    private boolean rescued = false;
//
//    private static ShapeRenderer shapeRenderer;
//
//    public Falcon(float x, float y) {
//        this.position = new Vector2(x, y);
//        if (shapeRenderer == null) {
//            shapeRenderer = new ShapeRenderer();
//        }
//    }
//
//    public void render(SpriteBatch batch) {
//        if (rescued) return;
//        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.ORANGE);
//        shapeRenderer.rect(position.x, position.y, 30, 30);
//        shapeRenderer.end();
//    }
//
//    public boolean checkRescue(Vector2 playerPos) {
//        return !rescued && playerPos.dst(position) < 40;
//    }
//
//    public void rescue() {
//        rescued = true;
//    }
//}
