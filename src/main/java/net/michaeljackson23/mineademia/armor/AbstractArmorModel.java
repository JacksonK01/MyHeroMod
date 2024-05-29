package net.michaeljackson23.mineademia.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public abstract class AbstractArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {

    public AbstractArmorModel(ModelPart root) {
        super(root);
    }

    public abstract void setHelmetVisible(boolean visible);

    public abstract void setChestplateVisible(boolean visible);

    public abstract void setLeggingsVisible(boolean visible);

    public abstract void setBootsVisible(boolean visible);
}
