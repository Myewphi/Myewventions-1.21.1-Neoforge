package myewphi.myewventions.block.cubezio;

import net.minecraft.core.Direction;

public interface ISideRestrictedIO {
    boolean isOutputSide(Direction dir);
    boolean isInputSide(Direction dir);
}
