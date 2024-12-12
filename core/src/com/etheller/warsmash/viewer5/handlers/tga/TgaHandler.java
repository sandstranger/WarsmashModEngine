package com.etheller.warsmash.viewer5.handlers.tga;

import java.util.ArrayList;

import com.etheller.warsmash.viewer5.HandlerResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;
import com.etheller.warsmash.viewer5.handlers.ResourceHandlerConstructionParams;
import com.etheller.warsmash.viewer5.handlers.blp.BlpGdxTexture;

public class TgaHandler extends ResourceHandler {

	public TgaHandler() {
		this.extensions = new ArrayList<>();
		this.extensions.add(new String[] { ".tga", "arrayBuffer" });
	}

	@Override
	public boolean load(final ModelViewer modelViewer) {
		return true;
	}

	@Override
	public HandlerResource<?> construct(final ResourceHandlerConstructionParams params) {
		return new BlpGdxTexture(params.getViewer(), params.getHandler(), params.getExtension(), params.getPathSolver(),
				params.getFetchUrl().substring(0,params.getFetchUrl().length()-4)+".png");
	}

}
