package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;

public class BulletOnShield extends CollisionHandler {
    private Connection<Bundle> client;

    public BulletOnShield() {
        super(EntityType.BULLET, EntityType.SHIELD);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity shield) {
        FXGL.getGameWorld().removeEntity(bullet);
    }

    /*private void sendPlayerAction(String action) {
        if (this.client != null) {
            Bundle message = new Bundle("PlayerAction");
            message.put("action", action);
            this.client.send(message);
        } else {
            System.out.println("Connection is not established.");
        }
    }*/
}
