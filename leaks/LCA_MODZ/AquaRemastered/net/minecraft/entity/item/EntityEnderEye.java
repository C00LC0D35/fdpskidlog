package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
   private double targetX;
   private double targetY;
   private double targetZ;
   private int despawnTimer;
   private boolean shatterOrDrop;

   public EntityEnderEye(World worldIn) {
      super(worldIn);
      this.setSize(0.25F, 0.25F);
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public boolean isInRangeToRenderDist(double distance) {
      double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
      if (Double.isNaN(d0)) {
         d0 = 4.0;
      }

      d0 *= 64.0;
      return distance < d0 * d0;
   }

   public EntityEnderEye(World worldIn, double x, double y, double z) {
      super(worldIn);
      this.despawnTimer = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(x, y, z);
   }

   public void moveTowards(BlockPos p_180465_1_) {
      double d0 = (double)p_180465_1_.getX();
      int i = p_180465_1_.getY();
      double d1 = (double)p_180465_1_.getZ();
      double d2 = d0 - this.posX;
      double d3 = d1 - this.posZ;
      float f = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
      if (f > 12.0F) {
         this.targetX = this.posX + d2 / (double)f * 12.0;
         this.targetZ = this.posZ + d3 / (double)f * 12.0;
         this.targetY = this.posY + 8.0;
      } else {
         this.targetX = d0;
         this.targetY = (double)i;
         this.targetZ = d1;
      }

      this.despawnTimer = 0;
      this.shatterOrDrop = this.rand.nextInt(5) > 0;
   }

   @Override
   public void setVelocity(double x, double y, double z) {
      this.motionX = x;
      this.motionY = y;
      this.motionZ = z;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float f = MathHelper.sqrt_double(x * x + z * z);
         this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, (double)f) * 180.0 / Math.PI);
      }
   }

   @Override
   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      this.posX += this.motionX;
      this.posY += this.motionY;
      this.posZ += this.motionZ;
      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
      this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * 180.0 / Math.PI);

      while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
      this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
      if (!this.worldObj.isRemote) {
         double d0 = this.targetX - this.posX;
         double d1 = this.targetZ - this.posZ;
         float f1 = (float)Math.sqrt(d0 * d0 + d1 * d1);
         float f2 = (float)MathHelper.atan2(d1, d0);
         double d2 = (double)f + (double)(f1 - f) * 0.0025;
         if (f1 < 1.0F) {
            d2 *= 0.8;
            this.motionY *= 0.8;
         }

         this.motionX = Math.cos((double)f2) * d2;
         this.motionZ = Math.sin((double)f2) * d2;
         if (this.posY < this.targetY) {
            this.motionY += (1.0 - this.motionY) * 0.015F;
         } else {
            this.motionY += (-1.0 - this.motionY) * 0.015F;
         }
      }

      float f3 = 0.25F;
      if (this.isInWater()) {
         for(int i = 0; i < 4; ++i) {
            this.worldObj
               .spawnParticle(
                  EnumParticleTypes.WATER_BUBBLE,
                  this.posX - this.motionX * (double)f3,
                  this.posY - this.motionY * (double)f3,
                  this.posZ - this.motionZ * (double)f3,
                  this.motionX,
                  this.motionY,
                  this.motionZ
               );
         }
      } else {
         this.worldObj
            .spawnParticle(
               EnumParticleTypes.PORTAL,
               this.posX - this.motionX * (double)f3 + this.rand.nextDouble() * 0.6 - 0.3,
               this.posY - this.motionY * (double)f3 - 0.5,
               this.posZ - this.motionZ * (double)f3 + this.rand.nextDouble() * 0.6 - 0.3,
               this.motionX,
               this.motionY,
               this.motionZ
            );
      }

      if (!this.worldObj.isRemote) {
         this.setPosition(this.posX, this.posY, this.posZ);
         ++this.despawnTimer;
         if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
            this.setDead();
            if (this.shatterOrDrop) {
               this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
            } else {
               this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
            }
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tagCompound) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tagCompund) {
   }

   @Override
   public float getBrightness(float partialTicks) {
      return 1.0F;
   }

   @Override
   public int getBrightnessForRender(float partialTicks) {
      return 15728880;
   }

   @Override
   public boolean canAttackWithItem() {
      return false;
   }
}
