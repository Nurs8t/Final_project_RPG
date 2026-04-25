package com.chronoshatter.entities.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component {

    public enum State { IDLE, WALK, ATTACK, DEATH }

    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> deathAnim;

    private State currentState;
    private float stateTime;

    public AnimationComponent() {
        this.currentState = State.IDLE;
        this.stateTime    = 0f;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        Animation<TextureRegion> anim = getCurrentAnimation();
        if (anim == null) return null;
        return anim.getKeyFrame(stateTime, true);
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        switch (currentState) {
            case IDLE:   return idleAnim;
            case WALK:   return walkAnim;
            case ATTACK: return attackAnim;
            case DEATH:  return deathAnim;
            default:     return idleAnim;
        }
    }

    public void setState(State state) {
        if (this.currentState != state) {
            this.currentState = state;
            this.stateTime    = 0f;
        }
    }

    public void setIdleAnim(Animation<TextureRegion> anim)   { this.idleAnim   = anim; }
    public void setWalkAnim(Animation<TextureRegion> anim)   { this.walkAnim   = anim; }
    public void setAttackAnim(Animation<TextureRegion> anim) { this.attackAnim = anim; }
    public void setDeathAnim(Animation<TextureRegion> anim)  { this.deathAnim  = anim; }

    public State getCurrentState() { return currentState; }
    public float getStateTime()    { return stateTime; }
}
