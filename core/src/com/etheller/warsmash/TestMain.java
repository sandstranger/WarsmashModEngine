package com.etheller.warsmash;

import com.etheller.warsmash.util.War3ID;

public class TestMain {
	public static void main(final String[] args) {
		if (true) {
			System.out.println(War3ID.fromString("hwat").getValue());
			System.out.println(Integer.toHexString(War3ID.fromString("hwat").getValue()));
			System.out.println(new War3ID(0x68776174));
			return;
		}
//		System.out.println(Integer.parseInt("4294967295"));
		for (int i = 1; i <= 30; i++) {
//			System.out.println(a(i));
		}

		int checkX = 0;
		int checkY = 0;
		for (int i = 0; i < 300; i++) {
			System.out.println(checkX + "," + checkY);
			final double angle = ((((int) Math.floor(Math.sqrt((4 * i) + 1))) % 4) * Math.PI) / 2;
			checkX += (int) Math.sin(angle);
			checkY += (int) Math.cos(angle);
		}
	}

	public static int a(final int n) {
		if (n == 1) {
			return 0;
		}
		else {
			return a(n - 1) - (int) Math.sin(((Math.floor(Math.sqrt((4 * (n - 2)) + 1)) % 4) * Math.PI) / 2);
		}
	}
}
