package com.etheller.warsmash.pjblp;

import java.util.Stack;

public class blpImage implements blpDataFormat {

	public int type;
	public int encoding;
	public int alphaDepth;
	public int alphaEncoding;
	public int mipLevels;
	public long[] offsets;
	public long[] engths;
	public int[] lengths;
	public int content;
	public int alphaBits;
	public Stack<mipmap> mipmaps = new Stack<>();
	public byte[] data;
	public int width;
	public int height;
	public int[][] palette;

	public blpImage(byte[] bytes, int width, int height) {
		this.data = bytes;
		this.width = width;
		this.height = height;
	}

	public blpImage() {

	}
}
