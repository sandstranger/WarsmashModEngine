package net.warsmash.phone.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.etheller.warsmash.WarsmashGdxMultiScreenGame;
import com.etheller.warsmash.viewer5.AudioContext;
import com.etheller.warsmash.viewer5.AudioDestination;
import com.etheller.warsmash.viewer5.gl.ANGLEInstancedArrays;
import com.etheller.warsmash.viewer5.gl.AudioExtension;
import com.etheller.warsmash.viewer5.gl.Extensions;
import com.etheller.warsmash.viewer5.gl.WireframeExtension;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
		configuration.useGL30 = true;
//		DataTable dataTable = loadWarsmashIni();
		loadExtensions();
		final WarsmashGdxMultiScreenGame warsmashGdxMultiScreenGame = new WarsmashGdxMultiScreenGame();
		initialize(warsmashGdxMultiScreenGame, configuration);
	}

	public static void loadExtensions() {
		Extensions.angleInstancedArrays = new ANGLEInstancedArrays() {
			@Override
			public void glVertexAttribDivisorANGLE(final int index, final int divisor) {
				Gdx.gl30.glVertexAttribDivisor(index, divisor);
			}

			@Override
			public void glDrawElementsInstancedANGLE(final int mode, final int count, final int type,
													 final int indicesOffset, final int instanceCount) {
				Gdx.gl30.glDrawElementsInstanced(mode, count, type, indicesOffset, instanceCount);
			}

			@Override
			public void glDrawArraysInstancedANGLE(final int mode, final int first, final int count,
												   final int instanceCount) {
				Gdx.gl30.glDrawArraysInstanced(mode, first, count, instanceCount);
			}
		};
		Extensions.wireframeExtension = new WireframeExtension() {
			@Override
			public void glPolygonMode(final int face, final int mode) {
			}
		};
		Extensions.audio = new AudioExtension() {
			@Override
			public AudioContext createContext(boolean world) {
				AudioContext.Listener listener;
				listener = AudioContext.Listener.DO_NOTHING;

				return new AudioContext(listener, new AudioDestination() {
				});
			}

			@Override
			public float getDuration(Sound sound) {
				if (sound == null) {
					return 1;
				}
				return 2.0f;
			}

			@Override
			public long play(Sound buffer, float volume, float pitch, float x, float y, float z,
							 boolean is3DSound, float maxDistance, float refDistance, boolean looping) {
				if(looping) {
					buffer.loop(volume, pitch, 0.0f);
				} else {
					buffer.play(volume, pitch, 0.0f);
				}
				return 0;
			}
		};
		Extensions.GL_LINE = 0;
		Extensions.GL_FILL = 0;

		};
}