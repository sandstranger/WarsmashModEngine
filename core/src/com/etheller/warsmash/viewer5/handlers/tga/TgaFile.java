/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etheller.warsmash.viewer5.handlers.tga;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 *
 * @author Riven, modified by Oger-Lord
 */
public class TgaFile {

	/**
	 * Read a TGA image from a file
	 *
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static BufferedImage readTGA(final File file) throws FileNotFoundException, IOException {
		return readTGA(file.getName(), new FileInputStream(file));

	}

	/**
	 * Read a TGA image from an input stream.
	 *
	 * @param name
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage readTGA(final String name, final InputStream stream) throws IOException {

		// Read Header
		final byte[] header = new byte[18];
		stream.read(header);

		// Read pixel data
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		data = buffer.toByteArray();

		// Verify Header
		if ((header[0] | header[1]) != 0) {
			throw new IllegalStateException("Error " + name);
		}
		if (header[2] != 2) {
			throw new IllegalStateException("Error " + name);
		}
		int w = 0, h = 0;
		w |= (header[12] & 0xFF) << 0;
		w |= (header[13] & 0xFF) << 8;
		h |= (header[14] & 0xFF) << 0;
		h |= (header[15] & 0xFF) << 8;

		boolean alpha;

		if ((header[16] == 24)) {
			alpha = false;
		}
		else if (header[16] == 32) {
			alpha = true;
		}
		else {
			throw new IllegalStateException("Error " + name + " invalid pixel depth: " + header[16]);
		}

		if ((header[17] & 15) != (alpha ? 8 : 0)) {
			throw new IllegalStateException("Error " + name);
		}

		final BufferedImage dst = new BufferedImage(w, h,
				alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		final int[] pixels = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
		if (pixels.length != (w * h)) {
			throw new IllegalStateException("Error " + name);
		}
		if (data.length < (pixels.length * (alpha ? 4 : 3))) {
			throw new IllegalStateException("Error " + name + " not enaugh pixel data");
		}

		if (alpha) {
			for (int i = 0, p = (pixels.length - 1) * 4; i < pixels.length; i++, p -= 4) {
				pixels[i] |= ((data[p + 0]) & 0xFF) << 0;
				pixels[i] |= ((data[p + 1]) & 0xFF) << 8;
				pixels[i] |= ((data[p + 2]) & 0xFF) << 16;
				pixels[i] |= ((data[p + 3]) & 0xFF) << 24;
			}
		}
		else {
			for (int i = 0, p = (pixels.length - 1) * 3; i < pixels.length; i++, p -= 3) {
				pixels[i] |= ((data[p + 0]) & 0xFF) << 0;
				pixels[i] |= ((data[p + 1]) & 0xFF) << 8;
				pixels[i] |= ((data[p + 2]) & 0xFF) << 16;
			}
		}

		if ((header[17] >> 4) == 1) {
			// ok
		}
		else if ((header[17] >> 4) == 0) {
			// flip horizontally

			for (int y = 0; y < h; y++) {
				final int w2 = w / 2;
				for (int x = 0; x < w2; x++) {
					final int a = (y * w) + x;
					final int b = (y * w) + (w - 1 - x);
					final int t = pixels[a];
					pixels[a] = pixels[b];
					pixels[b] = t;
				}
			}
		}
		else if ((header[17] >> 4) == 2) {
			// flip horizontally and vertically

			for (int y = 0; y < h; y++) {
				final int w2 = w / 2;
				for (int x = 0; x < w2; x++) {
					final int a = (y * w) + x;
					final int b = ((h - 1 - y) * w) + (w - 1 - x);
					final int t = pixels[a];
					pixels[a] = pixels[b];
					pixels[b] = t;
				}
			}
		}
		else {
			final int headerval = header[17] >> 4;
			throw new UnsupportedOperationException("Error " + name + " (" + headerval + ")");
		}

		return dst;
	}
}