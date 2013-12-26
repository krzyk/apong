package com.kirela.android.apong;

import android.graphics.Color;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

public class ResourceManager {

    public ITextureRegion splashRegion;
    public ITextureRegion menuBackgroundRegion;
    public ITextureRegion playRegion;
    public ITextureRegion optionsRegion;
    public ITiledTextureRegion playerRegion;

    public Font font;

    private static final ResourceManager INSTANCE = new ResourceManager();


    private Engine engine;
    private GameActivity activity;
    private BoundCamera camera;
    private VertexBufferObjectManager buffer;

    private BitmapTextureAtlas splashAtlas;
    private BuildableBitmapTextureAtlas menuTextureAtlas;

    public ITextureRegion platform1Region;
    public ITextureRegion platform2Region;
    public ITextureRegion platform3Region;
    public ITextureRegion coinRegion;
    public ITextureRegion completeWindowRegion;
    public ITiledTextureRegion completeStarsRegion;

    private BuildableBitmapTextureAtlas gameTextureAtlas;

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public static void prepare(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager buffer) {
        INSTANCE.engine = engine;
        INSTANCE.activity = activity;
        INSTANCE.camera = camera;
        INSTANCE.buffer = buffer;
    }

    public void loadSplash() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashAtlas, activity, "splash.png", 0, 0);
        splashAtlas.load();
    }

    public void unloadSplash() {
        splashAtlas.unload();
        splashRegion = null;
    }

    public void loadMenuGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "background.png");
        playRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        optionsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
        try {
            menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            menuTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            Debug.e(e);
            throw new IllegalStateException(e);
        }
    }

    public void loadMenuFonts() {
        FontFactory.setAssetBasePath("font/");
        final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }

    public Engine getEngine() {
        return engine;
    }

    public GameActivity getActivity() {
        return activity;
    }

    public BoundCamera getCamera() {
        return camera;
    }

    public VertexBufferObjectManager getBuffer() {
        return buffer;
    }

    public void loadMenuResources() {
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }

    private void loadMenuAudio() {

    }

    public void unloadMenuTextures() {
        menuTextureAtlas.unload();
    }

    private void loadMenuTextures() {
        menuTextureAtlas.load();
    }

    public void loadGameResources() {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadGameAudio() {

    }

    private void loadGameFonts() {

    }

    private void loadGameGraphics() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
        gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
        platform1Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
        platform2Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
        platform3Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform3.png");
        coinRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "coin.png");
        playerRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);

        completeWindowRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "complete.png");
        completeStarsRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "stars.png", 2, 1);

        try {
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            gameTextureAtlas.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            Debug.e(e);
            throw new IllegalStateException(e);
        }
    }

    public void unloadGameTextures() {

    }
}
