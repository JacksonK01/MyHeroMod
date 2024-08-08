package net.michaeljackson23.mineademia.util;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Optional;

public interface BipedModelMixinAccessor {
    Optional<ModelPart> getChild(String name);
    ModelPart getPart();
}
