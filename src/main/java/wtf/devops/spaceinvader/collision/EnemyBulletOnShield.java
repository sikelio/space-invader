package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;
import wtf.devops.spaceinvader.common.ShieldState;
import wtf.devops.spaceinvader.components.ShieldComponent;

public class EnemyBulletOnShield extends CollisionHandler {
    private Connection<Bundle> client;

    public EnemyBulletOnShield() {
        super(EntityType.ENEMY_BULLET, EntityType.SHIELD);
    }

    @Override
    protected void onCollisionBegin(Entity enemyBullet, Entity shield) {
        ShieldComponent shieldComponent = shield.getComponent(ShieldComponent.class);

        FXGL.getGameWorld().removeEntity(enemyBullet);

        shield.getComponent(ShieldComponent.class).setLifepoint(shieldComponent.getLifepoint() - 100);

        if (shieldComponent.getLifepoint() <= 1000 && shieldComponent.getLifepoint() >= 666) {
            shieldComponent.setState(ShieldState.New);
        } else if (shieldComponent.getLifepoint() < 666 && shieldComponent.getLifepoint() >= 333) {
            shieldComponent.setState(ShieldState.SlightlyDamaged);
        } else if (shieldComponent.getLifepoint() < 333 && shieldComponent.getLifepoint() > 0) {
            shieldComponent.setState(ShieldState.Damaged);
        }  else if (shieldComponent.getLifepoint() <= 0) {
            shieldComponent.setState(ShieldState.Destroyed);
        }

        shieldComponent.updateTexture();

        if (shieldComponent.getLifepoint() <= 0) {
            FXGL.getGameWorld().removeEntity(shield);
        }
    }
}
