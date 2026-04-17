package com.chronoshatter.systems;

public class ChronoAbility {
    private boolean active      = false;
    private float   activeTimer = 0f;
    private float   coolTimer   = 0f;

    private static final float DURATION = 5f;
    private static final float COOLDOWN = 8f;

    public void activate() {
        if (!active && coolTimer <= 0) {
            active      = true;
            activeTimer = DURATION;
            coolTimer   = DURATION + COOLDOWN;
        }
    }

    public void update(float dt) {
        if (active) {
            activeTimer -= dt;
            if (activeTimer <= 0) { active = false; activeTimer = 0; }
        }
        if (coolTimer > 0) coolTimer -= dt;
        if (coolTimer < 0) coolTimer = 0;
    }

    public boolean isActive()          { return active; }
    public float   getSlowMultiplier() { return active ? 0.33f : 1.0f; }

    /** 0 = пусто, 1 = полный */
    public float getReadyFraction() {
        if (active) return activeTimer / DURATION;        // убывает пока активен
        if (coolTimer > 0) return 1f - (coolTimer / (DURATION + COOLDOWN)); // растёт на перезарядке
        return 1f;                                        // полный — готов
    }

    public boolean isReady()           { return !active && coolTimer <= 0; }
    public float   getCooldownProgress(){ return coolTimer > 0 ? coolTimer / (DURATION + COOLDOWN) : 0; }
}
