package com.chronoshatter.systems;

import com.chronoshatter.entities.Player;
import com.chronoshatter.utils.Assets;
import com.chronoshatter.weapons.AK47;
import com.chronoshatter.weapons.Shotgun;

public class ShopSystem {
    private final Player player;

    public ShopSystem(Player player) { this.player = player; }

    public String buyMedkit() {
        if (player.getHp() >= player.getMaxHp())
            return "HP is already full!";
        if (!player.spendMoney(50))
            return "Not enough coins!";
        player.heal(25);
        Assets.sounds.playBuy();
        return null;
    }

    public String buyArmor() {
        if (player.getArmor() >= player.getMaxArmor())
            return "Armor is already at maximum!";
        if (!player.spendMoney(100))
            return "Not enough coins!";
        player.addArmor(50);
        Assets.sounds.playBuy();
        return null;
    }

    public String buyAK47() {
        if (player.getWeapon() instanceof AK47)
            return "You already have an AK-47!";
        if (!player.spendMoney(200))  // ← было 100, стало 200
            return "Not enough coins!";
        player.setWeapon(new AK47());
        Assets.sounds.playBuy();
        return null;
    }

    public String buyShotgun() {
        if (player.getWeapon() instanceof Shotgun)
            return "You already have a Shotgun!";
        if (!player.spendMoney(100))  // ← было 150, стало 100
            return "Not enough coins!";
        player.setWeapon(new Shotgun());
        Assets.sounds.playBuy();
        return null;
    }
}