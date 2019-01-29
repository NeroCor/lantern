package com.divae.lantern.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Rider extends Image {

    private TextureAtlas textureAtlas;

    private ParticleEffect effect;
    private Body body;
    private World world;

    private boolean hasEffect = true;

    private boolean autoRespawn = true;

    private final float startX;
    private float startY;

    private final float size;

    public Rider(World world, float startX, float startY, float size, boolean autoRespawn, boolean hasEffect) {
        super(new Texture("sleigh_white.png"));
        this.startX = startX;
        this.startY = startY;
        this.size = size;
        this.hasEffect = hasEffect;
        setSize(size, size);

        textureAtlas = new TextureAtlas();
        textureAtlas.addRegion("note",new TextureRegion(new Texture("note.png")));

        this.setPosition(startX, startY);

        this.world = world;

        if (hasEffect) {
            effect = new ParticleEffect();
            effect.load(Gdx.files.internal("bubleNote.p"), textureAtlas);
            effect.start();
            effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(startX, startY);

        // Create a body in the world using our definition
        body = this.world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 1f;
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (effect != null) {
            effect.draw(batch);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
        if (effect != null) {
            effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
            effect.update(delta);
        }

        if (autoRespawn) {
            if (getY() < 0) {
                Gdx.app.log("Rider","respawn");
                setPosition(startX, startY);
                if (effect != null) {
                    effect.setPosition(startX, startY);
                    effect.update(delta);
                }
                body.setTransform(startX, startY, 1);
                setX(startX);
                setY(startY);
            }
        }
    }

}
