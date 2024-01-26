package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import wtf.devops.spaceinvader.common.*;
import wtf.devops.spaceinvader.components.*;
import wtf.devops.spaceinvader.common.Wave;
import wtf.devops.spaceinvader.factories.SpaceInvaderEntityFactory;
import wtf.devops.spaceinvader.factories.SpaceInvaderSceneFactory;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

public class SpaceInvaderApp extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Space Invader");
        gameSettings.setVersion("1.0.0");
        gameSettings.setMainMenuEnabled(true);
        gameSettings.setSceneFactory(new SpaceInvaderSceneFactory());
    }

    private Entity player;
    private HashSet<Entity> enemies;
    private Stack<Entity> shield;

    @Override
    protected void initGame() {
    	getGameWorld().addEntityFactory(new SpaceInvaderEntityFactory());
        this.player = getGameWorld().spawn("player");

        Wave enemiesWave = new Wave();
        this.enemies = enemiesWave.generateWave();
        enemiesWave.moveEnemies(this.enemies);
        
        this.shield = new Stack<Entity>();
        this.shield.push(spawn("shield", new SpawnData(1, 1).put("x", 50)));
        this.shield.push(spawn("shield", new SpawnData(1, 1).put("x", 200)));
        this.shield.push(spawn("shield", new SpawnData(1, 1).put("x", 375)));
        this.shield.push(spawn("shield", new SpawnData(1, 1).put("x", 525)));
        
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, () -> this.player.getComponent(PlayerComponent.class).left());
        onKey(KeyCode.RIGHT, () -> this.player.getComponent(PlayerComponent.class).right());
        onKey(KeyCode.SPACE, () -> this.player.getComponent(PlayerComponent.class).shoot());
    }

    @Override
    protected void initUI() {
        Text score = new Text();
        score.setTranslateX(50);
        score.setTranslateY(50);
        score.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString());

        FXGL.getGameScene().addUINode(score);
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            enemies.remove(enemy);

            FXGL.inc("score", +10);

            FXGL.getGameWorld().removeEntity(bullet);
            FXGL.getGameWorld().removeEntity(enemy);
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }
}
