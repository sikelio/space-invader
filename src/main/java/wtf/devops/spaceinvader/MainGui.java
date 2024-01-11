package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import wtf.devops.spaceinvader.common.*;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

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

    @Override
    protected void initGame() {
    	getGameWorld().addEntityFactory(new SpaceInvaderEntityFactory());
    	this.player = spawn("player");
       /* this.player = FXGL
                .entityBuilder()
                .at(287.5, 550)
                .view(new Rectangle(25, 25, Color.BLUE))
                .buildAndAttach(); */
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.RIGHT, () -> {
            if (this.player.getX() > 575) {
                return;
            }

            this.player.translateX(5);
        });

        FXGL.onKey(KeyCode.LEFT, () -> {
            if (this.player.getX() < 0) {
                return;
            }

            this.player.translateX(-5);
        });
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
