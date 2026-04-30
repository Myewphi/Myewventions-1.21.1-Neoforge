package myewphi.myewventions.block.fleshgeode;

import myewphi.myewventions.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.NotNull;

public class FleshGeodeMeatBlock extends FleshGeodeBlock{
    public FleshGeodeMeatBlock(Properties properties) {
        super(properties);
    }

    public void feedFleshGeodeBlock(LivingDeathEvent deathEvent, BlockPos pos){
        Level level = deathEvent.getEntity().level();

        level.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if(entity instanceof LivingEntity && !level.isClientSide() && level.getBlockState(pos.above()).isAir()){
            if(entity instanceof Player){
                if(((Player) entity).isCreative()){
                    return;
                }
            }
            if(Math.random() < 0.05){
                if (entity.getKnownMovement().x != 0 || entity.getKnownMovement().y != 0) {
                    level.setBlock(pos.above(), ModBlocks.TOOTHY_SPIKES.get().defaultBlockState(), 3);
                }
            }
        }

        super.stepOn(level, pos, state, entity);
    }
}
