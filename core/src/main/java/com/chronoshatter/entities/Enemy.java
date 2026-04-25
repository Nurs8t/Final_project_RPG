package com.chronoshatter.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chronoshatter.entities.components.AnimationComponent;
import com.chronoshatter.entities.components.HealthComponent;
import com.chronoshatter.entities.components.TransformComponent;

public abstract class Enemy extends Entity {

    protected int attackPower;
    protected float speed;

    public Enemy(float x, float y, int hp, int attackPower, float speed) {
        addComponent(new HealthComponent(hp));
        addComponent(new TransformComponent(x, y));
        addComponent(new AnimationComponent());
        this.attackPower = attackPower;
        this.speed       = speed;
    }

    @Override
    public void update(float delta) {
        getComponent(AnimationComponent.class).update(delta);
        moveTowardsPlayer(delta);
    }

    protected abstract void moveTowardsPlayer(float delta);

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

    public boolean isDead() {
        return getComponent(HealthComponent.class).isDead();
    }

    public int getAttackPower() { return attackPower; }
}
