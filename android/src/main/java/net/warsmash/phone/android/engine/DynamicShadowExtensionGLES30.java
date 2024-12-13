package net.warsmash.phone.android.engine;

import android.opengl.*;
import com.etheller.warsmash.viewer5.gl.DynamicShadowExtension;

import java.nio.IntBuffer;

public class DynamicShadowExtensionGLES30 implements DynamicShadowExtension {
	@Override
	public void glFramebufferTexture(int target, int attachment, int texture, int level) {
		GLES32.glFramebufferTexture(target, attachment, texture, level);
	}

	@Override
	public void glDrawBuffer(int mode) {
		GLES30.glDrawBuffers(mode, IntBuffer.allocate(mode));
	}
}
