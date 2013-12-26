package com.kirela.android.apong.scene;

import com.kirela.android.apong.GameActivity;
import com.kirela.android.apong.SceneType;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class SplashScene extends BaseScene {
    private Sprite splash;

    @Override
    public void createScene() {
        splash = new Sprite(GameActivity.MID_WIDTH, GameActivity.MID_HEIGHT, manager.splashRegion, vbom) {
            @Override
            protected void preDraw(GLState state, Camera camera) {
                super.preDraw(state, camera);
                state.enableDither();
            }
        };
        splash.setScale(1.5f);
        attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.SPLASH;
    }

    @Override
    public void disposeScene() {
        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();
    }
}
