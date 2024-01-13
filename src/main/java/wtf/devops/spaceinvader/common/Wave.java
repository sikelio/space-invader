package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
        // Calculer max itération
        AtomicInteger iterationCount = new AtomicInteger(0);
        AtomicBoolean entityIsAtRightSide = new AtomicBoolean(false);

        int totalIterations = 25;

        var timer = FXGL.getGameTimer();
        timer.runAtInterval(() -> {
            if(iterationCount.get() < 2){
                enemies.forEach(enemie -> enemie.translateX(5));
            }else{
                if((iterationCount.get() - 2) % 6 == 0){
                    enemies.forEach(enemie -> enemie.translateY(5));
                    entityIsAtRightSide.set(!entityIsAtRightSide.get());
                }else{
                    if(entityIsAtRightSide.get()){
                        enemies.forEach(enemie -> enemie.translateX(-5));
                    }else{
                        enemies.forEach(enemie -> enemie.translateX(5));
                    }
                }
            }

            if(iterationCount.incrementAndGet() >= totalIterations){
                timer.clear();
            }
        }, Duration.seconds(0.5));
    }

}