package net.michaeljackson23.mineademia.util;

import net.minecraft.client.model.ModelPart;

public class ModelPartHolder {
    private final ModelPart modelPart;

    public ModelPartHolder(ModelPart modelPart) {
        this.modelPart = modelPart;
    }

    public ModelPart getModelPart() {
        return modelPart;
    }
}
