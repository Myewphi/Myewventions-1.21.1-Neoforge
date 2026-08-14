package myewphi.myewventions.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class HeaterBlockEntity extends AbstractProcessorBlockEntity {

    public HeaterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.HEATER_BE.get(), pos, blockState);

        items = NonNullList.withSize(1, ItemStack.EMPTY);
        heats = NonNullList.withSize(1, 0);

        NORTH_IO = "both";
        NORTH_TYPE = "solid";
        DOWN_IO = "output";
        DOWN_TYPE = "solid";
        UP_IO = "output";
        UP_TYPE = "heat";
    }
}
