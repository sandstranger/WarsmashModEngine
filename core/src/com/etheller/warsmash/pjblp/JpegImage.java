package com.etheller.warsmash.pjblp;

import java.util.*;

public class JpegImage {
	/*
 Copyright 2011 notmasteryet
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

// - The JPEG specification can be found in the ITU CCITT Recommendation T.81
//   (www.w3.org/Graphics/JPEG/itu-t81.pdf)
// - The JFIF specification can be found in the JPEG File Interchange Format
//   (www.w3.org/Graphics/JPEG/jfif3.pdf)
// - The Adobe Application-Specific JPEG markers in the Supporting the DCT Filters
//   in PostScript Level 2, Technical Note #5116
//   (partners.adobe.com/public/developer/en/ps/sdk/5116.DCT_Filter.pdf)

	// NOTE: This file was edited to match the crude usage of the JPG format by Blizzard for their BLP1 format.
	int[] dctZigZag = new int[]{
			0,
			1, 8,
			16, 9, 2,
			3, 10, 17, 24,
			32, 25, 18, 11, 4,
			5, 12, 19, 26, 33, 40,
			48, 41, 34, 27, 20, 13, 6,
			7, 14, 21, 28, 35, 42, 49, 56,
			57, 50, 43, 36, 29, 22, 15,
			23, 30, 37, 44, 51, 58,
			59, 52, 45, 38, 31,
			39, 46, 53, 60,
			61, 54, 47,
			55, 62,
			63
	};
	protected int width;
	protected int height;
	protected Stack<component> components;
	private byte[] data;
	private DataView dr;

	interface decodeDelegate {
		void decode(component component, int offset);
	}

	int dctCos1 = 4017;  // cos(pi/16)
	int dctSin1 = 799;   // sin(pi/16)
	int dctCos3 = 3406;   // cos(3*pi/16)
	int dctSin3 = 2276;   // sin(3*pi/16)
	int dctCos6 = 1567;  // cos(6*pi/16)
	int dctSin6 = 3784;  // sin(6*pi/16)
	int dctSqrt2 = 5793;  // sqrt(2)
	int dctSqrt1d2 = 2896; // sqrt(2) / 2

	class codeItem {
		public HashMap<Integer, Object> children = new HashMap<>();
		public int index;
	}

	HashMap<Integer, Object> buildHuffmanTable(int[] codeLengths, int[] values) {
		var k = 0;
		var code = new Stack<codeItem>();
		int i;
		int j;
		int length = 16;
		while (length > 0 && codeLengths[length - 1] == 0)
			length--;
		code.push(new codeItem());
		var p = code.get(0);
		codeItem q;
		for (i = 0; i < length; i++) {
			for (j = 0; j < codeLengths[i]; j++) {
				p = code.pop();
				p.children.put(p.index, values[k]);
				while (p.index > 0) {
					p = code.pop();
				}
				p.index++;
				code.push(p);
				while (code.size() <= i) {
					code.push((q = new codeItem()));
					p.children.put(p.index, q.children);
					p = q;
				}
				k++;
			}
			if (i + 1 < length) {
				// p here points to last code
				code.push(q = new codeItem());
				p.children.put(p.index, q.children);
				p = q;
			}
		}
		return code.get(0).children;
	}

	class component {

		public int blocksPerLine;
		public HashMap<Integer, Object> huffmanTableDC;
		public int[] blockData;
		public int pred;
		public HashMap<Integer, Object> huffmanTableAC;
		public int v;
		public int h;
		public int[] output;
		public int scaleX;
		public int scaleY;
		public int blocksPerColumn;
		public int[] quantizationTable;
	}

	int getBlockBufferOffset(component component, int row, int col) {
		return 64 * ((component.blocksPerLine + 1) * row + col);
	}

	class frame {

		public int precision;
		public int samplesPerLine;
		public int scanLines;
		public int mcusPerLine;
		public boolean progressive;
		public int maxH;
		public int maxV;
		public int mcusPerColumn;
		public Stack<component> components;
		public HashMap<Byte, Integer> componentIds;
		public boolean extended;
	}

	abstract class methods {
		public abstract void decodeBaseline(component component, int offset);
	}

	int decodeScan(byte[] data, final int offset,
			frame frame, Stack<component> components, int resetInterval,
			int spectralStart, int spectralEnd,
			int successivePrev, int successive) {
		var precision = frame.precision;
		var samplesPerLine = frame.samplesPerLine;
		var scanLines = frame.scanLines;
		var mcusPerLine = frame.mcusPerLine;
		var progressive = frame.progressive;
		int maxH = frame.maxH, maxV = frame.maxV;
		int startOffset = offset;
		var methods = new methods() {
			int bitsData;
			int bitsCount;
			int offset = startOffset;

			public Integer readBit() {
				if (bitsCount > 0) {
					bitsCount--;
					return (bitsData >> bitsCount) & 1;
				}
				bitsData = dr.read(this.offset++);
//				bitsData = data[this.offset++];
				if (bitsData == 0xFF) {
//					var nextByte = data[offset++];
					var nextByte = dr.read(this.offset++);
					if (nextByte != 0) {
						throw new RuntimeException("unexpected marker: " + ((bitsData << 8) | nextByte));
					}
					// unstuff 0
				}
				bitsCount = 7;
				return bitsData >>> 7;
			}

			public int decodeHuffman(HashMap<Integer, Object> tree) {
				Object node = tree;
				Integer bit;
				while ((bit = readBit()) != null) {
					node = ((HashMap<Integer, Object>) node).get(bit);
					if (node instanceof Number)
						return (int) node;
				}
				throw new RuntimeException();
			}


			Integer receive(int length) {
				var n = 0;
				while (length > 0) {
					var bit = readBit();
					if (bit == null) return null;
					n = (n << 1) | bit;
					length--;
				}
				return n;
			}

			int receiveAndExtend(int length) {
				var n = receive(length);
				if (n >= 1 << (length - 1))
					return n;
				return n + (-1 << length) + 1;
			}

			@Override
			public void decodeBaseline(component component, int offset) {
				int t = decodeHuffman(component.huffmanTableDC);
				var diff = t == 0 ? 0 : receiveAndExtend(t);
				component.blockData[offset] = (component.pred += diff);
				var k = 1;
				while (k < 64) {
					int rs = decodeHuffman(component.huffmanTableAC);
					int s = rs & 15;
					int r = rs >> 4;
					if (s == 0) {
						if (r != 15)
							break;
						k += 16;
						continue;
					}
					k += r;
					var z = dctZigZag[k];
					component.blockData[offset + z] = (short) receiveAndExtend(s);
					k++;
				}
			}

			public void decodeDCFirst(component component, int offset) {
				var t = decodeHuffman(component.huffmanTableDC);
				var diff = t == 0 ? 0 : (receiveAndExtend(t) << successive);
				component.blockData[offset] = (short) (component.pred += diff);
			}

			public void decodeDCSuccessive(component component, int offset) {
				component.blockData[offset] |= readBit() << successive;
			}

			public int eobrun = 0;

			public void decodeACFirst(component component, int offset) {
				if (eobrun > 0) {
					eobrun--;
					return;
				}
				int k = spectralStart, e = spectralEnd;
				while (k <= e) {
					int rs = decodeHuffman(component.huffmanTableAC);
					int s = rs & 15, r = rs >> 4;
					if (s == 0) {
						if (r < 15) {
							eobrun = receive(r) + (1 << r) - 1;
							break;
						}
						k += 16;
						continue;
					}
					k += r;
					var z = dctZigZag[k];
					component.blockData[offset + z] = (short) (receiveAndExtend(s) * (1 << successive));
					k++;
				}
			}

			int successiveACState = 0, successiveACNextValue;

			public void decodeACSuccessive(component component, int offset) {
				int k = spectralStart, e = spectralEnd, r = 0;
				while (k <= e) {
					var z = dctZigZag[k];
					switch (successiveACState) {
					case 0: // initial state
						var rs = decodeHuffman(component.huffmanTableAC);
						int s = rs & 15;
						r = rs >> 4;
						if (s == 0) {
							if (r < 15) {
								eobrun = receive(r) + (1 << r);
								successiveACState = 4;
							}
							else {
								r = 16;
								successiveACState = 1;
							}
						}
						else {
							if (s != 1)
								throw new RuntimeException("invalid ACn encoding");
							successiveACNextValue = receiveAndExtend(s);
							successiveACState = r != 0 ? 2 : 3;
						}
						continue;
					case 1: // skipping r zero items
					case 2:
						if (component.blockData[offset + z] != 0) {
							component.blockData[offset + z] += (readBit() << successive);
						}
						else {
							r--;
							if (r == 0)
								successiveACState = successiveACState == 2 ? 3 : 0;
						}
						break;
					case 3: // set value for a zero item
						if (component.blockData[offset + z] != 0) {
							component.blockData[offset + z] += (readBit() << successive);
						}
						else {
							component.blockData[offset + z] = (short) (successiveACNextValue << successive);
							successiveACState = 0;
						}
						break;
					case 4: // eob
						if (component.blockData[offset + z] != 0) {
							component.blockData[offset + z] += (readBit() << successive);
						}
						break;
					}
					k++;
				}
				if (successiveACState == 4) {
					eobrun--;
					if (eobrun == 0)
						successiveACState = 0;
				}
			}

			public void decodeMcu(component component, decodeDelegate decode, int mcu, int row, int col) {
				var mcuRow = (mcu / mcusPerLine) | 0;
				var mcuCol = mcu % mcusPerLine;
				var blockRow = mcuRow * component.v + row;
				var blockCol = mcuCol * component.h + col;
				var offset = getBlockBufferOffset(component, blockRow, blockCol);
				decode.decode(component, offset);
			}

			public void decodeBlock(component component, decodeDelegate decode, int mcu) {
				var blockRow = (mcu / component.blocksPerLine) | 0;
				var blockCol = mcu % component.blocksPerLine;
				var offset = getBlockBufferOffset(component, blockRow, blockCol);
				decode.decode(component, offset);
			}
		};
		decodeDelegate decodeDCFirst = (c, s) -> methods.decodeDCFirst(c, s);
		decodeDelegate decodeDCSuccessive = (c, s) -> methods.decodeDCSuccessive(c, s);
		decodeDelegate decodeACFirst = (c, s) -> methods.decodeACFirst(c, s);
		decodeDelegate decodeACSuccessive = (c, s) -> methods.decodeACSuccessive(c, s);
		int componentsLength = components.size();
		component component;
		int i, j, k, n;
		decodeDelegate decodeFn;
		if (progressive) {
			if (spectralStart == 0)
				decodeFn = successivePrev == 0 ? decodeDCFirst : decodeDCSuccessive;
			else
				decodeFn = successivePrev == 0 ? decodeACFirst : decodeACSuccessive;
		}
		else {
			decodeFn = (c, s) -> methods.decodeBaseline(c, s);
		}

		int mcu = 0, marker;
		int mcuExpected;
		if (componentsLength == 1) {
			mcuExpected = components.get(0).blocksPerLine * components.get(0).blocksPerColumn;
		}
		else {
			mcuExpected = mcusPerLine * frame.mcusPerColumn;
		}
		if (resetInterval == 0) {
			resetInterval = mcuExpected;
		}

		int h, v;
		while (mcu < mcuExpected) {
			// reset interval stuff
			for (i = 0; i < componentsLength; i++) {
				components.get(i).pred = 0;
			}
			methods.eobrun = 0;

			if (componentsLength == 1) {
				component = components.get(0);
				for (n = 0; n < resetInterval; n++) {
					methods.decodeBlock(component, decodeFn, mcu);
					mcu++;
				}
			}
			else {
				for (n = 0; n < resetInterval; n++) {

					for (i = 0; i < componentsLength; i++) {
						component = components.get(i);
						h = component.h;
						v = component.v;
						for (j = 0; j < v; j++) {
							for (k = 0; k < h; k++) {
								methods.decodeMcu(component, decodeFn, mcu, j, k);
							}
						}
					}
					mcu++;
				}
			}

			// find marker
			marker = dr.getUint16(methods.offset);//data[methods.offset] << 8) | data[methods.offset + 1];
//			System.out.println("marker=" + marker + " offset=" + methods.offset);
			if (marker <= 0xFF00) {
				throw new RuntimeException("marker was not found");
			}

			if (marker >= 0xFFD0 && marker <= 0xFFD7) { // RSTx
				methods.offset += 2;
			}
			else {
				break;
			}
		}

		return methods.offset - startOffset;
	}

	// A port of poppler's IDCT method which in turn is taken from:
	//   Christoph Loeffler, Adriaan Ligtenberg, George S. Moschytz,
	//   "Practical Fast 1-D DCT Algorithms with 11 Multiplications",
	//   IEEE Intl. Conf. on Acoustics, Speech & Signal Processing, 1989,
	//   988-991.
	void quantizeAndInverse(component component, int blockBufferOffset, int[] p) {
		var qt = component.quantizationTable;
		int v0, v1, v2, v3, v4, v5, v6, v7, t;
		int i;

		// dequant
		for (i = 0; i < 64; i++) {
			p[i] = component.blockData[blockBufferOffset + i] * qt[i];
		}

		// inverse DCT on rows
		for (i = 0; i < 8; ++i) {
			var row = 8 * i;

			// check for all-zero AC coefficients
			if (p[1 + row] == 0 && p[2 + row] == 0 && p[3 + row] == 0 &&
						p[4 + row] == 0 && p[5 + row] == 0 && p[6 + row] == 0 &&
						p[7 + row] == 0) {
				t = (dctSqrt2 * p[0 + row] + 512) >> 10;
				p[0 + row] = t;
				p[1 + row] = t;
				p[2 + row] = t;
				p[3 + row] = t;
				p[4 + row] = t;
				p[5 + row] = t;
				p[6 + row] = t;
				p[7 + row] = t;
				continue;
			}

			// stage 4
			v0 = (dctSqrt2 * p[0 + row] + 128) >> 8;
			v1 = (dctSqrt2 * p[4 + row] + 128) >> 8;
			v2 = p[2 + row];
			v3 = p[6 + row];
			v4 = (dctSqrt1d2 * (p[1 + row] - p[7 + row]) + 128) >> 8;
			v7 = (dctSqrt1d2 * (p[1 + row] + p[7 + row]) + 128) >> 8;
			v5 = p[3 + row] << 4;
			v6 = p[5 + row] << 4;

			// stage 3
			t = (v0 - v1 + 1) >> 1;
			v0 = (v0 + v1 + 1) >> 1;
			v1 = t;
			t = (v2 * dctSin6 + v3 * dctCos6 + 128) >> 8;
			v2 = (v2 * dctCos6 - v3 * dctSin6 + 128) >> 8;
			v3 = t;
			t = (v4 - v6 + 1) >> 1;
			v4 = (v4 + v6 + 1) >> 1;
			v6 = t;
			t = (v7 + v5 + 1) >> 1;
			v5 = (v7 - v5 + 1) >> 1;
			v7 = t;

			// stage 2
			t = (v0 - v3 + 1) >> 1;
			v0 = (v0 + v3 + 1) >> 1;
			v3 = t;
			t = (v1 - v2 + 1) >> 1;
			v1 = (v1 + v2 + 1) >> 1;
			v2 = t;
			t = (v4 * dctSin3 + v7 * dctCos3 + 2048) >> 12;
			v4 = (v4 * dctCos3 - v7 * dctSin3 + 2048) >> 12;
			v7 = t;
			t = (v5 * dctSin1 + v6 * dctCos1 + 2048) >> 12;
			v5 = (v5 * dctCos1 - v6 * dctSin1 + 2048) >> 12;
			v6 = t;

			// stage 1
			p[0 + row] = v0 + v7;
			p[7 + row] = v0 - v7;
			p[1 + row] = v1 + v6;
			p[6 + row] = v1 - v6;
			p[2 + row] = v2 + v5;
			p[5 + row] = v2 - v5;
			p[3 + row] = v3 + v4;
			p[4 + row] = v3 - v4;
		}

		// inverse DCT on columns
		for (i = 0; i < 8; ++i) {
			var col = i;

			// check for all-zero AC coefficients
			if (p[1 * 8 + col] == 0 && p[2 * 8 + col] == 0 && p[3 * 8 + col] == 0 &&
						p[4 * 8 + col] == 0 && p[5 * 8 + col] == 0 && p[6 * 8 + col] == 0 &&
						p[7 * 8 + col] == 0) {
				t = (dctSqrt2 * p[i + 0] + 8192) >> 14;
				p[0 * 8 + col] = t;
				p[1 * 8 + col] = t;
				p[2 * 8 + col] = t;
				p[3 * 8 + col] = t;
				p[4 * 8 + col] = t;
				p[5 * 8 + col] = t;
				p[6 * 8 + col] = t;
				p[7 * 8 + col] = t;
				continue;
			}

			// stage 4
			v0 = (dctSqrt2 * p[0 * 8 + col] + 2048) >> 12;
			v1 = (dctSqrt2 * p[4 * 8 + col] + 2048) >> 12;
			v2 = p[2 * 8 + col];
			v3 = p[6 * 8 + col];
			v4 = (dctSqrt1d2 * (p[1 * 8 + col] - p[7 * 8 + col]) + 2048) >> 12;
			v7 = (dctSqrt1d2 * (p[1 * 8 + col] + p[7 * 8 + col]) + 2048) >> 12;
			v5 = p[3 * 8 + col];
			v6 = p[5 * 8 + col];

			// stage 3
			t = (v0 - v1 + 1) >> 1;
			v0 = (v0 + v1 + 1) >> 1;
			v1 = t;
			t = (v2 * dctSin6 + v3 * dctCos6 + 2048) >> 12;
			v2 = (v2 * dctCos6 - v3 * dctSin6 + 2048) >> 12;
			v3 = t;
			t = (v4 - v6 + 1) >> 1;
			v4 = (v4 + v6 + 1) >> 1;
			v6 = t;
			t = (v7 + v5 + 1) >> 1;
			v5 = (v7 - v5 + 1) >> 1;
			v7 = t;

			// stage 2
			t = (v0 - v3 + 1) >> 1;
			v0 = (v0 + v3 + 1) >> 1;
			v3 = t;
			t = (v1 - v2 + 1) >> 1;
			v1 = (v1 + v2 + 1) >> 1;
			v2 = t;
			t = (v4 * dctSin3 + v7 * dctCos3 + 2048) >> 12;
			v4 = (v4 * dctCos3 - v7 * dctSin3 + 2048) >> 12;
			v7 = t;
			t = (v5 * dctSin1 + v6 * dctCos1 + 2048) >> 12;
			v5 = (v5 * dctCos1 - v6 * dctSin1 + 2048) >> 12;
			v6 = t;

			// stage 1
			p[0 * 8 + col] = v0 + v7;
			p[7 * 8 + col] = v0 - v7;
			p[1 * 8 + col] = v1 + v6;
			p[6 * 8 + col] = v1 - v6;
			p[2 * 8 + col] = v2 + v5;
			p[5 * 8 + col] = v2 - v5;
			p[3 * 8 + col] = v3 + v4;
			p[4 * 8 + col] = v3 - v4;
		}

		// convert to 8-bit integers
		for (i = 0; i < 64; ++i) {
			var index = blockBufferOffset + i;
			var q = p[i];
			q = (q <= -2056) ? 0 : (q >= 2024) ? 255 : (q + 2056) >> 4;
			component.blockData[index] = (short) q;
		}
	}

	int[] buildComponentData(frame frame, component component) {
		var blocksPerLine = component.blocksPerLine;
		var blocksPerColumn = component.blocksPerColumn;
		var samplesPerLine = blocksPerLine << 3;
		var computationBuffer = new int[64];

		int i, j, ll = 0;
		for (var blockRow = 0; blockRow < blocksPerColumn; blockRow++) {
			for (var blockCol = 0; blockCol < blocksPerLine; blockCol++) {
				var offset = getBlockBufferOffset(component, blockRow, blockCol);
				quantizeAndInverse(component, offset, computationBuffer);
			}
		}
		return component.blockData;
	}

	byte clampToUint8(int a) {
		return (byte) (a <= 0 ? 0 : a >= 255 ? 255 : a | 0);
	}

	abstract class parser extends JpegImage {
		public abstract int readUint16();

		public abstract byte[] readDataBlock();
	}

	class adobe {

		public byte transformCode;
		public int flags1;
		public int flags0;
		public byte version;
	}

	class jfif {
		private final JpegImage.version version;
		private final int densityUnits;
		private final int xDensity;
		private final int yDensity;
		private final int thumbWidth;
		private final int thumbHeight;
		private final byte[] thumbData;

		jfif(version version, int densityUnits, int xDensity, int yDensity, int thumbWidth, int thumbHeight, byte[] thumbData) {
			this.version = version;
			this.densityUnits = densityUnits;
			this.xDensity = xDensity;
			this.yDensity = yDensity;
			this.thumbWidth = thumbWidth;
			this.thumbHeight = thumbHeight;
			this.thumbData = thumbData;
		}
	}

	class version {

		private final byte major;
		private final byte minor;

		public version(byte major, byte minor) {
			this.major = major;
			this.minor = minor;
		}
	}

	public int readUint16() {
//				var value = (data[offset] << 8) | data[offset + 1];
//				offset += 2;
//				return value;
		var value = dr.getUint16(offset);
		offset += 2;
		return value;
	}

	public byte[] readDataBlock() {
		var length = readUint16();
		var array = Arrays.copyOfRange(data, offset, offset + length - 2);
		offset += array.length;
		return array;
	}

	public void prepareComponents(frame frame) {
		var mcusPerLine = (int) Math.ceil(frame.samplesPerLine / 8 / frame.maxH);
		var mcusPerColumn = (int) Math.ceil(frame.scanLines / 8 / frame.maxV);
		for (var i = 0; i < frame.components.size(); i++) {
			component component = frame.components.get(i);
			var blocksPerLine = (int) Math.ceil(Math.ceil(frame.samplesPerLine / 8) * component.h / frame.maxH);
			var blocksPerColumn = (int) Math.ceil(Math.ceil(frame.scanLines / 8) * component.v / frame.maxV);
			var blocksPerLineForMcu = mcusPerLine * component.h;
			var blocksPerColumnForMcu = mcusPerColumn * component.v;

			var blocksBufferSize = 64 * blocksPerColumnForMcu
										   * (blocksPerLineForMcu + 1);
			component.blockData = new int[blocksBufferSize];
			component.blocksPerLine = blocksPerLine;
			component.blocksPerColumn = blocksPerColumn;
		}
		frame.mcusPerLine = mcusPerLine;
		frame.mcusPerColumn = mcusPerColumn;
	}

	int offset = 0;
	jfif jfif = null;
	adobe adobe = null;
	frame frame;
	int resetInterval;
	HashMap<Integer, int[]> quantizationTables = new HashMap<Integer, int[]>();
	HashMap<Integer, HashMap<Integer, Object>> huffmanTablesAC = new HashMap<>();
	HashMap<Integer, HashMap<Integer, Object>> huffmanTablesDC = new HashMap<>();


	public void parse(byte[] data) {
		this.data = data;
		this.dr = new DataView(data);
		int fileMarker = readUint16();
		if (fileMarker != 0xFFD8) { // SOI (Start of Image)
			throw new RuntimeException("SOI not found");
		}

		fileMarker = readUint16();
		while (fileMarker != 0xFFD9) { // EOI (End of image)
//			System.out.println("fileMarker=" + fileMarker);
			switch (fileMarker) {
			case 0xFFE0: // APP0 (Application Specific)
			case 0xFFE1: // APP1
			case 0xFFE2: // APP2
			case 0xFFE3: // APP3
			case 0xFFE4: // APP4
			case 0xFFE5: // APP5
			case 0xFFE6: // APP6
			case 0xFFE7: // APP7
			case 0xFFE8: // APP8
			case 0xFFE9: // APP9
			case 0xFFEA: // APP10
			case 0xFFEB: // APP11
			case 0xFFEC: // APP12
			case 0xFFED: // APP13
			case 0xFFEE: // APP14
			case 0xFFEF: // APP15
			case 0xFFFE: // COM (Comment)
				var appData = readDataBlock();
				if (fileMarker == 0xFFE0) {
					if (appData[0] == 0x4A && appData[1] == 0x46 && appData[2] == 0x49 &&
								appData[3] == 0x46 && appData[4] == 0) { // 'JFIF\x00'
						int densityUnits = appData[7];
						int xDensity = dr.getUint16(8);//appData[8] << 8) | appData[9];
						int yDensity = dr.getUint16(10);//(appData[10] << 8) | appData[11];
						int thumbWidth = appData[12];
						int thumbHeight = appData[13];
						byte[] thumbData =
								Arrays.copyOfRange(appData, 14, 14 + 3 * appData[12] * appData[13]);
						jfif = new jfif(new version(appData[5], appData[6]), densityUnits, xDensity, yDensity, thumbWidth, thumbHeight, thumbData);
					}
				}
				// TODO APP1 - Exif
				if (fileMarker == 0xFFEE) {
					if (appData[0] == 0x41 && appData[1] == 0x64 && appData[2] == 0x6F &&
								appData[3] == 0x62 && appData[4] == 0x65 && appData[5] == 0) { // 'Adobe\x00'
						adobe = new adobe();
						adobe.version = appData[6];
						adobe.flags0 = dr.getUint16(7);//appData[7] << 8) | appData[8];
						adobe.flags1 = dr.getUint16(9);// (appData[9] << 8) | appData[10];
						adobe.transformCode = appData[11];
					}
				}
				break;

			case 0xFFDB: // DQT (Define Quantization Tables)
				var quantizationTablesLength = readUint16();
				var quantizationTablesEnd = quantizationTablesLength + offset - 2;
				while (offset < quantizationTablesEnd) {
					var quantizationTableSpec = data[offset++];
					var tableData = new int[64];
					if ((quantizationTableSpec >> 4) == 0) { // 8 bit values
						for (var j = 0; j < 64; j++) {
							var z = dctZigZag[j];
							tableData[z] = data[offset++];
						}
					}
					else if ((quantizationTableSpec >> 4) == 1) { //16 bit
						for (var j = 0; j < 64; j++) {
							var z = dctZigZag[j];
							tableData[z] = readUint16();
						}
					}
					else
						throw new RuntimeException("DQT: invalid table spec");
					quantizationTables.put(quantizationTableSpec & 15, tableData);
				}
				break;

			case 0xFFC0: // SOF0 (Start of Frame, Baseline DCT)
			case 0xFFC1: // SOF1 (Start of Frame, Extended DCT)
			case 0xFFC2: // SOF2 (Start of Frame, Progressive DCT)
				if (frame != null) {
					throw new RuntimeException("Only single frame JPEGs supported");
				}
				readUint16(); // skip data length
				frame = new frame();
				frame.extended = (fileMarker == 0xFFC1);
				frame.progressive = (fileMarker == 0xFFC2);
				frame.precision = data[offset++];
				frame.scanLines = readUint16();
				frame.samplesPerLine = readUint16();
				frame.components = new Stack<>();
				frame.componentIds = new HashMap<Byte, Integer>();
				int componentsCount = data[offset++], componentId;
				int maxH = 0, maxV = 0;
				for (var i = 0; i < componentsCount; i++) {
					componentId = data[offset];
					var h = data[offset + 1] >> 4;
					var v = data[offset + 1] & 15;
					if (maxH < h) maxH = h;
					if (maxV < v) maxV = v;
					var qId = data[offset + 2];

					var c = new component();
					c.h = h;
					c.v = v;
					c.quantizationTable = quantizationTables.get((int) qId);
					frame.components.push(c);
					frame.componentIds.put((byte) componentId, frame.components.size() - 1);
					offset += 3;
				}
				frame.maxH = maxH;
				frame.maxV = maxV;
				prepareComponents(frame);
				break;

			case 0xFFC4: // DHT (Define Huffman Tables)
				var huffmanLength = readUint16();
				for (var i = 2; i < huffmanLength; ) {
					var huffmanTableSpec = dr.read(offset++);
					var codeLengths = new int[16];
					var codeLengthSum = 0;
					for (var j = 0; j < 16; j++, offset++)
						codeLengthSum += (codeLengths[j] = dr.read(offset));
					var huffmanValues = new int[codeLengthSum];
					for (var j = 0; j < codeLengthSum; j++, offset++)
						huffmanValues[j] = dr.read(offset);
					i += 17 + codeLengthSum;
					if ((huffmanTableSpec >> 4) == 0) {
						huffmanTablesDC.put(huffmanTableSpec & 15, buildHuffmanTable(codeLengths, huffmanValues));
					}
					else {
						huffmanTablesAC.put(huffmanTableSpec & 15, buildHuffmanTable(codeLengths, huffmanValues));
					}
				}
				break;

			case 0xFFDD: // DRI (Define Restart Interval)
				readUint16(); // skip data length
				resetInterval = readUint16();
				break;

			case 0xFFDA: // SOS (Start of Scan)

				var scanLength = readUint16();
				int selectorsCount = data[offset++];
				var components = new Stack<component>();
				component component;

				for (var i = 0; i < selectorsCount; i++) {
					var componentIndex = frame.componentIds.get(data[offset++]);
					component = frame.components.get(componentIndex);
					var tableSpec = data[offset++];
					component.huffmanTableDC = huffmanTablesDC.get(tableSpec >> 4);
					component.huffmanTableAC = huffmanTablesAC.get(tableSpec & 15);
					components.push(component);
				}
				var spectralStart = data[offset++];
				var spectralEnd = data[offset++];
				var successiveApproximation = data[offset++];
				var processed = decodeScan(data, offset,
						frame, components, resetInterval,
						spectralStart, spectralEnd,
						successiveApproximation >> 4, successiveApproximation & 15);
				offset += processed;
				break;
			default:
				if (data[offset - 3] == 0xFF &&
							data[offset - 2] >= 0xC0 && data[offset - 2] <= 0xFE) {
					// could be incorrect encoding -- last 0xFF byte of the previous
					// block was eaten by the encoder
					offset -= 3;
					break;
				}
				throw new RuntimeException("unknown JPEG marker " + fileMarker);
			}
			fileMarker = readUint16();
		}

		this.width = frame.samplesPerLine;
		this.height = frame.scanLines;
		this.jfif = jfif;
		this.adobe = adobe;
		this.components = new Stack<component>();
		for (
				var i = 0; i < frame.components.size(); i++) {
			component component = frame.components.get(i);
			var c = new component();
			c.output = buildComponentData(frame, component);
			c.scaleX = component.h / frame.maxH;
			c.scaleY = component.v / frame.maxV;
			c.blocksPerLine = component.blocksPerLine;
			c.blocksPerColumn =
					component.blocksPerColumn;
			this.components.push(c);
		}
	}

	public byte[] getData(imageData imageData, int width, int height) {
		int scaleX = this.width / width, scaleY = this.height / height;

		component component;
		int componentScaleX, componentScaleY;
		int x, y, i;
		var offset = 0;
		int Y, Cb, Cr, K, C, M, Ye, R, G, B;
		int colorTransform;
		var numComponents = this.components.size();
		var dataLength = width * height * numComponents;
		//var data = new Uint8Array(dataLength);
		var data = imageData.data;

		// lineData is reused for all components. Assume first component is
		// the biggest
		var lineData = new int[((this.components.get(0).blocksPerLine << 3) *
										this.components.get(0).blocksPerColumn * 8)];

		// First construct image data ...
		for (i = 0; i < numComponents; i++) {
			component = this.components.get(i < 3 ? 2 - i : i);
			var blocksPerLine = component.blocksPerLine;
			var blocksPerColumn = component.blocksPerColumn;
			var samplesPerLine = blocksPerLine << 3;

			int j, k, ll = 0;
			for (var blockRow = 0; blockRow < blocksPerColumn; blockRow++) {
				var scanLine = blockRow << 3;
				for (var blockCol = 0; blockCol < blocksPerLine; blockCol++) {
					var bufferOffset = getBlockBufferOffset(component, blockRow, blockCol);
					int off = 0, sample = blockCol << 3;
					for (j = 0; j < 8; j++) {
						var lineOffset = (scanLine + j) * samplesPerLine;
						for (k = 0; k < 8; k++) {
							lineData[lineOffset + sample + k] =
									component.output[bufferOffset + off++];
						}
					}
				}
			}

			componentScaleX = component.scaleX * scaleX;
			componentScaleY = component.scaleY * scaleY;
			offset = i;

			int cx, cy;
			int index;
			for (y = 0; y < height; y++) {
				for (x = 0; x < width; x++) {
					cy = 0 | (y * componentScaleY);
					cx = 0 | (x * componentScaleX);
					index = cy * samplesPerLine + cx;
					data[offset] = (byte) lineData[index];
					offset += numComponents;
				}
			}
		}
		return data;
	}

	void copyToImageData(imageData imageData) {
		int width = imageData.width, height = imageData.height;
		var imageDataBytes = width * height * 4;
		var imageDataArray = imageData.data;
		var data = this.getData(imageData, width, height);
		int i = 0, j = 0, k0, k1;
		int Y, K, C, M, R, G, B;
		switch (this.components.size()) {
		case 1:
			while (j < imageDataBytes) {
				Y = data[i++];

				imageDataArray[j++] = (byte) Y;
				imageDataArray[j++] = (byte) Y;
				imageDataArray[j++] = (byte) Y;
				imageDataArray[j++] = (byte) 255;
			}
			break;
		case 3:
			while (j < imageDataBytes) {
				R = data[i++];
				G = data[i++];
				B = data[i++];

				imageDataArray[j++] = (byte) R;
				imageDataArray[j++] = (byte) G;
				imageDataArray[j++] = (byte) B;
				imageDataArray[j++] = (byte) 255;
			}
			break;
		case 4:
			while (j < imageDataBytes) {
				C = data[i++];
				M = data[i++];
				Y = data[i++];
				K = data[i++];

				k0 = 255 - K;
				k1 = k0 / 255;


				R = clampToUint8(k0 - C * k1);
				G = clampToUint8(k0 - M * k1);
				B = clampToUint8(k0 - Y * k1);

				imageDataArray[j++] = (byte) R;
				imageDataArray[j++] = (byte) G;
				imageDataArray[j++] = (byte) B;
				imageDataArray[j++] = (byte) 255;
			}
			break;
		default:
			throw new RuntimeException("Unsupported color mode");
		}
	}
}
