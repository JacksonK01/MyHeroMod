package net.michaeljackson23.mineademia.quirk.feature.ofa;

import net.michaeljackson23.mineademia.quirk.feature.QuirkModelLogicHelper;
import net.michaeljackson23.mineademia.quirk.feature.QuirkModelStateHelper;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class SlideEffectModel extends BipedEntityModel<AbstractClientPlayerEntity> implements QuirkModelStateHelper, QuirkModelLogicHelper {
	private final ModelPart model;
	private final ModelPart middle;
	private final ModelPart left;
	private final ModelPart right;

	public SlideEffectModel(ModelPart root) {
        super(root);
        this.model = root.getChild("model");
		this.middle = root.getChild("middle");
		this.left = root.getChild("left");
		this.right = root.getChild("right");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData model = modelPartData.addChild("model", ModelPartBuilder.create(), ModelTransform.of(2.0F, 15.5F, -20.0F, 0.0F, 0.0F, -0.1745F));

		ModelPartData middle = model.addChild("middle", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.5F, 0.0F));

		ModelPartData cube_r1 = middle.addChild("cube_r1", ModelPartBuilder.create().uv(0, 13).cuboid(-3.0F, 0.3F, -0.8F, 6.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 5.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r2 = middle.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0.0F, -2.0F, 6.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData left = model.addChild("left", ModelPartBuilder.create(), ModelTransform.of(-5.0F, 1.5F, 0.0F, 0.0F, 0.0F, -0.3491F));

		ModelPartData cube_r3 = left.addChild("cube_r3", ModelPartBuilder.create().uv(13, 0).cuboid(-2.0F, 0.0F, -1.0F, 5.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

		ModelPartData cube_r4 = left.addChild("cube_r4", ModelPartBuilder.create().uv(12, 13).cuboid(-2.0F, 0.3F, -0.8F, 5.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 5.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData right = model.addChild("right", ModelPartBuilder.create(), ModelTransform.of(5.0F, 1.5F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r5 = right.addChild("cube_r5", ModelPartBuilder.create().uv(0, 7).cuboid(-3.0F, 0.3F, -0.8F, 6.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 5.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r6 = right.addChild("cube_r6", ModelPartBuilder.create().uv(12, 7).cuboid(-3.0F, 0.0F, -1.0F, 6.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void process(PlayerEntity player) {
		middle.visible = true;
		left.visible = true;
		right.visible = true;
	}

	@Override
	public void copyModelState(ArmPose leftArmPose, ArmPose rightArmPose, ModelPart head, ModelPart hat, ModelPart body, ModelPart rightArm, ModelPart leftArm, ModelPart rightLeg, ModelPart leftLeg) {

	}
}