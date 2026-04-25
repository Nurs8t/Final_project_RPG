package com.chronoshatter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BattleState implements GameState {

    private GameStateManager gsm;
    private int playerHP    = 100;
    private int enemyHP     = 80;
    private int playerATK   = 15;
    private int enemyATK    = 10;
    private boolean playerTurn  = true;
    private float turnTimer     = 0f;
    private float turnDelay     = 1.5f;
    private boolean battleOver  = false;
    private boolean playerWon   = false;

    public BattleState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void enter() {
        playerHP   = 100;
        enemyHP    = 80;
        playerTurn = true;
        battleOver = false;
    }

    @Override
    public void update(float delta) {
        if (battleOver) {
            turnTimer += delta;
            if (turnTimer >= 2f) {
                if (playerWon) {
                    gsm.popState();
                } else {
                    gsm.setState(new GameOverState(gsm));
                }
            }
            return;
        }

        if (playerTurn) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                playerAttack();
            }
        } else {
            turnTimer += delta;
            if (turnTimer >= turnDelay) {
                enemyAttack();
                turnTimer = 0f;
            }
        }
    }

    private void playerAttack() {
        enemyHP -= playerATK;
        if (enemyHP <= 0) {
            enemyHP    = 0;
            battleOver = true;
            playerWon  = true;
            turnTimer  = 0f;
        } else {
            playerTurn = false;
        }
    }

    private void enemyAttack() {
        playerHP -= enemyATK;
        if (playerHP <= 0) {
            playerHP   = 0;
            battleOver = true;
            playerWon  = false;
            turnTimer  = 0f;
        } else {
            playerTurn = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.end();
    }

    @Override
    public void exit() { }

    @Override
    public void dispose() { }

    public int getPlayerHP()      { return playerHP; }
    public int getEnemyHP()       { return enemyHP; }
    public boolean isPlayerTurn() { return playerTurn; }
}
