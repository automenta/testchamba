package notreal.collision;

import com.codeforces.commons.geometry.Line2D;
import com.codeforces.commons.geometry.Point2D;
import notreal.Body;
import notreal.form.CircularGeom;
import notreal.form.RectangularGeom;
import notreal.form.Shape;
import notreal.util.GeometryUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * @author Maxim Shipko (sladethe@gmail.com)
 *         Date: 19.06.2015
 */
public class RectangleAndCircleCollider extends ColliderBase {
    public RectangleAndCircleCollider(double epsilon) {
        super(epsilon);
    }

    @Override
    protected boolean matchesOneWay(@NotNull Body bodyA, @NotNull Body bodyB) {
        return bodyA.geom().shape == Shape.RECTANGLE && bodyB.geom().shape == Shape.CIRCLE;
    }

    @SuppressWarnings("OverlyLongMethod")
    @Nullable
    @Override
    protected CollisionInfo collideOneWay(@NotNull Body bodyA, @NotNull Body bodyB) {
        RectangularGeom rectangularFormA = (RectangularGeom) bodyA.geom();
        CircularGeom circularFormB = (CircularGeom) bodyB.geom();

        Point2D[] pointsA = rectangularFormA.getPoints(bodyA.pos(), bodyA.angle(), epsilon);
        int pointACount = pointsA.length;

        if (!GeometryUtil.isPointOutsideConvexPolygon(bodyB.pos(), pointsA, epsilon)) {
            double minDistanceFromB = Double.POSITIVE_INFINITY;
            Line2D nearestLineA = null;

            for (int pointAIndex = 0; pointAIndex < pointACount; ++pointAIndex) {
                Point2D point1A = pointsA[pointAIndex];
                Point2D point2A = pointsA[pointAIndex == pointACount - 1 ? 0 : pointAIndex + 1];

                Line2D lineA = Line2D.getLineByTwoPoints(point1A, point2A);
                double distanceFromB = lineA.getDistanceFrom(bodyB.pos());

                if (distanceFromB < minDistanceFromB) {
                    minDistanceFromB = distanceFromB;
                    nearestLineA = lineA;
                }
            }

            if (nearestLineA != null) {
                return new CollisionInfo(
                        bodyA, bodyB, bodyB.pos(), nearestLineA.getUnitNormal().negate(),
                        circularFormB.getRadius() - nearestLineA.getSignedDistanceFrom(bodyB.pos()), epsilon
                );
            }
        }

        CollisionInfo collisionInfo = null;

        for (int pointAIndex = 0; pointAIndex < pointACount; ++pointAIndex) {
            Point2D point1A = pointsA[pointAIndex];
            Point2D point2A = pointsA[pointAIndex == pointACount - 1 ? 0 : pointAIndex + 1];

            CollisionInfo lineCollisionInfo = LineAndCircleCollider.collideOneWay(
                    bodyA, bodyB, point1A, point2A, circularFormB, epsilon
            );

            if (lineCollisionInfo != null
                    && (collisionInfo == null || lineCollisionInfo.getDepth() > collisionInfo.getDepth())) {
                collisionInfo = lineCollisionInfo;
            }
        }

        return collisionInfo;
    }
}
