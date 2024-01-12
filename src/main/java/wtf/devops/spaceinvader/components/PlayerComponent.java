package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.TransformComponent;

import java.io.Console;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class PlayerComponent extends Component {
    private double dx = 0;
    private final double speed = 1;
    private boolean canShoot = true;
    private double lastTimeShoot = 0;

    @Override
    public void onUpdate(double tpf) {
        this.dx = 300 * tpf;

        if (!canShoot) {
            if ((getGameTimer().getNow() - lastTimeShoot) >= 1.0 / 3.0) {
                canShoot = true;
            }
        }
    }

    public void left() {
        if (getEntity().getX() - dx >= 0)
            getEntity().translateX(-dx);
    }

    public void right() {
        if (getEntity().getX() + getEntity().getWidth() + dx <= 600)
            getEntity().translateX(dx);
    }

    public void shoot() {
        if (!canShoot) {
            return;
        }

        this.canShoot = false;
        this.lastTimeShoot = getGameTimer().getNow();

        spawn("bullet", new SpawnData(0, 0).put("owner", getEntity()));
    }
}
