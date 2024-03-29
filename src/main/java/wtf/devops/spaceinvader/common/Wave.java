package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.time.Timer;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Wave {
    private HashSet<Entity> enemies = new HashSet<>();
    private int[] xCord = {34, 82, 130, 179, 227, 276, 324, 372, 421, 469, 518, 560};
    private int[] yCord = {525, 475, 425, 375, 325};


    public HashSet<Entity> generateWave(){
        double delay = 0.3;

        Connection<Bundle> connection = FXGL.geto("conn");

        for (int i = 0; i < 5; i++) {
            for (int e = 0; e < 12; e++) {
                EnemyType type = determineEnemyType(i, e);

                var enemy = spawn(
                        "enemy",
                        new SpawnData(this.xCord[e], this.yCord[i]).put("enemyType", type)
                );

                getService(MultiplayerService.class).spawn(connection, enemy, "enemy");

                this.enemies.add(enemy);
            }
        }

        return this.enemies;
    }

    public void moveEnemies(HashSet<Entity> enemies) {
        AtomicBoolean entityIsAtRightSide = new AtomicBoolean(false);

        Timer timer = FXGL.getGameTimer();
        timer.runAtInterval(() -> {
            boolean atRightEdge = enemies.stream().anyMatch(enemy -> enemy.getRightX() >= FXGL.getAppWidth() - 2.5);
            boolean atLeftEdge = enemies.stream().anyMatch(enemy -> enemy.getX() <= 2.5);

            if (atRightEdge || atLeftEdge) {
                enemies.forEach(enemy -> enemy.translateY(5));
                entityIsAtRightSide.set(!entityIsAtRightSide.get());
            }

            int dx = entityIsAtRightSide.get() ? -5 : 5;
            enemies.forEach(enemy -> enemy.translateX(dx));

            boolean atBottom = enemies.stream().anyMatch(enemy -> enemy.getY() >= FXGL.getAppHeight() - enemy.getHeight());
            if (atBottom) {
                timer.clear();
                this.onGameEnd();
            }
        }, Duration.seconds(0.25));
    }

    private void onGameEnd() {
        FXGL.getDialogService().showMessageBox("Game Over!", () -> {
            FXGL.getGameController().gotoMainMenu();
        });
    }

    private EnemyType determineEnemyType(int row, int col) {
        return switch (row) {
            case 0, 1 -> EnemyType.SQUID;
            case 2, 3 -> EnemyType.OCTOPUS;
            default -> EnemyType.CRAB;
        };
    }
}