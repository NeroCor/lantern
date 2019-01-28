package com.divae.lantern.Bodies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PointActor extends Actor {

    private ShapeRenderer sr;

    public PointActor(World world, Color color, float posX, float posY, float radius) {
        setSize(radius, radius);
        setColor(color);

        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);

        setPosition(posX, posY);
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(new Vector2(posX, posY));

        // Create a body from the defintion and add it to the world
        Body body = world.createBody(groundBodyDef);

        // Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create a fixture from our polygon shape and add it to our ground body
        body.createFixture(fixtureDef);

        // Clean up after ourselves
        circle.dispose();
        this.setOrigin(this.getWidth() / 2,this.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        Vector2 pos = new Vector2(getX(), getY());
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.circle(pos.x, pos.y, getHeight());
        sr.end();

//        Vector2 coords = new Vector2(getX(), getY());
//
//        Color color = new Color(getColor());
//        sr.setProjectionMatrix(batch.getProjectionMatrix());
//        sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//
//
//        sr.rectLine(coords.x,coords.y,coords.x + getWidth(), coords.y, getHeight());
//        sr.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
//        Gdx.gl.glLineWidth(1f);
//        sr.setColor(Color.WHITE);

        batch.begin();
    }

}
