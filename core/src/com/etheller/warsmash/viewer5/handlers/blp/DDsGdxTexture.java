package com.etheller.warsmash.viewer5.handlers.blp;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.etheller.warsmash.datasources.DataSource;
import com.etheller.warsmash.pjblp.DDSReader;
import com.etheller.warsmash.viewer5.GdxTextureResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.PathSolver;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class DDsGdxTexture extends GdxTextureResource {

	public DDsGdxTexture(final ModelViewer viewer, final ResourceHandler handler, final String extension,
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
				throw new RuntimeException("No such fetchURL:" + fetchUrl);
			}

			Bitmap bitmap = DDSReader.decode(IOUtils.toByteArray(src));

			Gdx.app.postRunnable(() -> {
                Texture tex = new Texture(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex.getTextureObjectHandle());
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                bitmap.recycle();
                tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                setGdxTexture(tex);
            });
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
