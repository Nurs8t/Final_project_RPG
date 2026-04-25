package com.chronoshatter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chronoshatter.entities.components.AnimationComponent;
import com.chronoshatter.entities.components.ChronoComponent;
import com.chronoshatter.entities.components.HealthComponent;
import com.chronoshatter.entities.components.TransformComponent;

public class Player extends Entity {

    private float speed = 150f;

    public Player(float startX, float startY) {
        addComponent(new HealthComponent(100));
        addComponent(new TransformComponent(startX, startY));
        addComponent(new AnimationComponent());
        addComponent(new ChronoComponent());
    }

    @Override
    public void update(float delta) {
        TransformComponent  transform  = getComponent(TransformComponent.class);
        AnimationComponent  animation  = getComponent(AnimationComponent.class);
        ChronoComponent     chrono     = getComponent(ChronoComponent.class);

        float actualSpeed = speed * chrono.getTimeScale();
        boolean moving    = false;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            transform.y += actualSpeed * delta;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            transform.y -= actualSpeed * delta;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            transform.x -= actualSpeed * delta;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            transform.x += actualSpeed * delta;
            moving = true;
        }

        if (moving) {
            animation.setState(AnimationComponent.State.WALK);
        } else {
            animation.setState(AnimationComponent.State.IDLE);
        }

        animation.update(delta);
    }

    @Override
    public void render(SpriteBatch batch) {
        TransformComponent transform = getComponent(TransformComponent.class);
        AnimationComponent animation = getComponent(AnimationComponent.class);

        if (animation.getCurrentFrame() != null) {
            batch.draw(animation.getCurrentFrame(), transform.x, transform.y, 64, 64);
        }
    }

    @Override
    public void dispose() { }

    public HealthComponent getHealth() {
        return getComponent(HealthComponent.class);
    }
}
