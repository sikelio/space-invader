package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;
import wtf.devops.spaceinvader.common.ShieldState;
import wtf.devops.spaceinvader.components.OwnerComponent;
import wtf.devops.spaceinvader.components.ShieldComponent;

public class EnemyBulletOnShield extends CollisionHandler {
    private Connection<Bundle> client;

    public EnemyBulletOnShield() {
        super(EntityType.ENEMY_BULLET, EntityType.SHIELD);

        this.client = FXGL.geto("networkClient");
    }

    @Override
    protected void onCollisionBegin(Entity enemyBullet, Entity shield) {
        ShieldComponent shieldComponent = shield.getComponent(ShieldComponent.class);

        FXGL.getGameWorld().removeEntity(enemyBullet);

        shield.getComponent(ShieldComponent.class).setLifepoint(shieldComponent.getLifepoint() - 100);

        if (shieldComponent.getLifepoint() <= 1000 && shieldComponent.getLifepoint() >= 666) {
            shieldComponent.setState(ShieldState.New);
            this.sendEnemyAction("shieldNew");
        } else if (shieldComponent.getLifepoint() < 666 && shieldComponent.getLifepoint() >= 333) {
            shieldComponent.setState(ShieldState.SlightlyDamaged);
            this.sendEnemyAction("shieldSlightlyDamaged");
        } else if (shieldComponent.getLifepoint() < 333 && shieldComponent.getLifepoint() > 0) {
            shieldComponent.setState(ShieldState.Damaged);
            this.sendEnemyAction("shieldDamaged");
        }  else if (shieldComponent.getLifepoint() <= 0) {
            shieldComponent.setState(ShieldState.Destroyed);
            this.sendEnemyAction("shieldDestroyed");
        }

        shieldComponent.updateTexture();

        if (shieldComponent.getLifepoint() <= 0) {
            FXGL.getGameWorld().removeEntity(shield);
        }
    }

    private void sendEnemyAction(String action) {
        if (this.client != null) {
            Bundle message = new Bundle("EnemyAction");
            message.put("action", action);
            this.client.send(message);
        } else {
            System.out.println("Connection is not established.");
        }
    }
}
