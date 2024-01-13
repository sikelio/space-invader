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

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;
import java.util.Stack;

public class MainGui extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Space Invader");
        gameSettings.setVersion("1.0.0");
    }

    private Entity player;
    private Stack<Entity> enemy;

    @Override
    protected void initGame() {
    	getGameWorld().addEntityFactory(new SpaceInvaderEntityFactory());
        this.player = getGameWorld().spawn("player");

        this.enemy = new Stack<Entity>();
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 560).put("Y", 525)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 518).put("Y", 550)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 469).put("Y", 500)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 421).put("Y", 450)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 372).put("Y", 400)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 324).put("Y", 350)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 276).put("Y", 300)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 227).put("Y", 250)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 179).put("Y", 200)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 130).put("Y", 150)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 82).put("Y", 100)));
        enemy.push(getGameWorld().spawn("enemy", new SpawnData(0,0).put("X", 34).put("Y", 50)));
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
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }
}
