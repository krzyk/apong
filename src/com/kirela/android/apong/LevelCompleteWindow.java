package com.kirela.android.apong;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class LevelCompleteWindow extends Sprite {
    private TiledSprite star1;
    private TiledSprite star2;
    private TiledSprite star3;

    public enum StarsCount {
        ONE, TWO, THREE
    }

    public LevelCompleteWindow(final VertexBufferObjectManager vbom) {
        super(0, 0, 650, 400, ResourceManager.getInstance().completeWindowRegion, vbom);
        attachStars(vbom);
    }

    private void attachStars(final VertexBufferObjectManager vbom) {
        star1 = new TiledSprite(150, 150, ResourceManager.getInstance().completeStarsRegion, vbom);
        star2 = new TiledSprite(325, 150, ResourceManager.getInstance().completeStarsRegion, vbom);
        star3 = new TiledSprite(500, 150, ResourceManager.getInstance().completeStarsRegion, vbom);
        attachChild(star1);
        attachChild(star2);
        attachChild(star3);
    }

    public void display(StarsCount count, Scene scene, Camera camera) {
        switch (count) {
            case ONE:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(1);
                star3.setCurrentTileIndex(1);
                break;
            case TWO:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(1);
                break;
            case THREE:
                star1.setCurrentTileIndex(0);
                star2.setCurrentTileIndex(0);
                star3.setCurrentTileIndex(0);
                break;
        }
        camera.getHUD().setVisible(false);
        camera.setChaseEntity(null);
        setPosition(camera.getCenterX(), camera.getCenterY());
        scene.attachChild(this);
    }
}
