package com.etheller.warsmash.pjblp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BitConvert {
	/**
	 * int转字节数组 大端模式
	 */
	public static byte[] int2bytesBE(int x) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (x >> 24);
		bytes[1] = (byte) (x >> 16);
		bytes[2] = (byte) (x >> 8);
		bytes[3] = (byte) x;
		return bytes;
	}

	/**
	 * int转字节数组 小端模式
	 */
	public static byte[] int2bytesLE(int x) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) x;
		bytes[1] = (byte) (x >> 8);
		bytes[2] = (byte) (x >> 16);
		bytes[3] = (byte) (x >> 24);
		return bytes;
	}
	public static int bytes2intBE(InputStream src) throws IOException {
		byte[] bytes = new byte[4];
		src.read(bytes);
		return bytes2intBE(bytes);
	}

	public static int bytes2intLE(InputStream src) throws IOException {
		byte[] bytes = new byte[4];
		src.read(bytes);
		return bytes2intLE(bytes);
	}
	public static byte[] uint2bytesBE(long value)
	{
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putLong(value);
		return Arrays.copyOfRange(bytes, 4, 8);
	}

	public static long bytes2uintBE(byte[] bytes)
	{
		long x = 0;
		for (int i = 0; i < 4; i++) {
			x <<= 8;
			long b = bytes[i] & 0xFF;
			x |= b;
		}
		return x;
	}
	public static byte[] uint2bytesLE(long value)
	{
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putLong(value);
		return Arrays.copyOfRange(bytes, 4, 8);
	}

	public static long bytes2uintLE(byte[] bytes)
	{
		long x = 0;
		for (int i = 0; i < 4; i++) {
			long b = (bytes[i] & 0xFF) << (i * 8);
			x |= b;
		}
		return x;
//		ByteBuffer buffer = ByteBuffer.allocate(8)
//									.put(new byte[]{0, 0, 0, 0})
//									.put(bytes);
//		buffer.position(0);
//		return buffer.getLong();
	}
	/**
	 * 字节数组转int 大端模式
	 */
	public static int bytes2intBE(byte[] bytes) {
		int x = 0;
		for (int i = 0; i < 4; i++) {
			x <<= 8;
			int b = bytes[i] & 0xFF;
			x |= b;
		}
		return x;
	}
	/**
	 * 字节数组转int 小端模式
	 */
	public static int bytes2intLE(byte[] bytes) {
		int x = 0;
		for (int i = 0; i < 4; i++) {
			int b = (bytes[i] & 0xFF) << (i * 8);
			x |= b;
		}
		return x;
	}

	/**
	 * 字节数组转int 大端模式
	 */
	public static int bytes2intBE(byte[] bytes, int byteOffset, int byteCount) {
		int intValue = 0;
		for (int i = byteOffset; i < (byteOffset + byteCount); i++) {
			intValue |= (bytes[i] & 0xFF) << (8 * (i - byteOffset));
		}
		return intValue;
	}

	/**
	 * 字节数组转int 小端模式
	 */
	public static int bytes2intLE(byte[] bytes, int byteOffset, int byteCount) {
		int intValue = 0;
		for (int i = byteOffset; i < (byteOffset + byteCount); i++) {
			intValue <<= 8;
			int b = bytes[i] & 0xFF;
			intValue |= b;
		}
		return intValue;
	}

	public static long bytes2uintLE(InputStream src) throws IOException {
		byte[] b = new byte[4];
		src.read(b);
		return bytes2uintLE(b);
	}

	public static long bytes2uintBE(InputStream src) throws IOException {
		byte[] b = new byte[4];
		src.read(b);
		return bytes2uintBE(b);
	}
}
