package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.almasb.fxgl.net.Connection;

import static com.almasb.fxgl.dsl.FXGL.getService;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class PlayerComponent extends Component {
    private double dx = 0;
    private final double speed = 1;
    private boolean canShoot = true;
    private double lastTimeShoot = 0;
    private int lifepoint;
    Connection<Bundle> client;

    public PlayerComponent(int lifepoint) {
        this.lifepoint = lifepoint;
    }

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
        if (getEntity().getX() - dx >= 0) {
            getEntity().translateX(-dx);
        }
    }

    public void right() {
        if (getEntity().getX() + getEntity().getWidth() + dx <= 600) {
            getEntity().translateX(dx);
        }
    }

    public void shoot() {
        Connection<Bundle> connection = FXGL.geto("conn");

        if (!canShoot) {
            return;
        }

        this.canShoot = false;
        this.lastTimeShoot = getGameTimer().getNow();

        Entity bullet = spawn("bullet", new SpawnData(getEntity().getX(), getEntity().getY()));
        bullet.setProperty("playerID", getEntity().getProperties().getValue("playerID").toString());
        getService(MultiplayerService.class).spawn(connection, bullet, "bullet");
    }

    public int getLifepoint() {
        return lifepoint;
    }

    public void setLifepoint(int lifepoint) {
        this.lifepoint = lifepoint;
    }
}
