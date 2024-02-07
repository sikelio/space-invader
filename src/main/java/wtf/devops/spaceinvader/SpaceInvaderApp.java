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
import com.mongodb.MongoException;
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

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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
    private Boolean isServer = false;
    private MongoDatabase database;

    private MongoCollection<Document> playerCollection;

    @Override
    protected void initGame() {
        String uri = "mongodb://localhost:27017";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("space-invader");
            database.runCommand(new Document("ping", 1));
        } catch(MongoException e) {
            e.printStackTrace();
        }

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

        player1 = spawn("player");
        player1.setProperty("playerID", 1);
        getService(MultiplayerService.class).spawn(connection, player1, "player");

        player2 = spawn("player");
        player2.setProperty("playerID", 2);
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
        Image backgroundImage = FXGL.image("background/background.png");
        FXGL.getGameScene().setBackgroundRepeat(backgroundImage);

        getService(MultiplayerService.class).addEntityReplicationReceiver(connection, getGameWorld());
        getService(MultiplayerService.class).addInputReplicationSender(connection, getInput());
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, () -> {
            if (player1 != null && player1.hasComponent(PlayerComponent.class)) {
                player1.getComponent(PlayerComponent.class).left();
            }
        });
        onKey(KeyCode.RIGHT, () -> {
            if (player1 != null && player1.hasComponent(PlayerComponent.class)) {
                player1.getComponent(PlayerComponent.class).right();
            }
        });
        onKey(KeyCode.SPACE, () -> {
            if (player1 != null && player1.hasComponent(PlayerComponent.class)) {
                player1.getComponent(PlayerComponent.class).shoot();
            }
        });

        clientInput = new Input();

        onKeyBuilder(clientInput, KeyCode.LEFT).onAction(() -> player2.getComponent(PlayerComponent.class).left());
        onKeyBuilder(clientInput, KeyCode.RIGHT).onAction(() -> player2.getComponent(PlayerComponent.class).right());
        onKeyBuilder(clientInput, KeyCode.SPACE).onAction(() -> player2.getComponent(PlayerComponent.class).shoot());
    }

    @Override
    protected void initUI() {
        Text scorePlayer1 = new Text();
        Text scorePlayer2 = new Text();
        Text livesPlayer1 = new Text();
        Text livesPlayer2 = new Text();

        this.setupScoreText(scorePlayer1, 25, 525, "scorePlayer1");
        this.setupScoreText(scorePlayer2, 25, 575, "scorePlayer2");
        this.setupLivesText(livesPlayer1, 560, 525, "livesPlayer1");
        this.setupLivesText(livesPlayer2, 560, 575, "livesPlayer2");
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
        vars.put("scorePlayer1", 0);
        vars.put("scorePlayer2", 0);

        vars.put("livesPlayer1", 10);
        vars.put("livesPlayer2", 10);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (this.isServer) {
            this.clientInput.update(tpf);
        }
    }

    private void setupLivesText(Text text, int x, int y, String property) {
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.textProperty().bind(FXGL.getWorldProperties().intProperty(property).asString());
        text.setFill(Color.rgb(255, 231, 68));
        text.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-family: Impact;" +
            "-fx-text-alignment: right"
        );

        FXGL.getGameScene().addUINode(text);
    }

    private void setupScoreText(Text text, int x, int y, String property) {
        text.setTranslateX(x);
        text.setTranslateY(y);
        text.textProperty().bind(FXGL.getWorldProperties().intProperty(property).asString());
        text.setFill(Color.rgb(255, 231, 68));
        text.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-fAont-family: Impact;"
        );

        FXGL.getGameScene().addUINode(text);
    }
}
