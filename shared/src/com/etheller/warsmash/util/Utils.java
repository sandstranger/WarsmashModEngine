package com.etheller.warsmash.util;

import java.nio.ByteBuffer;
import java.util.zip.Checksum;

public class Utils {
    public static void update(Checksum checksum, ByteBuffer buffer) {
        int pos = buffer.position();
        int limit = buffer.limit();
        assert (pos <= limit);
        int rem = limit - pos;
        if (rem <= 0) {
            return;
        }
        if (buffer.hasArray()) {
            checksum.update(buffer.array(), pos + buffer.arrayOffset(), rem);
        } else {
            byte[] b = new byte[Math.min(buffer.remaining(), 4096)];
            while (buffer.hasRemaining()) {
                int length = Math.min(buffer.remaining(), b.length);
                buffer.get(b, 0, length);
                checksum.update(b, 0, length);
            }
        }
        buffer.position(limit);
    }
}
