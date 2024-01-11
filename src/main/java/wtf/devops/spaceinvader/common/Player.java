package wtf.devops.spaceinvader.common;

public class Player {
    private String username;

    private int lifepoint;

    private int score;

    private float x;

    private float y;

    public Player(String username, int lifepoint) {
        this.username = username;
        this.lifepoint = lifepoint;
    }

    public void move() {}

    public void shoot() {}

    public String getUsername() {
        return username;
    }

    public int getLifepoint() {
        return this.lifepoint;
    }

    public int getScore() {
        return this.score;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return y;
    }

    public void setLifepoint(int lifepoint) {
        this.lifepoint = lifepoint;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
