package wtf.devops.spaceinvader.common;

public class Shield {
    private int lifepoint;

    private ShieldState state;

    public Shield (int lifepoint) {
        this.lifepoint = lifepoint;
        this.state = ShieldState.New;
    }

    public boolean isDestroyed() {
        return this.state == ShieldState.Destroyed;
    }

    public ShieldState takeDamage() {
        return this.state;
    }

    public int getLifepoint() {
        return lifepoint;
    }

    public ShieldState getState() {
        return state;
    }

    public void setLifepoint(int lifepoint) {
        this.lifepoint = lifepoint;
    }

    public void setState(ShieldState state) {
        this.state = state;
    }
}
