package com.divae.lantern.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.divae.lantern.Bodies.PointActor;
import com.divae.lantern.Bodies.Rider;

import static com.badlogic.gdx.Gdx.graphics;

public class GameScreen implements Screen {

    private Game game;

    private Stage stage;

    private World world;

    private Box2DDebugRenderer debugRenderer;

    public GameScreen(Game game) {
        this.game = game;
        init();
    }

    private void init() {
        Gdx.input.setInputProcessor(stage);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        stage = new Stage(new ScreenViewport());
        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -1000), true);

        Rider rider = new Rider(world, normalizeX(300), normalizeY(900), 100);
        stage.addActor(rider);

        rider.addListener(onRiderTouched());
        rider.setTouchable(Touchable.enabled);

        for (int i = 0; i < 300; i = i + 3) {
            PointActor point = new PointActor(world, Color.WHITE, normalizeX(280 + i), normalizeY(700 - i), 3);
            stage.addActor(point);
            System.out.println("point added");
        }
    }

//    private final List<Body> pointBodies = new ArrayList<Body>();

    // TODO get resolution from cam
    private float normalizeX(float x) {
        float resolutionX = 1000;
        return (graphics.getWidth() / resolutionX) * x;
    }

    private float normalizeY(float y) {
        float resolutionY = 1000;
        return (graphics.getHeight() / resolutionY) * y;
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();
        stage.act();

        // if debug
//        debugRenderer.render(world, stage.getCamera().combined);
        world.step(graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private InputListener onRiderTouched() {
        return new InputListener() {
            public void clicked(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("X:" + x + " Y:" + y);
                //return true;
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("X:" + x + " Y:" + y);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touchup");
            }
        };
    }

}
