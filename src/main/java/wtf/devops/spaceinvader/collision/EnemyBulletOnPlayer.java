package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;
import wtf.devops.spaceinvader.components.PlayerComponent;

public class EnemyBulletOnPlayer extends CollisionHandler {
    public EnemyBulletOnPlayer() {
        super(EntityType.ENEMY_BULLET, EntityType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity enemyBullet, Entity player) {
        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        FXGL.getGameWorld().removeEntity(enemyBullet);
        FXGL.inc("lives", -1);

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
