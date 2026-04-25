package com.chronoshatter.entities.components;

public class ChronoComponent implements Component {

    private boolean affectedByTime;
    private float timeScale;

    public ChronoComponent() {
        this.affectedByTime = true;
        this.timeScale      = 1f;
    }

    public void slowDown(float scale) { this.timeScale = scale; }
    public void resetTime()           { this.timeScale = 1f; }

    public boolean isAffectedByTime() { return affectedByTime; }
    public float getTimeScale()       { return timeScale; }
    public void setAffectedByTime(boolean val) { this.affectedByTime = val; }
}
