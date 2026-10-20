package myewphi.myewventions.block.misc;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StardustBlock extends Block {
    //public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty NECorner = IntegerProperty.create("necorner", 0, 1);
    public static final IntegerProperty NWCorner = IntegerProperty.create("nwcorner", 0, 1);
    public static final IntegerProperty SECorner = IntegerProperty.create("secorner", 0, 1);
    public static final IntegerProperty SWCorner = IntegerProperty.create("swcorner", 0, 1);

    public StardustBlock(Properties properties) {
        super(properties);

        registerDefaultState(stateDefinition.any()
                .setValue(NECorner, 0)
                .setValue(NWCorner, 0)
                .setValue(SECorner, 0)
                .setValue(SWCorner, 0)
        );
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NECorner, NWCorner, SECorner, SWCorner);
    }
}
