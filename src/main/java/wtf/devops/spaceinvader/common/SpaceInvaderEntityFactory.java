package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;


import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import wtf.devops.spaceinvader.components.BulletComponent;
import wtf.devops.spaceinvader.components.OwnerComponent;
import wtf.devops.spaceinvader.components.PlayerComponent;

import static com.almasb.fxgl.dsl.FXGL.*;
import static wtf.devops.spaceinvader.common.EntityType.*;

public class SpaceInvaderEntityFactory implements EntityFactory {
	@Spawns("player")
	public Entity player(SpawnData data) {
		Texture texture = texture("player.png");
		texture.setPreserveRatio(true);
		texture.setFitHeight(40);

		return FXGL.entityBuilder(data)
				.at((double) getAppWidth() / 2 - 20, getAppHeight() - 40)
				.type(PLAYER)
				.viewWithBBox(texture)
				.with(new CollidableComponent(true))
				.with(new PlayerComponent())
				.with("dead", true)
				.buildAndAttach();
	}
	
	@Spawns("bullet")
	public Entity bullet(SpawnData data) {
		Entity owner = data.get("owner");

		return FXGL.entityBuilder(data)
				.type(BULLET)
				.at(owner.getCenter().add(-3, 20))
				.bbox(new HitBox(BoundingShape.box(9, 20)))
				.view(new Rectangle(10, 10, Color.BROWN))
				.with(new CollidableComponent(true), new OwnerComponent(owner.getType()))
				.with(new OffscreenCleanComponent(), new BulletComponent(850))
				.build();
	}
	
	@Spawns("enemy")
	public Entity enemy(SpawnData data) {
		return FXGL.entityBuilder(data)
					.type(ENEMY)
					.at(500,500)
					.view(new Rectangle(75, 75, Color.GREEN))
					.buildAndAttach();
	}
	
	@Spawns("shield")
	public Entity shield(SpawnData data) {
		Texture texture = texture("shield/fullShield.png");
		
		return FXGL.entityBuilder(data)
					.type(SHIELD)
					.at(data.<Integer>get("x"), 150)
					.scale(4, 4)
					.viewWithBBox(texture)
					.build();
	}
}
