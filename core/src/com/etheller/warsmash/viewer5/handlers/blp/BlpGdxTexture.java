package com.etheller.warsmash.viewer5.handlers.blp;

import java.io.InputStream;
import com.badlogic.gdx.graphics.Texture;
import com.etheller.warsmash.datasources.DataSource;
import com.etheller.warsmash.util.DataSourceFileHandle;
import com.etheller.warsmash.viewer5.GdxTextureResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.PathSolver;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;

public class BlpGdxTexture extends GdxTextureResource {

	public BlpGdxTexture(final ModelViewer viewer, final ResourceHandler handler, final String extension,
						 final PathSolver pathSolver, final String fetchUrl) {
		super(viewer, handler, extension, pathSolver, fetchUrl);
	}

	@Override
	protected void lateLoad() {

	}

	@Override
	protected void load(final InputStream src, final Object options) {
		try {
			DataSource dataSource = (DataSource) options;
			if(!dataSource.has(fetchUrl)) {
				throw new RuntimeException("No such fetchURL: " + fetchUrl);
			}
			Texture myTexture = new Texture(new DataSourceFileHandle(dataSource, fetchUrl));
			myTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			setGdxTexture(myTexture);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
