package com.kirela.android.apong.scene;

import com.kirela.android.apong.GameActivity;
import com.kirela.android.apong.SceneManager;
import com.kirela.android.apong.SceneType;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

public class MainMenuScene extends BaseScene
    implements MenuScene.IOnMenuItemClickListener {

    private MenuScene child;
    private final static int PLAY = 0;
    private final static int OPTIONS = 1;

    @Override
    public void createScene() {
        createBackground();
        createChild();
    }

    @Override
    public void onBackKeyPressed() {
        System.exit(0);
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.MENU;
    }

    @Override
    public void disposeScene() {
    }

    public void createBackground() {
        final Sprite background = new Sprite(0, 0, manager.menuBackgroundRegion, vbom) {
            @Override
            protected void preDraw(GLState state, Camera camera) {
                super.preDraw(state, camera);
                state.enableDither();
            }
        };
        background.setPosition(GameActivity.MID_WIDTH, GameActivity.MID_HEIGHT);
        attachChild(background);
    }

    private void createChild() {
        child = new MenuScene(camera);
        child.setPosition(GameActivity.MID_WIDTH, GameActivity.MID_HEIGHT);
        final IMenuItem playItem = new ScaleMenuItemDecorator(new SpriteMenuItem(PLAY, manager.playRegion, vbom), 1.2f, 1);
        final IMenuItem optionsItem = new ScaleMenuItemDecorator(new SpriteMenuItem(OPTIONS, manager.optionsRegion, vbom), 1.2f, 1);
        child.addMenuItem(playItem);
        child.addMenuItem(optionsItem);
        child.buildAnimations();
        child.setBackgroundEnabled(false);
        playItem.setPosition(0, 10);
        optionsItem.setPosition(0, -110);
        child.setOnMenuItemClickListener(this);
        setChildScene(child);
    }

    @Override
    public boolean onMenuItemClicked(final MenuScene scene, final IMenuItem item, final float v, final float v2) {
        switch (item.getID()) {
            case PLAY:
                SceneManager.getInstance().loadGameScene(engine);
                return true;
            case OPTIONS: return true;
            default: return false;
        }
    }
}
