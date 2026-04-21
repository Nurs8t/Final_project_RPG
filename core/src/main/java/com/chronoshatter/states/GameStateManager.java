package com.chronoshatter.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

public class GameStateManager {
    private final Stack<GameState> states = new Stack<>();

    public void pushState(GameState state) {
        states.push(state);
    }

    public void popState() {
        if (!states.isEmpty()) {
            states.pop().dispose();
        }
    }

    public void setState(GameState state) {
        // Сначала убираем старый стейт из стека, пушим новый,
        // и только ПОТОМ вызываем dispose() — иначе dispose() затирает
        // InputProcessor, который новый стейт уже успел установить.
        GameState old = states.isEmpty() ? null : states.pop();
        states.push(state);
        if (old != null) old.dispose();
    }

    public void update(float dt) {
        if (!states.isEmpty()) {
            states.peek().update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        if (!states.isEmpty()) {
            states.peek().render(batch);
        }
    }

    public void dispose() {
        while (!states.isEmpty()) {
            states.pop().dispose();
        }
    }
}