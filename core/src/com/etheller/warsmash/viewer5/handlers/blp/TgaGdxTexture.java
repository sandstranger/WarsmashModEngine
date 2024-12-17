package com.etheller.warsmash.viewer5.handlers.blp;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.etheller.warsmash.TGAReader;
import com.etheller.warsmash.datasources.DataSource;
import com.etheller.warsmash.util.ImageUtils;
import com.etheller.warsmash.viewer5.GdxTextureResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.PathSolver;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;

import org.apache.commons.io.IOUtils;

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

			Pixmap bitmap = TGAReader.decode(IOUtils.toByteArray(src));
			Texture texture = new Texture(bitmap);
			texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			setGdxTexture(texture);
			src.close();
			ImageUtils.disposePixMap(bitmap);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
