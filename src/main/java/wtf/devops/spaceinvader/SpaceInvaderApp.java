package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.handlers.CollectibleHandler;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import wtf.devops.spaceinvader.collision.BulletOnEnemy;
import wtf.devops.spaceinvader.collision.BulletOnShield;
import wtf.devops.spaceinvader.collision.EnemyBulletOnPlayer;
import wtf.devops.spaceinvader.collision.EnemyBulletOnShield;
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

        Image backgroundImage = FXGL.image("background/background.png");
        FXGL.getGameScene().setBackgroundRepeat(backgroundImage);

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
        score.setTranslateX(25);
        score.setTranslateY(575);
        score.textProperty().bind(FXGL.getWorldProperties().intProperty("score").asString());
        score.setFill(Color.rgb(255, 231, 68));
        score.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-family: Impact;"
        );

        Text lives = new Text();
        lives.setTranslateX(560);
        lives.setTranslateY(575);
        lives.textProperty().bind(FXGL.getWorldProperties().intProperty("lives").asString());
        lives.setFill(Color.rgb(255, 231, 68));
        lives.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-family: Impact;" +
            "-fx-text-alignment: right"
        );

        FXGL.getGameScene().addUINode(score);
        FXGL.getGameScene().addUINode(lives);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletOnEnemy(this.enemies));
        getPhysicsWorld().addCollisionHandler(new BulletOnShield());
        getPhysicsWorld().addCollisionHandler(new EnemyBulletOnShield());
        getPhysicsWorld().addCollisionHandler(new EnemyBulletOnPlayer());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("lives", 10);
    }
}
