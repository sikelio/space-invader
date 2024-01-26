package wtf.devops.spaceinvader;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class SpaceInvaderSceneFactory extends SceneFactory {
    @Override
    public FXGLMenu newMainMenu() {
        return new SpaceInvaderMainMenu(MenuType.MAIN_MENU);
    }
}
