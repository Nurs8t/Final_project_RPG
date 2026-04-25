package com.chronoshatter.entities;

import com.chronoshatter.entities.components.Component;
import java.util.HashMap;
import java.util.Map;

public abstract class Entity {

    private static int idCounter = 0;

    private final int id;
    private final Map<Class<? extends Component>, Component> components;

    public Entity() {
        this.id         = idCounter++;
        this.components = new HashMap<>();
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.get(type);
    }

    public <T extends Component> boolean hasComponent(Class<T> type) {
        return components.containsKey(type);
    }

    public int getId() { return id; }

    public abstract void update(float delta);
    public abstract void render(com.badlogic.gdx.graphics.g2d.SpriteBatch batch);
    public abstract void dispose();
}
