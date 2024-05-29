package net.michaeljackson23.mineademia.quirk.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;

public interface QuirkModelHelper {
    void copyModelState(BipedEntityModel.ArmPose leftArmPose,
                        BipedEntityModel.ArmPose rightArmPose,
                        ModelPart head,
                        ModelPart hat,
                        ModelPart body,
                        ModelPart rightArm,
                        ModelPart leftArm,
                        ModelPart rightLeg,
                        ModelPart leftLeg);
}
