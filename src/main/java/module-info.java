open module wtf.devops.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires annotations;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;

    exports wtf.devops.spaceinvader;
    exports wtf.devops.spaceinvader.common;
    exports wtf.devops.spaceinvader.components;
    exports wtf.devops.spaceinvader.custom;
    exports wtf.devops.spaceinvader.factories;
}