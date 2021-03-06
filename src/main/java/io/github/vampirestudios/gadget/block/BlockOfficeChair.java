package io.github.vampirestudios.gadget.block;

import io.github.vampirestudios.gadget.HuskyGadgetMod;
import io.github.vampirestudios.gadget.tileentity.TileEntityOfficeChair;
import io.github.vampirestudios.gadget.util.SeatUtil;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockOfficeChair extends BlockColoredFacing {

    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockOfficeChair(EnumDyeColor color) {
        super("office_chair", color);
        this.setCreativeTab(HuskyGadgetMod.deviceDecoration);
        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
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
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            SeatUtil.createSeatAndSit(worldIn, pos, playerIn, 0.5);
        }
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityOfficeChair();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.MODEL;
    }

    public enum Type implements IStringSerializable {
        LEGS, SEAT, FULL;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }
}
