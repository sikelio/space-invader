package wtf.devops.spaceinvader.common;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class Enemy extends GameApplication {
    private String  lifepoint;

    private float x;
    private float y;
    private EnemyType enemyType;

    public Enemy(float x, float y, EnemyType enemyType){
        this.x = x;
        this.y = y;
        this.enemyType = enemyType;
    }

    private void moove(){

    }

    private void attack(){

    }

    public String getLifepoint(){
        return this.lifepoint;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    @Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		
	}
}