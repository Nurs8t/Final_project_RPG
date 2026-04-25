package com.chronoshatter.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

public class GameStateManager {

    private static GameStateManager instance;
    private final Stack<GameState> states;

    private GameStateManager() {
        states = new Stack<>();
    }

    public static GameStateManager getInstance() {
        if (instance == null) {
            instance = new GameStateManager();
        }
        return instance;
    }

    public void pushState(GameState state) {
        states.push(state);
        state.enter();
    }

    public void popState() {
        if (!states.isEmpty()) {
            states.peek().exit();
            states.pop().dispose();
        }
    }

    public void setState(GameState state) {
        if (!states.isEmpty()) {
            states.peek().exit();
            states.pop().dispose();
        }
        pushState(state);
    }

    public void update(float delta) {
        if (!states.isEmpty()) {
            states.peek().update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        if (!states.isEmpty()) {
            states.peek().render(batch);
        }
    }

    public void dispose() {
        while (!states.isEmpty()) {
            states.peek().exit();
            states.pop().dispose();
        }
    }
}
