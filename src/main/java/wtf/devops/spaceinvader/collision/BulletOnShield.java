package wtf.devops.spaceinvader.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import wtf.devops.spaceinvader.common.EntityType;

public class BulletOnShield extends CollisionHandler {
    public BulletOnShield() {
        super(EntityType.BULLET, EntityType.SHIELD);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity shield) {
        FXGL.getGameWorld().removeEntity(bullet);
    }
}
