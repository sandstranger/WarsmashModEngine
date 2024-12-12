package com.etheller.warsmash.viewer5.handlers.blp;

import com.etheller.warsmash.viewer5.HandlerResource;
import com.etheller.warsmash.viewer5.ModelViewer;
import com.etheller.warsmash.viewer5.handlers.ResourceHandler;
import com.etheller.warsmash.viewer5.handlers.ResourceHandlerConstructionParams;

import java.util.ArrayList;

public class PngHandler extends ResourceHandler {

    public PngHandler() {
        this.extensions = new ArrayList<>();
        this.extensions.add(new String[] { ".png", "arrayBuffer" });
    }

    @Override
    public boolean load(final ModelViewer modelViewer) {
        return true;
    }

    @Override
    public HandlerResource<?> construct(final ResourceHandlerConstructionParams params) {
           return new BlpGdxTexture(params.getViewer(), params.getHandler(), params.getExtension(), params.getPathSolver(),
                params.getFetchUrl());
    }

}
