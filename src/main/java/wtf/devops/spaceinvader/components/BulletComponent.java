package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    private double speed;

    public BulletComponent(double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(-tpf * this.speed);

        if (entity.getY() + entity.getHeight() < 0) {
            FXGL.getGameWorld().removeEntity(entity);
        }
    }
}
