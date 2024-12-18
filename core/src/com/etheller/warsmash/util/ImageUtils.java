package com.etheller.warsmash.util;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.etheller.warsmash.datasources.DataSource;
import com.etheller.warsmash.pjblp.Blp2;
import com.etheller.warsmash.viewer5.handlers.tga.TgaFile;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;

/**
 * Uses AWT stuff
 */
public final class ImageUtils {
	private static final int BYTES_PER_PIXEL = 4;
	public static final String DEFAULT_ICON_PATH = "ReplaceableTextures\\CommandButtons\\BTNTemp.blp";

	public static Pixmap getPixmap (Texture texture){
		TextureData textureData = texture.getTextureData();
		if (!textureData.isPrepared()){
			textureData.prepare();
		}
		return textureData.consumePixmap();
	}

	public static Pixmap getPixmap(byte[] bytes) throws IOException {
		var image = Blp2.decode(bytes);
		var data = Blp2.getImageData(image, 0);
		var pixmap = new Pixmap(data.width, data.height, Pixmap.Format.RGBA8888);
		pixmap.setPixels((ByteBuffer) BufferUtils.createByteBuffer(data.width * data.height * 4).put(data.data).flip());
		return pixmap;
	}

	public static Texture getAnyExtensionTexture(final DataSource dataSource, final String path) {
		Texture image;
		try {
			final AnyExtensionImage imageInfo = getAnyExtensionImageFixRGB(dataSource, path, "texture");
			image = imageInfo.getImageData();
			if (image != null) {
				return image;
			}
		} catch (final IOException e) {
			return null;
		}
		return null;
	}

	public static AnyExtensionImage getAnyExtensionImageFixRGB(final DataSource dataSource, final String path,
															   final String errorType) throws IOException {
//		final String tgaPath = path.substring(0, path.length() - 4) + ".png";
		if (dataSource.has(path)) {
			String lowerCasePath = path.toLowerCase();
			if (lowerCasePath.endsWith(".blp")){
				InputStream stream = dataSource.getResourceAsStream(path);
				Pixmap pixmap = getPixmap(IOUtils.toByteArray(stream));
				Texture texture = new Texture(pixmap);
				stream.close();
				disposePixMap(pixmap);
				return new AnyExtensionImage(false, texture);
			}
			else if (lowerCasePath.endsWith(".tga")){
				InputStream src = dataSource.getResourceAsStream(path);
				var bitmap = TgaFile.readTGA(path, src);
				src.close();
				return new AnyExtensionImage(false,getTexture(bitmap, false));
			}
			return new AnyExtensionImage(false, new Texture(new DataSourceFileHandle(dataSource, path)));
		} else {
			throw new IllegalStateException("Missing " + errorType + ": " + path);
		}
	}

	public static void disposePixMap(Pixmap pixmap){
		Gdx.app.postRunnable(() -> {
			if (!pixmap.isDisposed()) {
				pixmap.dispose();
			}
		});
	}

	public static final class AnyExtensionImage {
		private final Texture imageData;

		public AnyExtensionImage(final boolean needsSRGBFix, final Texture imageData) {
			this.imageData = imageData;
		}

		public Texture getImageData() {
			return this.imageData;
		}

		public Texture getRGBCorrectImageData() {
			return getImageData();
		}

		public boolean isNeedsSRGBFix() {
			return false;
		}
	}

	public static Buffer getTextureBuffer(final Texture image) {

		final int imageWidth = image.getWidth();
		final int imageHeight = image.getHeight();
		TextureData textureData = image.getTextureData();
		if(!textureData.isPrepared()) {
			textureData.prepare();;
		}
		Pixmap pixmap = textureData.consumePixmap();

		final ByteBuffer buffer = ByteBuffer.allocateDirect(imageWidth * imageHeight * BYTES_PER_PIXEL)
				.order(ByteOrder.nativeOrder());
		// 4
		// for
		// RGBA,
		// 3
		// for
		// RGB

		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {
				final int pixel = pixmap.getPixel(x, y);
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Green component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 0) & 0xFF)); // Alpha component.
				// Only for RGBA
			}
		}

		buffer.flip();
		return buffer;
	}


	private ImageUtils() {
	}

	public static int getARGBFromRGBA(int x) {
		return ((x & 0xFF) << 24) | ((x & 0xFF000000) >> 8) | ((x & 0xFF0000) >> 8)  | ((x & 0xFF00) >> 8);
	}

	public static Texture getTexture(final BufferedImage image, final boolean sRGBFix) {
		final int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		// 4
		// for
		// RGBA,
		// 3
		// for
		// RGB

		final Pixmap pixmap = sRGBFix ? new Pixmap(image.getWidth(), image.getHeight(), Pixmap.Format.RGBA8888) {
			@Override
			public int getGLInternalFormat() {
				return GL30.GL_SRGB8_ALPHA8;
			}
		} : new Pixmap(image.getWidth(), image.getHeight(), Pixmap.Format.RGBA8888);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				final int pixel = pixels[(y * image.getWidth()) + x];
				pixmap.drawPixel(x, y, (pixel << 8) | (pixel >>> 24));
			}
		}
		final Texture texture = new Texture(pixmap);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		return texture;
	}
}
