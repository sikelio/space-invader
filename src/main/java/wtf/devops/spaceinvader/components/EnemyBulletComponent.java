package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class EnemyBulletComponent extends Component {
    private double speed;

    public EnemyBulletComponent(double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(tpf * this.speed);

        if (entity.getY() + entity.getHeight() > 600) {
            FXGL.getGameWorld().removeEntity(entity);
        }
    }
}
