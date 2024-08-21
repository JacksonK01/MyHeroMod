package net.michaeljackson23.mineademia.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.michaeljackson23.mineademia.client.ClientCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double getFovReturn(double original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null)
            return original;

        UUID uuid = player.getUuid();
        return original * ClientCache.getZoomLevel(uuid);
    }

//    @Shadow
//    @Final
//    private Camera camera;
//
//    @Shadow
//    @Final
//    MinecraftClient client;
//
//    private final HashMap<BlockPos, BlockState> savedStates = new HashMap<>();
//
//
//    @Inject(at = @At(value = "HEAD"), method = "render")
//    private void beforeRender(float tickDelta, long startTime, boolean tick, CallbackInfo info) {
//
//
//        List<PhasingPower> phasings = PowerHolderComponent.getPowers(camera.getFocusedEntity(), PhasingPower.class);
//        if (phasings.stream().anyMatch(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS)) {
//            float view = phasings.stream().filter(pp -> pp.getRenderType() == PhasingPower.RenderType.REMOVE_BLOCKS).map(PhasingPower::getViewDistance).min(Float::compareTo).get();
//            Set<BlockPos> eyePositions = getEyePos(0.25F, 0.05F, 0.25F);
//            Set<BlockPos> noLongerEyePositions = new HashSet<>();
//            for (BlockPos p : savedStates.keySet()) {
//                if (!eyePositions.contains(p)) {
//                    noLongerEyePositions.add(p);
//                }
//            }
//            for (BlockPos eyePosition : noLongerEyePositions) {
//                BlockState state = savedStates.get(eyePosition);
//                client.world.setBlockState(eyePosition, state);
//                savedStates.remove(eyePosition);
//            }
//            for (BlockPos p : eyePositions) {
//                BlockState stateAtP = client.world.getBlockState(p);
//                if (!savedStates.containsKey(p) && !client.world.isAir(p) && !(stateAtP.getBlock() instanceof FluidBlock)) {
//                    savedStates.put(p, stateAtP);
//                    client.world.setBlockState(p, Blocks.AIR.getDefaultState());
//                }
//            }
//        } else if (savedStates.size() > 0) {
//            Set<BlockPos> noLongerEyePositions = new HashSet<>(savedStates.keySet());
//            for (BlockPos eyePosition : noLongerEyePositions) {
//                BlockState state = savedStates.get(eyePosition);
//                client.world.setBlockState(eyePosition, state);
//                savedStates.remove(eyePosition);
//            }
//        }
//    }
//
//    private Set<BlockPos> getEyePos(float rangeX, float rangeY, float rangeZ) {
//        Vec3d pos = camera.getFocusedEntity().getPos().add(0, camera.getFocusedEntity().getEyeHeight(camera.getFocusedEntity().getPose()), 0);
//        Box cameraBox = new Box(pos, pos);
//        cameraBox = cameraBox.expand(rangeX, rangeY, rangeZ);
//        HashSet<BlockPos> set = new HashSet<>();
//        BlockPos.stream(cameraBox).forEach(p -> set.add(p.toImmutable()));
//        return set;
//    }

}
