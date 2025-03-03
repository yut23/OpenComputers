package li.cil.oc.api.internal;

import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.Environment;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This interface allows interaction with robots.
 * <br>
 * It is intended to be used by components when installed in a robot. In that
 * case, the robot in question is the tile entity passed to item driver when
 * asked to create the component's environment.
 * <br>
 * A robot's inventory contains component items and items in the actual
 * inventory. The physical layout in the underlying 'real' inventory is as
 * follows:
 * <ul>
 * <li>Tool</li>
 * <li><tt>equipmentInventory.getSizeInventory</tt> hot-swappable components.</li>
 * <li><tt>mainInventory.getSizeInventory</tt> internal inventory slots.</li>
 * <li><tt>componentCount</tt> hard-wired components.</li>
 * </ul>
 * Note that there may be no hot-swappable (or even built-in) components or
 * no inventory, depending on the configuration of the robot. The hard-wired
 * components cannot be changed (removed/replaced).
 * <br>
 * This interface is <em>not meant to be implemented</em>, just used.
 */
public interface Robot extends Agent, Environment, EnvironmentHost, Tiered, ISidedInventory {
    /**
     * The number of built-in components in this robot.
     */
    int componentCount();

    /**
     * Get the environment for the component in the specified slot.
     * <br>
     * This operates on the underlying, real inventory, as described in the
     * comment on top of this class.
     * <br>
     * This will return <tt>null</tt> for slots that do not contain components,
     * or components that do not have an environment (on the calling side).
     *
     * @param index the index of the slot from which to get the environment.
     * @return the environment for that slot, or <tt>null</tt>.
     */
    Environment getComponentInSlot(int index);

    /**
     * Sends the state of the <em>item</em> in the specified slot to the client
     * if it is an upgrade.
     * <br>
     * Use this to update the state of an upgrade in that slot for rendering
     * purposes (e.g. this is used by the generator upgrade to update the
     * active state so the renderer knows which texture to use).
     * <br>
     * This is necessary because inventories are not synchronized by default,
     * only if a player is currently 'looking into' the inventory (opened the
     * GUI of the inventory).
     * <br>
     * The component will be saved to its item's NBT tag compound, as it would
     * be when the game is saved, and then the item is re-sent to the client.
     * Keep the number of calls to this function low, since each call causes a
     * network packet to be sent.
     */
    void synchronizeSlot(int slot);

    /**
     * This essentially returns whether the robot is currently running or not.
     * <br>
     * This is explicitly meant for client side use, to allow upgrade renderers
     * to know whether to resume animations or not, based on whether the robot
     * is currently powered on or not.
     */
    @SideOnly(Side.CLIENT)
    boolean shouldAnimate();
}

