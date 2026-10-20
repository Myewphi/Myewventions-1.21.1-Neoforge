package myewphi.myewventions.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeathTimeStickItem extends Item {
    public DeathTimeStickItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(!level.isClientSide){
            ServerPlayer serverPlayer = ((ServerPlayer)player);
            Stat stat = Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH);
            serverPlayer.getStats().setValue(player, stat, serverPlayer.getStats().getValue(stat) + 12000);

            player.sendSystemMessage(Component.literal("Time Since Last Death now equals: " + serverPlayer.getStats().getValue(stat)));
        }

        return super.use(level, player, usedHand);
    }
}
