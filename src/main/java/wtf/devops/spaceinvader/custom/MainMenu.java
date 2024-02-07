package wtf.devops.spaceinvader.custom;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getDialogService;

public class MainMenu extends FXGLMenu {
    private final int buttonWidth = 200;
    private final int buttonHeight = 40;


    public MainMenu(@NotNull MenuType type) {
        super(type);

        Image background = FXGL.image("background/background.png");
        ImageView backgroundView = new ImageView(background);
        backgroundView.setFitWidth(FXGL.getAppWidth());
        backgroundView.setFitHeight(FXGL.getAppHeight());

        Image logo = FXGL.image("logo.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(FXGL.getAppWidth() - 100);
        logoView.setFitHeight((FXGL.getAppWidth() - 100) * logo.getHeight() / logo.getWidth());
        logoView.setTranslateX((double) (FXGL.getAppWidth() - (FXGL.getAppWidth() - 100)) / 2);
        logoView.setTranslateY(12.5);

        Button startButton = new Button("Start new game");
        startButton.setOnAction(e -> fireNewGame());
        startButton.setTranslateX((double) (FXGL.getAppWidth() - this.buttonWidth) / 2);
        startButton.setTranslateY(300);

        Button leaderboard = new Button("Leaderboard");
        leaderboard.setOnAction(e -> getDialogService().showMessageBox("Leaderboard", () -> {
            System.out.println("You typed");
        }));
        leaderboard.setTranslateX((double) (FXGL.getAppWidth() - this.buttonWidth) / 2);
        leaderboard.setTranslateY(400);

        Button quitButton = new Button("Quit game");
        quitButton.setOnAction(e -> fireExit());
        quitButton.setTranslateX((double) (FXGL.getAppWidth() - this.buttonWidth) / 2);
        quitButton.setTranslateY(500);

        this.styleButton(startButton);
        this.styleButton(leaderboard);
        this.styleButton(quitButton);

        getContentRoot().getChildren().addAll(backgroundView, logoView, startButton, leaderboard, quitButton);
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-font-size: 20px;" +
                        "-fx-background-color: #FFE744;" +
                        "-fx-text-fill: #000000;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-font-family: Impact;" +
                        "-fx-padding: 10px;");
        button.setPrefWidth(this.buttonWidth);
        button.setPrefHeight(this.buttonHeight);
        button.setOnMouseEntered(e -> {
            button.setCursor(Cursor.HAND);
            button.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-background-color: #FFEE77;" +
                "-fx-text-fill: #000000;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-family: Impact;" +
                "-fx-padding: 10px;"
            );
        });

        button.setOnMouseExited(e -> {
            button.setCursor(Cursor.DEFAULT);
            button.setStyle(
                "-fx-font-size: 20px;" +
                "-fx-background-color: #FFE744;" +
                "-fx-text-fill: #000000;" +
                "-fx-background-radius: 10px;" +
                "-fx-font-family: Impact;" +
                "-fx-padding: 10px;"
            );
        });
    }
}
