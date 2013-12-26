package com.kirela.android.apong;

import android.view.KeyEvent;
import java.io.IOException;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class GameActivity extends BaseGameActivity {
    //public static final int WIDTH = 1184;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final int MID_WIDTH = WIDTH/2;
    public static final int MID_HEIGHT = HEIGHT/2;

    private BoundCamera camera;
    private ResourceManager mgr;

    @Override
    public Engine onCreateEngine(EngineOptions options) {
        return new LimitedFPSEngine(options, 60);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.camera = new BoundCamera(0, 0, WIDTH, HEIGHT);
        EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(WIDTH, HEIGHT), this.camera);
        options.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
        return options;
    }

    @Override
    public void onCreateResources(final OnCreateResourcesCallback callback) throws IOException {
        ResourceManager.prepare(mEngine, this, camera, getVertexBufferObjectManager());
        mgr = ResourceManager.getInstance();
        callback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(final OnCreateSceneCallback callback) throws IOException {
        SceneManager.getInstance().createSplash(callback);
    }

    @Override
    public void onPopulateScene(final Scene scene, final OnPopulateSceneCallback callback) throws IOException {
        mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler handler) {
                mEngine.unregisterUpdateHandler(handler);
                SceneManager.getInstance().createMenu();
            }
        }));
        callback.onPopulateSceneFinished();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent event) {
        if (code == KeyEvent.KEYCODE_BACK) {
            SceneManager.getInstance().getScene().onBackKeyPressed();
        }
        return false;
    }
}
