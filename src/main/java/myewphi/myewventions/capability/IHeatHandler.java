package myewphi.myewventions.capability;

public interface IHeatHandler {

    int getHeatSlots();
    int getHeatInSlot(int slot);
    int insertHeat(int slot, int heat, boolean simulate);
    int extractHeat(int slot, int amount, boolean simulate);
    int getHeatLimit(int slot);
}
