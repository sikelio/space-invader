package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.net.Client;
import com.almasb.fxgl.net.Connection;
import com.almasb.fxgl.multiplayer.MultiplayerService;
import com.almasb.fxgl.net.Server;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import wtf.devops.spaceinvader.collision.BulletOnEnemy;
import wtf.devops.spaceinvader.collision.BulletOnShield;
import wtf.devops.spaceinvader.collision.EnemyBulletOnPlayer;
import wtf.devops.spaceinvader.collision.EnemyBulletOnShield;
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
        gameSettings.addEngineService(MultiplayerService.class);
    }

    private Entity player1;
    private Entity player2;
    private HashSet<Entity> enemies;
    private Stack<Entity> shields;
    private Client<Bundle> client;
    private Input clientInput;
    private Connection<Bundle> connection;

    private Boolean isServer;

    @Override
    protected void initGame() {
        runOnce(() -> {
            getDialogService().showConfirmationBox("Est tu le serveur ?", yes -> {
                isServer = yes;

                getGameWorld().addEntityFactory(new SpaceInvaderEntityFactory());

                if (yes) {
                    Server<Bundle> server = getNetService().newTCPServer(55555);
                    server.setOnConnected(conn -> {
                        connection = conn;

                        FXGL.set("conn", connection);

                        getExecutor().startAsyncFX(() -> onServer());
                    });

                    server.startAsync();
                } else {
                    Client<Bundle> client = getNetService().newTCPClient("localhost", 55555);
                    client.setOnConnected(conn -> {
                        connection = conn;

                        getExecutor().startAsyncFX(() -> onClient());
                    });

                    client.connectAsync();
                }
            });
        }, Duration.seconds(0.5));
    }

    private void onServer() {
        Image backgroundImage = FXGL.image("background/background.png");
        FXGL.getGameScene().setBackgroundRepeat(backgroundImage);

        player1 = getGameWorld().spawn("player");
        getService(MultiplayerService.class).spawn(connection, player1, "player");

        player2 = getGameWorld().spawn("player");
        getService(MultiplayerService.class).spawn(connection, player2, "player");

        Wave enemiesWave = new Wave();
        this.enemies = enemiesWave.generateWave();
        enemiesWave.moveEnemies(this.enemies);

        this.shields = new Stack<>();

        var shield = spawn("shield", new SpawnData(50, 1));
        getService(MultiplayerService.class).spawn(connection, shield, "shield");
        this.shields.push(shield);

        shield = spawn("shield", new SpawnData(200, 1));
        getService(MultiplayerService.class).spawn(connection, shield, "shield");
        this.shields.push(shield);

        shield = spawn("shield", new SpawnData(375, 1));
        getService(MultiplayerService.class).spawn(connection, shield, "shield");
        this.shields.push(shield);

        shield = spawn("shield", new SpawnData(525, 1));
        getService(MultiplayerService.class).spawn(connection, shield, "shield");
        this.shields.push(shield);

        getService(MultiplayerService.class).addInputReplicationReceiver(connection, clientInput);
    }

    private void onClient() {
        //this.player2 = new Entity();

        Image backgroundImage = FXGL.image("background/background.png");
        FXGL.getGameScene().setBackgroundRepeat(backgroundImage);

        getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
        getService(MultiplayerService.class).addInputReplicationSender(connection, getInput());
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, () -> this.player1.getComponent(PlayerComponent.class).left());
        onKey(KeyCode.RIGHT, () -> this.player1.getComponent(PlayerComponent.class).right());
        onKey(KeyCode.SPACE, () -> this.player1.getComponent(PlayerComponent.class).shoot());

        clientInput = new Input();

        onKeyBuilder(clientInput, KeyCode.LEFT).onAction(() -> this.player2.getComponent(PlayerComponent.class).left());
        onKeyBuilder(clientInput, KeyCode.RIGHT).onAction(() -> this.player2.getComponent(PlayerComponent.class).right());
        onKeyBuilder(clientInput, KeyCode.SPACE).onAction(() -> this.player2.getComponent(PlayerComponent.class).shoot());
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
        getPhysicsWorld().addCollisionHandler(new BulletOnEnemy());
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
