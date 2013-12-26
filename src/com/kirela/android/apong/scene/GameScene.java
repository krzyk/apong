package com.kirela.android.apong.scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.kirela.android.apong.GameActivity;
import com.kirela.android.apong.LevelCompleteWindow;
import com.kirela.android.apong.Player;
import com.kirela.android.apong.SceneManager;
import com.kirela.android.apong.SceneType;
import java.io.IOException;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

public class GameScene extends BaseScene implements IOnSceneTouchListener {
    private LevelCompleteWindow levelCompleteWindow;
    private HUD hud;
    private PhysicsWorld physicsWorld;

    private Text scoreText;
    private int score;
    private Text gameOverText;
    private boolean gameOverDisplayed = false;
    private Player player;
    private boolean firstTouch = false;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        createPhysics();
        loadLevel(1);
        createGameOverText();
        setOnSceneTouchListener(this);
        levelCompleteWindow = new LevelCompleteWindow(vbom);
    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setCenter(GameActivity.MID_WIDTH, GameActivity.MID_HEIGHT);
        camera.setChaseEntity(null);
    }

    private void createBackground() {
        setBackground(new Background(Color.BLUE));
    }

    private void createHUD() {
        hud = new HUD();
        scoreText = new Text(20, 420, manager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
        scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        hud.attachChild(scoreText);
        camera.setHUD(hud);
    }

    private void addToScore(int sum) {
        score += sum;
        scoreText.setText("Score: " + score);
    }

    private void createPhysics() {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }

    private void loadLevel(int level) {
        final SimpleLevelLoader loader = new SimpleLevelLoader(vbom);
        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);
        loader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
            @Override
            public IEntity onLoadEntity(final String name, final IEntity parent, final Attributes attributes, final SimpleLevelEntityLoaderData data) throws IOException {
                int width = SAXUtils.getIntAttributeOrThrow(attributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                int height = SAXUtils.getIntAttributeOrThrow(attributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                camera.setBounds(0, 0,  width, height);
                camera.setBoundsEnabled(true);
                return GameScene.this;
            }
        });
        loader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>("entity") {
            @Override
            public IEntity onLoadEntity(final String name, final IEntity parent, final Attributes attributes, final SimpleLevelEntityLoaderData data) throws IOException {
                int x = SAXUtils.getIntAttributeOrThrow(attributes, "x");
                int y = SAXUtils.getIntAttributeOrThrow(attributes, "y");
                String type = SAXUtils.getAttributeOrThrow(attributes, "type");
                final Sprite levelObject;
                if (type.equals("platform1")) {
                    levelObject = new Sprite(x, y, manager.platform1Region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, fixtureDef).setUserData("platform1");
                } else if (type.equals("platform2")) {
                    levelObject = new Sprite(x, y, manager.platform2Region, vbom);
                    Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, fixtureDef);
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals("platform3")) {
                    levelObject = new Sprite(x, y, manager.platform3Region, vbom);
                    Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, fixtureDef);
                    body.setUserData("platform3");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                } else if (type.equals("coin")) {
                    levelObject = new Sprite(x, y, manager.coinRegion, vbom) {
                        @Override
                        protected void onManagedUpdate(float seconds) {
                            super.onManagedUpdate(seconds);
                            if (player.collidesWith(this)) {
                                addToScore(10);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                } else if (type.equals("player")) {
                    player = new Player(x, y, vbom, camera, physicsWorld) {
                        @Override
                        public void onDie() {
                            if (!gameOverDisplayed) {
                                displayGameOver();
                            }
                        }
                    };
                    levelObject = player;
                } else if (type.equals("levelComplete")) {
                    levelObject = new Sprite(x, y, manager.completeStarsRegion, vbom) {
                        @Override
                        protected void onManagedUpdate(float seconds) {
                            super.onManagedUpdate(seconds);
                            if (player.collidesWith(this)) {
                                levelCompleteWindow.display(LevelCompleteWindow.StarsCount.TWO, GameScene.this, camera);
                                setVisible(true);
                                setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                } else {
                    throw new IllegalArgumentException();
                }
                levelObject.setCullingEnabled(true);
                return levelObject;
            }
        });
        loader.loadLevelFromAsset(activity.getAssets(), "level/" + level + ".lvl");
    }

    @Override
    public boolean onSceneTouchEvent(final Scene scene, final TouchEvent event) {
        if (event.isActionDown()) {
            if (!firstTouch) {
                player.setRunning();
                firstTouch = true;
            } else {
                player.jump();
            }
        }
        return false;
    }

    private void createGameOverText() {
        gameOverText = new Text(0, 0, manager.font, "Game Over", vbom);
    }

    private void displayGameOver() {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }

    private boolean nonNull(Fixture... fixtures) {
        for (Fixture fix : fixtures) {
            if (fix.getBody().getUserData() == null) {
                return false;
            }
        }
        return true;
    }

    private boolean contact(String name, Fixture... fixtures) {
        for (Fixture fix : fixtures) {
            if (fix.getBody().getUserData().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Fixture contact(String first, String second, Fixture... fixtures) {
        Fixture found = null;
        boolean secondFound = false;
        for (Fixture fix : fixtures) {
            if (fix.getBody().getUserData().equals(first)) {
                found = fix;
            }
            if (fix.getBody().getUserData().equals(second)) {
                secondFound = true;
            }
        }
        if (secondFound && found != null) {
            return found;
        }
        return null;
    }

    private ContactListener contactListener() {
        return new ContactListener() {
            @Override
            public void beginContact(final Contact contact) {
                final Fixture a = contact.getFixtureA();
                final Fixture b = contact.getFixtureB();
                if (nonNull(a, b)) {
                    if (contact("player", a, b)) {
                        player.incFootContacts();
                    }
                    final Fixture platform3 = contact("platform3", "player", a, b);
                    if (platform3 != null) {
                        platform3.getBody().setType(BodyDef.BodyType.DynamicBody);
                    }
                    final Fixture platform2 = contact("platform2", "player", a, b);
                    if (platform2 != null) {
                        engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
                            @Override
                            public void onTimePassed(
                                final TimerHandler handler) {
                                handler.reset();
                                unregisterUpdateHandler(handler);
                                platform2.getBody().setType(BodyDef.BodyType.DynamicBody);
                            }
                        }));
                    }
                }
            }

            @Override
            public void endContact(final Contact contact) {
                final Fixture a = contact.getFixtureA();
                final Fixture b = contact.getFixtureB();
                if (nonNull(a, b)) {
                    if (contact("player", a, b)) {
                        player.decFootContacts();
                    }
                }
            }

            @Override
            public void preSolve(final Contact contact, final Manifold manifold) {
            }

            @Override
            public void postSolve(final Contact contact, final ContactImpulse impulse) {
            }
        };
    }
}
