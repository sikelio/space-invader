package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;

public class BulletOnEnemy extends CollisionHandler {
    public BulletOnEnemy() {
        super(EntityType.BULLET, EntityType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        FXGL.inc("score", +10);
        FXGL.getGameWorld().removeEntity(bullet);
        FXGL.getGameWorld().removeEntity(enemy);

        if (FXGL.getGameWorld().getEntitiesByType(EntityType.ENEMY).isEmpty()) {
            this.onGameEnd();
        }
    }

    private void onGameEnd() {
        FXGL.getDialogService().showMessageBox("Congratulation! You win!", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }
}
