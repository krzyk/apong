package com.kirela.android.apong.scene;

import com.kirela.android.apong.GameActivity;
import com.kirela.android.apong.SceneType;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

public class LoadingScene extends BaseScene {
    @Override
    public void createScene() {
        setBackground(new Background(Color.WHITE));
        attachChild(new Text(GameActivity.MID_WIDTH, GameActivity.MID_HEIGHT, manager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed() {
    }

    @Override
    public SceneType getSceneType() {
        return SceneType.LOADING;
    }

    @Override
    public void disposeScene() {
    }
}
