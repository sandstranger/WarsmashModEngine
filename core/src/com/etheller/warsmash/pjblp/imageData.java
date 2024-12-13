package com.etheller.warsmash.pjblp;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;

public class imageData extends AbstractBitmap implements blpDataFormat {
	public final byte[] data;
	public final int width;
	public final int height;

	public imageData(byte[] bytes, int width, int height) {
		this.data = bytes;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public ByteBuffer getBuffer() {
		return (ByteBuffer) BufferUtils.createByteBuffer(this.width * height * 4).put(this.data).flip();
	}
}
