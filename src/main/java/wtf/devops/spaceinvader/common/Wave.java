package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Stack;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Wave {
    private HashSet<Entity> enemies = new HashSet<>();
    private int[] xCord = {34, 82, 130, 179, 227, 276, 324, 372, 421, 469, 518, 560};
    private int[] yCord = {525, 475, 425, 375, 325};


    public HashSet<Entity> generateWave(){
        double delay = 0.3;

        for(int i = 0; i < 5; i++) {
            for (int e = 0; e < 12; e++) {

                    this.enemies.add(getGameWorld().
                            spawn("enemy",
                                    new SpawnData(0,0).put("X", this.xCord[e]).put("Y", this.yCord[i])));

            }
        }
        return this.enemies;
    }

    public void moveEnemies(HashSet<Entity> enemies){
        FXGL.getGameTimer().runAtInterval(() ->  {
            for(Entity enemie : enemies){
                enemie.translateX(5);
            }
        }, Duration.seconds(1));
    }
}