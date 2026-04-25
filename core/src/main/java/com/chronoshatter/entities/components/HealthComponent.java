package com.chronoshatter.entities.components;

public class HealthComponent implements Component {

    private int currentHP;
    private int maxHP;

    public HealthComponent(int maxHP) {
        this.maxHP     = maxHP;
        this.currentHP = maxHP;
    }

    public void takeDamage(int amount) {
        currentHP -= amount;
        if (currentHP < 0) currentHP = 0;
    }

    public void heal(int amount) {
        currentHP += amount;
        if (currentHP > maxHP) currentHP = maxHP;
    }

    public boolean isDead() {
        return currentHP <= 0;
    }

    public int getCurrentHP() { return currentHP; }
    public int getMaxHP()     { return maxHP; }
}
