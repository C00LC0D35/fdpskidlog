package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderFish extends Render<EntityFishHook> {
   private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

   public RenderFish(RenderManager renderManagerIn) {
      super(renderManagerIn);
   }

   public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)x, (float)y, (float)z);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      this.bindEntityTexture(entity);
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      int i = 1;
      int j = 2;
      float f = 0.0625F;
      float f1 = 0.125F;
      float f2 = 0.125F;
      float f3 = 0.1875F;
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.5F;
      GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181710_j);
      worldrenderer.func_181662_b(-0.5, -0.5, 0.0).func_181673_a(0.0625, 0.1875).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b(0.5, -0.5, 0.0).func_181673_a(0.125, 0.1875).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b(0.5, 0.5, 0.0).func_181673_a(0.125, 0.125).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b(-0.5, 0.5, 0.0).func_181673_a(0.0625, 0.125).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.draw();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      if (entity.angler != null) {
         float f7 = entity.angler.getSwingProgress(partialTicks);
         float f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float) Math.PI);
         Vec3 vec3 = new Vec3(-0.36, 0.03, 0.35);
         vec3 = vec3.rotatePitch(
            -(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * (float) Math.PI / 180.0F
         );
         vec3 = vec3.rotateYaw(
            -(entity.angler.prevRotationYaw + (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * (float) Math.PI / 180.0F
         );
         vec3 = vec3.rotateYaw(f8 * 0.5F);
         vec3 = vec3.rotatePitch(-f8 * 0.7F);
         double d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double)partialTicks + vec3.xCoord;
         double d1 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * (double)partialTicks + vec3.yCoord;
         double d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double)partialTicks + vec3.zCoord;
         double d3 = (double)entity.angler.getEyeHeight();
         if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0 || entity.angler != Minecraft.getMinecraft().thePlayer) {
            float f9 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks)
               * (float) Math.PI
               / 180.0F;
            double d4 = (double)MathHelper.sin(f9);
            double d6 = (double)MathHelper.cos(f9);
            double d8 = 0.35;
            double d10 = 0.8;
            d0 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double)partialTicks - d6 * 0.35 - d4 * 0.8;
            d1 = entity.angler.prevPosY + d3 + (entity.angler.posY - entity.angler.prevPosY) * (double)partialTicks - 0.45;
            d2 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double)partialTicks - d4 * 0.35 + d6 * 0.8;
            d3 = entity.angler.isSneaking() ? -0.1875 : 0.0;
         }

         double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)partialTicks;
         double d5 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)partialTicks + 0.25;
         double d7 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)partialTicks;
         double d9 = (double)((float)(d0 - d13));
         double d11 = (double)((float)(d1 - d5)) + d3;
         double d12 = (double)((float)(d2 - d7));
         GlStateManager.disableTexture2D();
         GlStateManager.disableLighting();
         worldrenderer.func_181668_a(3, DefaultVertexFormats.field_181706_f);
         int k = 16;

         for(int l = 0; l <= 16; ++l) {
            float f10 = (float)l / 16.0F;
            worldrenderer.func_181662_b(x + d9 * (double)f10, y + d11 * (double)(f10 * f10 + f10) * 0.5 + 0.25, z + d12 * (double)f10)
               .func_181669_b(0, 0, 0, 255)
               .func_181675_d();
         }

         tessellator.draw();
         GlStateManager.enableLighting();
         GlStateManager.enableTexture2D();
         super.doRender(entity, x, y, z, entityYaw, partialTicks);
      }
   }

   protected ResourceLocation getEntityTexture(EntityFishHook entity) {
      return FISH_PARTICLES;
   }
}
