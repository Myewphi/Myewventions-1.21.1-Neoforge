package myewphi.myewventions.item;

import myewphi.myewventions.Myewventions;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HungryFleshSwordItem extends Item {
    public HungryFleshSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand usedHand) {
        if(!level.isClientSide()){
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Consume"));
            //player.hurt(Myewventions.fleshGeodeDamage(player), 100);
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }
}
