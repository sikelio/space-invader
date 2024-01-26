package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;

import java.util.HashSet;

public class BulletOnEnemy extends CollisionHandler {
    private HashSet<Entity> enemies;

    public BulletOnEnemy(HashSet<Entity> enemies) {
        super(EntityType.BULLET, EntityType.ENEMY);
        this.enemies = enemies;
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        this.enemies.remove(enemy);

        FXGL.inc("score", +10);
        FXGL.getGameWorld().removeEntity(bullet);
        FXGL.getGameWorld().removeEntity(enemy);

        if (this.enemies.isEmpty()) {
            this.onGameEnd();
        }
    }

    private void onGameEnd() {
        FXGL.getDialogService().showMessageBox("Congratulation! You win!", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }
}
