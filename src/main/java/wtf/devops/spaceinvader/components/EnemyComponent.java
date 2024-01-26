package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.time.LocalTimer;

import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class EnemyComponent extends Component {
    protected LocalTimer attackCooldown;
    protected Duration nextAttack = Duration.seconds(2);

    @Override
    public void onAdded() {
        this.attackCooldown = FXGL.newLocalTimer();
        this.attackCooldown.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if (attackCooldown.elapsed(nextAttack)) {
            if (FXGLMath.randomBoolean(0.3f)) {
                this.shoot();
            }

            this.nextAttack = Duration.seconds(5 * Math.random());
            this.attackCooldown.capture();
        }
    }

    protected void shoot() {
        spawn("enemy_bullet", new SpawnData(0, 0).put("owner", getEntity()));
    }

    public void die() {
        entity.removeFromWorld();
    }
}
