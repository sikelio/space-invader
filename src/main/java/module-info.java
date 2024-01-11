module wtf.devops.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens wtf.devops.spaceinvader to javafx.fxml;
    exports wtf.devops.spaceinvader;
}