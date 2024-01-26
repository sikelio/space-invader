package wtf.devops.spaceinvader.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import wtf.devops.spaceinvader.common.ShieldState;

public class ShieldComponent extends Component {
    private ShieldState state;

    private int lifepoint;

    public ShieldComponent() {
        this.lifepoint = 1000;
        this.state = ShieldState.New;
    }

    public void updateTexture() {
        String textureName = switch (this.getState()) {
            case New -> "shield/fullShield.png";
            case SlightlyDamaged -> "shield/semiShield.png";
            case Damaged, Destroyed -> "shield/almostDestroyedShield.png";
            default -> throw new IllegalStateException("Unexpected ShieldState: " + state);
        };

        Texture texture = FXGL.texture(textureName);
        texture.setScaleX(1);
        texture.setScaleY(1);

        entity.getViewComponent().clearChildren();
        entity.getViewComponent().addChild(texture);
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
