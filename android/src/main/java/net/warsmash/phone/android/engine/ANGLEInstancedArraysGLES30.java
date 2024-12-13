package net.warsmash.phone.android.engine;

import android.opengl.GLES30;
import com.etheller.warsmash.viewer5.gl.ANGLEInstancedArrays;

public class ANGLEInstancedArraysGLES30 implements ANGLEInstancedArrays {
    @Override
    public void glVertexAttribDivisorANGLE(final int index, final int divisor) {
        GLES30.glVertexAttribDivisor(index, divisor);
    }

    @Override
    public void glDrawElementsInstancedANGLE(final int mode, final int count, final int type,
                                             final int indicesOffset, final int instanceCount) {
        GLES30.glDrawElementsInstanced(mode, count, type, indicesOffset, instanceCount);
    }

    @Override
    public void glDrawArraysInstancedANGLE(final int mode, final int first, final int count,
                                           final int instanceCount) {
        GLES30.glDrawArraysInstanced(mode, first, count, instanceCount);
    }
}
