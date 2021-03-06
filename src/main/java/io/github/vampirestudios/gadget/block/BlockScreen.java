package io.github.vampirestudios.gadget.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import io.github.vampirestudios.gadget.HuskyGadgetMod;
import io.github.vampirestudios.gadget.object.Bounds;
import io.github.vampirestudios.gadget.tileentity.TileEntityScreen;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockScreen extends BlockDevice {

    public static final PropertyBool VERTICAL = PropertyBool.create("vertical");
    private static final PropertyEnum TYPE = PropertyEnum.create("type", Type.class);
    private static final AxisAlignedBB[] BODY_BOUNDING_BOX = new Bounds(4, 0, 2, 12, 2, 14).getRotatedBounds();
    private static final AxisAlignedBB[] BODY_VERTICAL_BOUNDING_BOX = new Bounds(14, 1, 2, 16, 9, 14).getRotatedBounds();
    private static final AxisAlignedBB[] SELECTION_BOUNDING_BOX = new Bounds(3, 0, 1, 13, 3, 15).getRotatedBounds();
    private static final AxisAlignedBB[] SELECTION_VERTICAL_BOUNDING_BOX = new Bounds(13, 0, 1, 16, 10, 15).getRotatedBounds();

    public BlockScreen() {
        super(Material.ANVIL, "screen");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, Type.LONELY));
        this.setCreativeTab(HuskyGadgetMod.deviceDecoration);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SELECTION_BOUNDING_BOX[state.getValue(FACING).getHorizontalIndex()];
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BODY_BOUNDING_BOX[state.getValue(FACING).getHorizontalIndex()]);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return this.getBoundingBox(state, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return this.getBoundingBox(state, worldIn, pos);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return state.withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return side != EnumFacing.DOWN;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityScreen) {
            TileEntityScreen screen = (TileEntityScreen) tileEntity;

            NBTTagCompound tileEntityTag = new NBTTagCompound();
            screen.writeToNBT(tileEntityTag);
            tileEntityTag.removeTag("x");
            tileEntityTag.removeTag("y");
            tileEntityTag.removeTag("z");
            tileEntityTag.removeTag("id");

            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("BlockEntityTag", tileEntityTag);

            ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
            drop.setTagCompound(compound);

            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(TYPE, Type.LONELY);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityScreen();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(TYPE, Type.LONELY);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    public enum Type implements IStringSerializable {
        CORNER_TOP_LEFT,
        CORNER_TOP_RIGHT,
        CORNER_BOTTOM_LEFT,
        CORNER_BOTTOM_RIGHT,
        MIDDLE_BOTTOM,
        MIDDLE_TOP,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        MIDDLE,
        LONELY;

        @Override
        public String getName() {
            return name().toLowerCase();
        }

    }

}
