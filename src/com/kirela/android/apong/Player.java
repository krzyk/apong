package com.kirela.android.apong;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class Player extends AnimatedSprite {
    private Body body;
    private boolean canRun = false;
    private int footContacts = 0;

    public Player(float x, float y, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
        super(x, y, ResourceManager.getInstance().playerRegion, vbom);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }

    public abstract void onDie();

    private void createPhysics(final Camera camera, final PhysicsWorld physicsWorld) {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setUserData("player");
        body.setFixedRotation(true);
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
            @Override
            public void onUpdate(final float seconds) {
                super.onUpdate(seconds);
                camera.onUpdate(0.1f);
                if (getY() <= 0) {
                    onDie();
                }
                if (canRun) {
                    body.setLinearVelocity(new Vector2(3, body.getLinearVelocity().y));
                }
            }
        });
    }

    public void setRunning() {
        canRun = true;
        final long[] playerAnimate = new long[] {100, 100, 100};
        animate(playerAnimate, 0, 2, true);
    }

    public void jump() {
        if (footContacts > 0) {
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
        }
    }

    public void incFootContacts() {
        footContacts++;
    }

    public void decFootContacts() {
        footContacts--;
    }
}
