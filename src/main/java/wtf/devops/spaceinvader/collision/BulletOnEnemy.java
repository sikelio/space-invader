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
        System.out.println(bullet.getProperties());

        if (bullet.getProperties().exists("playerID")) {
            switch (bullet.getProperties().getValue("playerID").toString()) {
                case "1":
                    FXGL.inc("scorePlayer1", +10);
                    break;
                case "2":
                    FXGL.inc("scorePlayer2", +10);
                    break;
                default:
                    throw new IllegalStateException("Unexpected Player");
            }
        }

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
