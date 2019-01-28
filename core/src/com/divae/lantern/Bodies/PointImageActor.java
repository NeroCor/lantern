package com.divae.lantern.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by julienvillegas on 01/02/2017.
 */

public class PointImageActor extends Image {

    private Body body;

    public PointImageActor(World world, float posX, float posY, float size, boolean isVisible) {
        super(new Texture(isVisible ? Gdx.files.internal("wood.jpg"): null));
        this.setSize(size, size);
        this.setOrigin(this.getWidth() / 2,this.getHeight() / 2);
        this.setPosition(posX, posY);
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX, posY));

        // Create a body from the defintion and add it to the world
        body = world.createBody(groundBodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // rotate
//        body.setTransform(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2, (float)Math.toRadians(angle));

        // Create a fixture from our polygon shape and add it to our ground body
        body.createFixture(fixtureDef);

        // Clean up after ourselves
        circle.dispose();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}

