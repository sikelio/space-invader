package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;



import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpaceInvaderEntityFactory implements EntityFactory {
	@Spawns("player")
	public Entity player(SpawnData data) {
		return FXGL.entityBuilder(data)
				    .at(287.5, 550)
				    .view(new Rectangle(25, 25, Color.BLUE))
	                .buildAndAttach();
	}
	
	@Spawns("bullet")
	public Entity bullet(SpawnData data) {
		return FXGL.entityBuilder(data)
			    .at(287.5, 550)
			    .view(new Rectangle(10, 10, Color.WHITE))
                .buildAndAttach();
	}
	
	@Spawns("enemy")
	public Entity enemy(SpawnData data) {
		return FXGL.entityBuilder(data)
				.at(500,500)
				.view(new Rectangle(75, 75, Color.GREEN))
				.buildAndAttach();
	}
}
