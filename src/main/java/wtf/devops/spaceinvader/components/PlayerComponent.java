package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class PlayerComponent extends Component {
    private final double speed = 1;
    private boolean canShoot = true;
    private double lastTimeShoot = 0;

    @Override
    public void onUpdate(double tpf) {
        if (!canShoot) {
            if ((getGameTimer().getNow() - lastTimeShoot) >= 1.0 / 3.0) {
                canShoot = true;
            }
        }
    }

    public void left() {
        if (this.entity.getX() < 0) {
            return;
        }

        this.entity.translateX(-5 * this.speed);
    }

    public void right() {
        if (this.entity.getX() > 575) {
            return;
        }

        this.entity.translateX(5 * this.speed);
    }

    public void shoot() {
        if (!canShoot) {
            return;
        }

        this.canShoot = false;
        this.lastTimeShoot = getGameTimer().getNow();

        spawn("Laser", new SpawnData(0, 0).put("owner", getEntity()));
    }
}
