package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.component.Component;

import java.util.HashSet;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.spawn;

public class Wave {
    private HashSet<Entity> enemies = new HashSet<>();
    private int[] xCord = {34, 82, 130, 179, 227, 276, 324, 372, 421, 469, 518, 560};
    private int[] yCord = {525, 450, 400, 350, 300, 250, 200, 150, 100, 50};

    public void generateWave(){
        double delay = 0.3;

        for(int i = 0; i < 5; i++) {
            for (int e = 0; i < 10; i++) {
                this.enemies.add(spawn("enemy", 50, 50));
            }
        }
    }
}

/* private static class WaveComponent extends Component {
    @Override
    public void onUpdate(){
        
    }
} */