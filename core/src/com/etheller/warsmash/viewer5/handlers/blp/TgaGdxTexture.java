package com.etheller.warsmash.viewer5.handlers.blp;

import com.etheller.warsmash.datasources.DataSource;
import com.etheller.warsmash.util.ImageUtils;
import com.etheller.warsmash.viewer5.GdxTextureResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.PathSolver;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;
import com.etheller.warsmash.viewer5.handlers.tga.TgaFile;

import java.io.InputStream;

public class TgaGdxTexture extends GdxTextureResource {

	public TgaGdxTexture(final ModelViewer viewer, final ResourceHandler handler, final String extension,
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

			var bitmap = TgaFile.readTGA(fetchUrl, src);
			setGdxTexture(ImageUtils.getTexture(bitmap, false));
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
