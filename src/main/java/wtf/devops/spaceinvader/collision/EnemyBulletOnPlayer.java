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
    }

    @Override
    protected void onCollisionBegin(Entity enemyBullet, Entity player) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        if (player.getProperties().exists("playerID")) {
            switch (player.getProperties().getValue("playerID").toString()) {
                case "1":
                    FXGL.inc("livesPlayer1", -1);
                    break;
                case "2":
                    FXGL.inc("livesPlayer2", -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected Player");
            }
        }

        FXGL.getGameWorld().removeEntity(enemyBullet);

        playerComponent.setLifepoint(playerComponent.getLifepoint() - 1);

        if (playerComponent.getLifepoint() <= 0) {
            this.onGameEnd();
        }
    }

    private void onGameEnd() {
        FXGL.getDialogService().showMessageBox("Game Over!", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }
}
