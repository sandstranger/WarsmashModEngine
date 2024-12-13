package com.etheller.warsmash.viewer5.handlers.w3x;

import com.badlogic.gdx.graphics.Texture;
import com.etheller.warsmash.viewer5.handlers.mdx.MdxModel;

public interface MdxAssetLoader {
	public MdxModel loadModelMdx(final String path);

	public Texture loadPathingTexture(String pathingTexture);
}
