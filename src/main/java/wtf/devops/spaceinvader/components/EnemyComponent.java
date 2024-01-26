package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.entity.component.Component;

public class EnemyComponent extends Component {
    public void die() {
        entity.removeFromWorld();
    }
}
