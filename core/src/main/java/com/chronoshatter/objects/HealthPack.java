//package com.chronoshatter.objects;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Vector2;
//
//public class HealthPack {
//    private final Vector2 position;
//    private boolean used = false;
//
//    private static ShapeRenderer shapeRenderer;
//
//    public HealthPack(float x, float y) {
//        this.position = new Vector2(x, y);
//        if (shapeRenderer == null) {
//            shapeRenderer = new ShapeRenderer();
//        }
//    }
//
//    public void render(SpriteBatch batch) {
//        if (used) return;
//        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.CYAN);
//        shapeRenderer.rect(position.x, position.y, 25, 25);
//        shapeRenderer.end();
//    }
//
//    public boolean checkPickup(Vector2 playerPos) {
//        if (!used && playerPos.dst(position) < 35) {
//            used = true;
//            return true;
//        }
//        return false;
//    }
//
//    public Vector2 getPosition() {
//        return position;
//    }
//}
