package com.divae.lantern.Bodies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by julienvillegas on 01/02/2017.
 */

public class Point extends BodyDef {

    public Point(float posX, float posY, float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        type = BodyType.StaticBody;
//        bd.type = BodyType.DynamicBody;
        position.set(new Vector2(posX, posY));
    }

}
