package com.kirela.android.apong;

import com.kirela.android.apong.scene.BaseScene;
import com.kirela.android.apong.scene.GameScene;
import com.kirela.android.apong.scene.LoadingScene;
import com.kirela.android.apong.scene.MainMenuScene;
import com.kirela.android.apong.scene.SplashScene;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface;

public class SceneManager {
    private static final SceneManager INSTANCE = new SceneManager();

    private BaseScene splash;
    private BaseScene menu;
    private BaseScene loading;
    private BaseScene game;

    private SceneType type = SceneType.SPLASH;
    private BaseScene scene;
    private Engine engine = ResourceManager.getInstance().getEngine();

    public static SceneManager getInstance() {
        return INSTANCE;
    }

    public void setScene(BaseScene scene) {
        engine.setScene(scene);
        this.scene = scene;
        type = scene.getSceneType();
    }

    public void setSceneType(SceneType type) {
        switch (type) {
            case SPLASH:
                setScene(splash);
                break;
            case MENU:
                setScene(menu);
                break;
            case GAME:
                setScene(game);
                break;
            case LOADING:
                setScene(loading);
                break;
        }
    }

    public SceneType getType() {
        return type;
    }

    public BaseScene getScene() {
        return scene;
    }

    public void createSplash(IGameInterface.OnCreateSceneCallback callback) {
        ResourceManager.getInstance().loadSplash();
        splash = new SplashScene();
        scene = splash;
        callback.onCreateSceneFinished(scene);
    }

    public void disposeSplash() {
        ResourceManager.getInstance().unloadSplash();
        splash.dispose();
        splash = null;
    }

    public void createMenu() {
        ResourceManager.getInstance().loadMenuResources();
        menu = new MainMenuScene();
        loading = new LoadingScene();
        setScene(menu);
        disposeSplash();
    }

    public void loadGameScene(final Engine engine) {
        setScene(loading);
        ResourceManager.getInstance().unloadMenuTextures();
        engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler handler) {
                engine.unregisterUpdateHandler(handler);
                ResourceManager.getInstance().loadGameResources();
                game = new GameScene();
                setScene(game);
            }
        }));
    }

    public void loadMenuScene(final Engine engine) {
        setScene(loading);
        game.disposeScene();
        ResourceManager.getInstance().unloadGameTextures();
        engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler handler) {
                engine.unregisterUpdateHandler(handler);
                ResourceManager.getInstance().loadMenuResources();
                menu = new MainMenuScene();
                setScene(menu);
            }
        }));
    }
}
