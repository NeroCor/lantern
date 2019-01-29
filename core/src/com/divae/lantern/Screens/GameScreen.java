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
import com.divae.lantern.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Gdx.graphics;

public class GameScreen implements Screen {

    private Game game;

    private Stage stage;

    private World world;

    private Box2DDebugRenderer debugRenderer;

    private List<PointActor> points = new ArrayList<PointActor>();
//    private List<Body> pointBodies = new ArrayList<Body>();

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

        Rider rider = new Rider(world, normalizeX(300, true), normalizeY(900, false), 100, true, false);
        stage.addActor(rider);

        rider.addListener(onRiderTouched());
        rider.setTouchable(Touchable.enabled);

//        final int length = 300;
//        final int gap = 10;
//
//        Gdx.app.log("GameScreen","adding points...");
//        for (int i = 0; i < length; i = i + gap) {
//            PointActor point = new PointActor(world, Color.WHITE, normalizeX(280 + i), normalizeY(700 - i), 3, false);
//            stage.addActor(point);
//        }
//        Gdx.app.log("GameScreen","adding points... finished");
    }

    // TODO get resolution from cam
    private float normalizeX(float x, boolean invertAxis) {
        if (invertAxis) {
            return graphics.getHeight() - ((graphics.getWidth() / resolutionX) * x);
        }
        else {
            return (graphics.getWidth() / resolutionX) * x;
        }
    }

    private float normalizeY(float y, boolean invertAxis) {
        if (invertAxis) {
            return graphics.getHeight() - ((graphics.getHeight() / resolutionY) * y);
        }
        else {
            return (graphics.getHeight() / resolutionY) * y;
        }
    }

    private float resolutionX = 1000;
    private float resolutionY = 1000;

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();
        stage.act();

        // if debug
        debugRenderer.render(world, stage.getCamera().combined);
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

    private int maxPoints = 20;

    public void onExtracted(int width, int height, List<Pair<Integer, Integer>> coords) {
        Gdx.app.log("GameScreen","on extracted: points size " + coords.size());
        resolutionX = width;
        resolutionY = height;
        int pick = coords.size() / maxPoints;
        Gdx.app.log("GameScreen","adding points...every " + pick);
        clearPoints();

        for (int i = 0; i < coords.size(); i++) {
            if (pick > 0 && i % pick == 0) {
                Pair<Integer, Integer> coord = coords.get(i);
                PointActor point = new PointActor(world, Color.YELLOW, normalizeX(coord.getElement0(), false), normalizeY(coord.getElement1(), true), 3, false);
                points.add(point);
                stage.addActor(point);
            }
        }

        Gdx.app.log("GameScreen","adding points... finished");
    }

    private void clearPoints() {
        Gdx.app.log("GameScreen","clear points...");
        for (PointActor point : points) {
//                    point.addAction(Actions.removeActor());
            point.remove();
        }
        points.clear();
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
