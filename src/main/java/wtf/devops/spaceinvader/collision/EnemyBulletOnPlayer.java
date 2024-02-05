package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;
import wtf.devops.spaceinvader.components.PlayerComponent;

public class EnemyBulletOnPlayer extends CollisionHandler {
    private Connection<Bundle> client;

    public EnemyBulletOnPlayer() {
        super(EntityType.ENEMY_BULLET, EntityType.PLAYER);

        this.client = FXGL.geto("networkClient");
    }

    @Override
    protected void onCollisionBegin(Entity enemyBullet, Entity player) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        FXGL.getGameWorld().removeEntity(enemyBullet);
        FXGL.inc("lives", -1);

        this.sendEnnemyAction("touchedPlayer");

        playerComponent.setLifepoint(playerComponent.getLifepoint() - 1);

        if (playerComponent.getLifepoint() <= 0) {
            this.sendEnnemyAction("playerDead");
            this.onGameEnd();
        }
    }

    private void onGameEnd() {
        FXGL.getDialogService().showMessageBox("Game Over!", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }

    private void sendEnnemyAction(String action) {
        if (this.client != null) {
            Bundle message = new Bundle("EnemyAction");
            message.put("action", action);
            this.client.send(message);
        } else {
            System.out.println("Connection is not established.");
        }
    }
}
