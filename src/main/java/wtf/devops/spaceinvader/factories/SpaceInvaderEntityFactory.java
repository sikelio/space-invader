package wtf.devops.spaceinvader.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;


import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.multiplayer.NetworkComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import wtf.devops.spaceinvader.common.EnemyType;
import wtf.devops.spaceinvader.components.*;

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
				.with(new PlayerComponent(10))
				.with(new NetworkComponent())
				.build();
	}

	@Spawns("bullet")
	public Entity bullet(SpawnData data) {
		Entity owner = data.get("owner");

		return FXGL.entityBuilder(data)
				.type(BULLET)
				.at(owner.getCenter().add(-3, 20))
				.bbox(new HitBox(BoundingShape.box(9, 20)))
				.view(new Rectangle(10, 10, Color.BROWN))
				.with(new OwnerComponent(owner.getType()))
				.with(new CollidableComponent(true))
				.with(new OffscreenCleanComponent())
				.with(new BulletComponent(850))
				.with(new NetworkComponent())
				.build();
	}

	@Spawns("enemy_bullet")
	public Entity enemyBullet(SpawnData data) {
		Entity owner = data.get("owner");

		return  FXGL.entityBuilder(data)
				.type(ENEMY_BULLET)
				.at(owner.getCenter().add(-3, 20))
				.bbox(new HitBox(BoundingShape.box(9, 20)))
				.view(new Rectangle(10, 10, Color.BROWN))
				.with(new OwnerComponent(owner.getType()))
				.with(new CollidableComponent(true))
				.with(new OffscreenCleanComponent())
				.with(new EnemyBulletComponent(850))
				.with(new NetworkComponent())
				.build();
	}

	@Spawns("enemy")
	public Entity enemy(SpawnData data) {
		//EnemyType enemyType = data.get("enemyType");
		Double positionX = data.getX();
		Double positionY = data.getY();
		EnemyType enemyType = EnemyType.randomValue();
		Texture texture;

		switch (enemyType) {
			case CRAB -> texture = texture("aliens/crab.png");
			case SQUID -> texture = texture("aliens/squid.png");
			case OCTOPUS -> texture = texture("aliens/octopus.png");
			default -> texture = texture("aliens/crab.png");
		}

		texture.outline(Color.BLACK);

		return FXGL.entityBuilder(data)
				.at(positionX,getAppHeight() - (double) positionY - 40)
				.type(ENEMY)
				.viewWithBBox(texture)
				.scale(4,4)
				.with(new CollidableComponent(true))
				.with(new EnemyComponent())
				.with(new NetworkComponent())
				.build();
	}

	@Spawns("shield")
	public Entity shield(SpawnData data) {
		Texture texture = texture("shield/fullShield.png");

		return FXGL.entityBuilder(data)
				.type(SHIELD)
				.at(data.getX(), getAppHeight() - 175)
				.scale(3, 3)
				.viewWithBBox(texture)
				.with(new CollidableComponent(true))
				.with(new ShieldComponent())
				.with(new NetworkComponent())
				.build();
	}

	@Spawns("opponentView")
	public Entity opponentView(SpawnData data) {
		// Utilisez un grand rectangle comme placeholder
		return FXGL.entityBuilder(data)
				.view(new Rectangle(300, 300, Color.GRAY))
				.at(300, 0) // Ajustez selon la mise en page
				.build();
	}
}
