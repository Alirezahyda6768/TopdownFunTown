package com.bluebook.physics;

import com.bluebook.util.GameObject;
import com.bluebook.util.Vector2;
import com.sun.javafx.geom.Line2D;

import java.util.ArrayList;

public class RayCast {

    public Line2D ray;
    public float max_distance = 2000;
    public RayCastHit hit = null;
    public double angle;
    public GameObject source;


    public RayCast(double angle, GameObject source){
        HitDetectionHandler.getInstance().raycasts.add(this);
        this.source = source;
        this.angle = angle;
        updatePosition();
    }

    public void updatePosition(){
        ray = new Line2D((float)source.getPosition().getX(),
                (float)source.getPosition().getY(),
                (float) source.getPosition().getX() + (float)Math.cos(angle) * max_distance,
                (float)source.getPosition().getY() + (float)Math.sin(angle) * max_distance);
    }

    public void Cast(){
        float collisionDistance = max_distance;
        Collider colliderHit = null;
        for(Collider c : HitDetectionHandler.getInstance().colliders){
            if(c.getTag() == "DMG" || c.getTag() == "Block") {
                Line2D[] box = c.getLines();
                for (Line2D l : box) {
                    //float distanceHit = getRayCast(ray, l);
                    float distanceHit = getRayCast(ray.x1, ray.y1,
                            ray.x1 + (float)Math.cos(angle) * collisionDistance,
                            ray.y1 + (float)Math.sin(angle) * collisionDistance,
                            l.x1, l.y1, l.x2, l.y2);
                    //System.out.println("Testing collision on angle :  " + angle + " got distance " + distanceHit);
                    if (distanceHit > 0 && distanceHit < collisionDistance) {
                        //System.out.println("COllision found " + c.getName() + " DISTANCE " + distanceHit + " ANGLE: " + angle);
                        collisionDistance = distanceHit;
                        colliderHit = c;
                    }
                }
            }
        }
        RayCastHit ret = new RayCastHit();
        ret.colliderHit = colliderHit;
        ret.isHit = colliderHit != null;
        ret.ray = new Line2D(ray.x1, ray.y1, ray.x1 + (float) (Math.cos(angle) * collisionDistance), ray.y1 + (float) (Math.sin(angle) * collisionDistance));
        ret.originalRay = this.ray;
        this.hit = ret;
    }

    private float getRayCast(Line2D ray, Line2D collision){
        return getRayCast(ray.x1, ray.y1, ray.x2,  ray.y2, collision.x1, collision.y1,  collision.x2, collision.y2);
    }

    float dist(float LineStartX, float LineStartY, float LineEndX, float LineEndY)
    {
        return (float) Math.sqrt((LineEndX - LineStartX) * (LineEndX - LineStartX) + (LineEndY - LineStartY) * (LineEndY - LineStartY));
    }

    float getRayCast(float RayStartX, float RayStartY, float RayCosineDist, float RaySineDist, float LineStartX, float LineStartY, float LineEndX, float LineEndY) {
        float RayEndX, RayEndY, LineXDiff, LineYDiff;
        RayEndX = RayCosineDist - RayStartX;
        RayEndY = RaySineDist - RayStartY;
        LineXDiff = LineEndX - LineStartX;
        LineYDiff = LineEndY - LineStartY;
        float s, t;
        s = (-RayEndY * (RayStartX - LineStartX) + RayEndX * (RayStartY - LineStartY)) / (-LineXDiff * RayEndY + RayEndX * LineYDiff);
        t = (LineXDiff * (RayStartY - LineStartY) - LineYDiff * (RayStartX - LineStartX)) / (-LineXDiff * RayEndY + RayEndX * LineYDiff);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            float x = RayStartX + (t * RayEndX);
            float y = RayStartY + (t * RayEndY);

            return dist(RayStartX, RayStartY, x, y);
        }

        return -1; // No collision
    }

}
