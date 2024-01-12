package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    private double speed;

    public BulletComponent(double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(-tpf * this.speed);
    }
}
