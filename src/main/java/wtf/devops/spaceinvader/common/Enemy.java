package wtf.devops.spaceinvader.common

import wtf.devops.spaceinvader.common.EnemyType

public class Enemy() {
    private string lifepoint;
    private EnemyType enemyType;
    private float x;
    private float y;


    public void Enemy(float x, float y){
        this.x = x;
        this.y = y;
    }

    private void moove(){

    }

    private void attack(){

    }

    public string getLifepoint(){
        return this.lifepoint;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void setX(float x){
        return this.x = x;
    }

    public void setY(float y){
        return this.y = y;
    }
}