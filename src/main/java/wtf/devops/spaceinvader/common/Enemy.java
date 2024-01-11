package wtf.devops.spaceinvader.common;

public class Enemy {
    private String  lifepoint;
    
    private EnemyType enemyType;
    private float x;
    private float y;


    public Enemy(float x, float y){
        this.x = x;
        this.y = y;
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

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }
}