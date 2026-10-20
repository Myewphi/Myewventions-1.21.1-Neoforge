package myewphi.myewventions.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyReturnValue(method = "getXRot", at = @At(value = "RETURN"))
    private float modifyGetYRot(float original){
        return 0;
    }
}
