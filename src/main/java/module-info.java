open module wtf.devops.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires annotations;

    exports wtf.devops.spaceinvader;
    exports wtf.devops.spaceinvader.common;
    exports wtf.devops.spaceinvader.components;
}