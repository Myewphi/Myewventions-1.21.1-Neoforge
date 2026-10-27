package myewphi.myewventions.mixin;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    private float eyeHeight;
    @Shadow
    private float eyeHeightOld;
    @Shadow
    protected abstract void setRotation(float yRot, float xRot, float roll);
    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Inject(method = "setup", at = @At("TAIL"))
    private void addMessage(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if(detached){
            float zoom = 12;

            this.setRotation(45, 65, 0);
            this.setPosition(
                    Mth.lerp(partialTick, entity.xo, entity.getX()) + (zoom * 0.35f),
                    Mth.lerp(partialTick, entity.yo, entity.getY()) + (double)Mth.lerp(partialTick, this.eyeHeightOld, this.eyeHeight) + zoom,
                    Mth.lerp(partialTick, entity.zo, entity.getZ()) - (zoom * 0.35f)
            );
        }
    }
}
