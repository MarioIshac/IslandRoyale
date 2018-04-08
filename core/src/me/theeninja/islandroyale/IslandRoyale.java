package me.theeninja.islandroyale;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.theeninja.islandroyale.gui.SpriteSheet;

import static me.theeninja.islandroyale.IslandTileType.*;

public class IslandRoyale extends ApplicationAdapter {
    SpriteSheet islandSpriteSheet;

    SpriteBatch batch;

    TextureRegion dirt;
    Island island;

	@Override
	public void create() {
		batch = new SpriteBatch();
		islandSpriteSheet = new SpriteSheet("island_sheet.xml");

        dirt = islandSpriteSheet.getSpriteRegion(DIRT.name());

        island = new Island(11, 11);

        dirt.setRegionWidth(dirt.getRegionWidth() / 4);
        dirt.setRegionHeight(dirt.getRegionHeight() / 4);
    }

    private static final int MAP_EDGE = 40;

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		final int islandPixelWidth = dirt.getRegionWidth() * island.getMaxWidth();
		final int islandPixelHeight = dirt.getRegionHeight() * island.getMaxHeight();

		for (int bottomCornerX = MAP_EDGE; bottomCornerX < 1280 - MAP_EDGE; bottomCornerX += islandPixelWidth) {
		    for (int bottomCornerY = MAP_EDGE; bottomCornerY < 720 - MAP_EDGE; bottomCornerY += islandPixelHeight) {
                for (int currentX = 0; currentX < island.getMaxWidth(); currentX++) {
                    for (int currentY = 0; currentY < island.getMaxHeight(); currentY++) {
                        IslandTileType islandTileType = island.getRepr()[currentX][currentY];

                        if (islandTileType != null) {
                            batch.draw(dirt, bottomCornerX + currentX * dirt.getRegionWidth(), bottomCornerY + currentY * dirt.getRegionHeight());
                        }
                    }
                }
            }
        }

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
