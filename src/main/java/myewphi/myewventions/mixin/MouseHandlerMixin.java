package myewphi.myewventions.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import myewphi.myewventions.Myewventions;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onMove", at = @At("TAIL"))
    private void onMoveMixin(long windowPointer, double xpos, double ypos, CallbackInfo ci){
        Myewventions.LOGGER.info(xpos + " " + ypos);
    }

    @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    private void modifyTurn(LocalPlayer instance, double v, double v1, Operation<Void> original){
        instance.turn(v, v1);
    }
}
