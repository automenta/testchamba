package nars.testchamba.object;

import nars.testchamba.View;
import nars.testchamba.util.Animation;
import notreal.Body;
import notreal.collision.CollisionInfo;

/**
 * Created by me on 2/17/17.
 */
public class VisionRayAnimation implements Animation {

    private final float a;
    private final CollisionInfo result;
    private final float maxDistance;
    float opacity;
    double ang;
    double dist;
    double dx;
    double dy;

    float r;
    float g;

    public VisionRayAnimation(Body agent, float a, CollisionInfo result, float maxDistance) {
        this.a = a;
        this.result = result;
        this.maxDistance = maxDistance;
        opacity = 1f;
        ang = a;
        dist = result != null ? result.getPoint().getDistanceTo(agent.pos()) : maxDistance;
        dx = Math.cos(ang) * dist;
        dy = Math.sin(ang) * dist;
        r = result != null ? 0f : 200f;
        g = result != null ? 200f : 0f;
    }

    @Override
    public boolean draw(View v, double rt) {

        v.stroke(r, g, 10, 180 * opacity);
        opacity -= 0.1f;

        v.strokeWeight(0.1f);

        v.line(0, 0, (float) dx, (float) dy);
        v.noStroke();

        return (opacity > 0);
    }
}
