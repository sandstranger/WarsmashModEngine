package com.etheller.warsmash.pjblp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataView extends ByteArrayInputStream {
	public final byte[] buffer;
	public final DataInputStream is;

	public DataView(byte[] buf) {
		super(buf);
		this.buffer = buf;
		this.is = new DataInputStream(this);
	}

	public DataView(byte[] buf, int offset, int size) {
		this(Arrays.copyOfRange(buf, offset, offset + size));
	}

	public byte get(int offset) {
		return buffer[offset];
	}

	public int getUint8(int i) {
		this.pos = i;
		return read();
	}
	public int getUint16(int offset){
		this.pos = offset;
		try {
			return is.readUnsignedShort();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public long getUint32(int offset, boolean b) {
		this.pos = offset;
		try {
			return b ? BitConvert.bytes2uintLE(this) : BitConvert.bytes2uintBE(this);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String readString(int offset, int len) {
		try {
			this.pos = offset;
			return readString(this, len);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String readString(InputStream in, int len) throws IOException {
		byte[] temp = new byte[len];
		for (int i = 0; i < temp.length; i++) {
			int b = in.read();
			if (b == -1)
				throw new EOFException();
			temp[i] = (byte) b;
		}
		return new String(temp, StandardCharsets.UTF_8);
	}

	public int read(int i) {
		this.pos=i;
		return this.read();
	}
}
