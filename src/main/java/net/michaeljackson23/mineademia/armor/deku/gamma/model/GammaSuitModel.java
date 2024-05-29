package net.michaeljackson23.mineademia.armor.deku.gamma.model;// Made with Blockbench 4.10.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

import net.michaeljackson23.mineademia.Mineademia;
import net.michaeljackson23.mineademia.armor.AbstractArmorModel;
import net.michaeljackson23.mineademia.armor.ModelWithBoots;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class GammaSuitModel<T extends LivingEntity> extends AbstractArmorModel<T> implements ModelWithBoots {
	private final ModelPart hat;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;
	private final ModelPart left_boot;
	private final ModelPart right_boot;

	public GammaSuitModel(ModelPart root) {
        super(root);
		this.hat = root.getChild("hat");
        this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.left_leg = root.getChild("left_leg");
		this.right_leg = root.getChild("right_leg");
		this.left_boot = root.getChild("left_boot");
		this.right_boot = root.getChild("right_boot");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(32, 26).cuboid(1.0F, 0.5F, 3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(28, 18).cuboid(-4.0F, 0.5F, 3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 40).cuboid(-4.0F, 1.5F, 3.0F, 8.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(42, 51).cuboid(2.0F, 4.5F, 3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(36, 51).cuboid(-4.0F, 4.5F, 3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(24, 10).cuboid(-4.0F, 6.5F, 3.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 16).cuboid(3.0F, 6.5F, 3.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(12, 32).cuboid(-5.5F, -1.0F, -1.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
				.uv(24, 6).cuboid(4.5F, -1.0F, -1.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData back_right_connect_mask_r1 = head.addChild("back_right_connect_mask_r1", ModelPartBuilder.create().uv(12, 16).cuboid(-5.5F, -1.3F, -2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.5819F, 0.5293F, -0.2145F));

		ModelPartData back_left_connect_mask_r1 = head.addChild("back_left_connect_mask_r1", ModelPartBuilder.create().uv(20, 0).cuboid(4.5F, -1.3F, -2.0F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.5819F, -0.5293F, 0.2145F));

		ModelPartData middle_front_mask_r1 = head.addChild("middle_front_mask_r1", ModelPartBuilder.create().uv(50, 26).cuboid(-2.0F, -2.0F, -5.0F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(2.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(24, 13).cuboid(-3.0F, -1.0F, -5.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		ModelPartData right_front_mask_r1 = head.addChild("right_front_mask_r1", ModelPartBuilder.create().uv(16, 32).cuboid(-5.0F, -2.0F, -3.0F, 1.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.4887F, -0.3142F, -0.2967F));

		ModelPartData left_front_mask_r1 = head.addChild("left_front_mask_r1", ModelPartBuilder.create().uv(13, 39).cuboid(4.0F, -2.0F, -3.0F, 1.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.4887F, 0.2618F, 0.2967F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(24, 0).cuboid(-4.0F, 11.0F, -2.5F, 8.0F, 1.0F, 5.0F, new Dilation(0.0F))
				.uv(30, 51).cuboid(-4.0F, 11.0F, -3.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(24, 51).cuboid(2.0F, 11.0F, -3.0F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData front_bag_on_belt_left_r1 = body.addChild("front_bag_on_belt_left_r1", ModelPartBuilder.create().uv(20, 39).cuboid(2.0F, 11.0F, -4.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(18, 51).cuboid(2.0F, 11.0F, 2.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 49).cuboid(-1.0F, 1.0F, -2.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(48, 21).cuboid(-1.0F, 1.0F, 1.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));

		ModelPartData left_glove_lower_thick4_r1 = left_arm.addChild("left_glove_lower_thick4_r1", ModelPartBuilder.create().uv(50, 48).cuboid(-2.0F, 7.0F, 0.1F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(51, 2).cuboid(-2.0F, 8.0F, -3.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(44, 11).cuboid(-2.0F, 1.0F, 0.1F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(40, 46).cuboid(-2.0F, 1.0F, -3.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData left_glove_lower_thick3_r1 = left_arm.addChild("left_glove_lower_thick3_r1", ModelPartBuilder.create().uv(49, 50).cuboid(-3.0F, 8.0F, 1.2F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(51, 0).cuboid(-3.0F, 8.0F, -2.2F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(16, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F))
				.uv(20, 46).cuboid(-3.0F, 1.0F, 1.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(30, 46).cuboid(-3.0F, 1.0F, -2.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(45, 0).cuboid(-3.5F, 8.0F, -2.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));

		ModelPartData left_glove_lower_thick4_r2 = right_arm.addChild("left_glove_lower_thick4_r2", ModelPartBuilder.create().uv(44, 31).cuboid(1.2F, 8.0F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(44, 36).cuboid(-2.2F, 8.0F, -1.0F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
				.uv(10, 51).cuboid(-2.0F, 7.0F, -1.1F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(44, 16).cuboid(-2.0F, 1.0F, -1.1F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(10, 46).cuboid(-2.0F, 1.0F, 2.3F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(28, 6).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(28, 28).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));

		ModelPartData left_boot = modelPartData.addChild("left_boot", ModelPartBuilder.create().uv(47, 41).cuboid(-2.0F, 5.0F, -2.5F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 32).cuboid(1.0F, 4.0F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(25, 0).cuboid(-2.0F, 4.0F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-2.0F, 4.0F, 1.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(50, 36).cuboid(-2.0F, 10.0F, -3.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 32).cuboid(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

		ModelPartData left_boot_base3_r1 = left_boot.addChild("left_boot_base3_r1", ModelPartBuilder.create().uv(44, 6).cuboid(-2.0F, 4.0F, -2.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData right_boot = modelPartData.addChild("right_boot", ModelPartBuilder.create().uv(50, 30).cuboid(-2.0F, 5.0F, -2.5F, 4.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(32, 20).cuboid(1.0F, 4.0F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(17, 32).cuboid(-2.0F, 4.0F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
				.uv(35, 40).cuboid(-2.0F, 4.0F, 1.5F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(50, 45).cuboid(-2.0F, 10.0F, -3.0F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(32, 18).cuboid(-2.0F, 8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

		ModelPartData right_boot_base2_r1 = right_boot.addChild("right_boot_base2_r1", ModelPartBuilder.create().uv(40, 26).cuboid(-2.0F, 4.0F, 1.7F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setHelmetVisible(boolean visible) {
		this.head.visible = visible;
	}

	@Override
	public void setChestplateVisible(boolean visible) {
		this.body.visible = visible;
		this.left_arm.visible = visible;
		this.right_arm.visible = visible;
	}
	@Override
	public void setLeggingsVisible(boolean visible) {
		this.left_leg.visible = visible;
		this.right_leg.visible = visible;
	}
	@Override
	public void setBootsVisible(boolean visible) {
		this.left_boot.visible = visible;
		this.right_boot.visible = visible;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		hat.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_arm.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_leg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		left_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		right_boot.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getLeftBoot() {
		return this.left_boot;
	}

	@Override
	public ModelPart getRightBoot() {
		return this.right_boot;
	}
}