package com.kirela.android.apong.scene;

import android.app.Activity;
import com.kirela.android.apong.ResourceManager;
import com.kirela.android.apong.SceneType;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class BaseScene extends Scene {
    protected Engine engine;
    protected Activity activity;
    protected ResourceManager manager;
    protected VertexBufferObjectManager vbom;
    protected BoundCamera camera;

    public BaseScene() {
        this.manager = ResourceManager.getInstance();
        this.engine = manager.getEngine();
        this.activity = manager.getActivity();
        this.vbom = manager.getBuffer();
        this.camera = manager.getCamera();
        createScene();
    }

    public abstract void createScene();
    public abstract void onBackKeyPressed();
    public abstract SceneType getSceneType();
    public abstract void disposeScene();
}
