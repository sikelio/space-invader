package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Server;

public class SpaceInvaderServer extends GameApplication {
    private Server<Bundle> server;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) { }

    @Override
    protected void initGame() {
        this.server = FXGL.getNetService().newTCPServer(55555);
        this.server.startAsync();
        this.server.setOnConnected(connection -> {
            System.out.println("Connection");
            connection.addMessageHandlerFX((connection1, message) -> {
                System.out.println(message);
            });
        });
    }
}
