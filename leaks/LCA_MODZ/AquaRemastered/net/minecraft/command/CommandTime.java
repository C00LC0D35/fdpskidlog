package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase {
   @Override
   public String getCommandName() {
      return "time";
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public String getCommandUsage(ICommandSender sender) {
      return "commands.time.usage";
   }

   @Override
   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
      if (args.length > 1) {
         if (args[0].equals("set")) {
            int l;
            if (args[1].equals("day")) {
               l = 1000;
            } else if (args[1].equals("night")) {
               l = 13000;
            } else {
               l = parseInt(args[1], 0);
            }

            this.setTime(sender, l);
            notifyOperators(sender, this, "commands.time.set", new Object[]{l});
            return;
         }

         if (args[0].equals("add")) {
            int k = parseInt(args[1], 0);
            this.addTime(sender, k);
            notifyOperators(sender, this, "commands.time.added", new Object[]{k});
            return;
         }

         if (args[0].equals("query")) {
            if (args[1].equals("daytime")) {
               int j = (int)(sender.getEntityWorld().getWorldTime() % 2147483647L);
               sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, j);
               notifyOperators(sender, this, "commands.time.query", new Object[]{j});
               return;
            }

            if (args[1].equals("gametime")) {
               int i = (int)(sender.getEntityWorld().getTotalWorldTime() % 2147483647L);
               sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
               notifyOperators(sender, this, "commands.time.query", new Object[]{i});
               return;
            }
         }
      }

      throw new WrongUsageException("commands.time.usage");
   }

   @Override
   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
      return args.length == 1
         ? getListOfStringsMatchingLastWord(args, new String[]{"set", "add", "query"})
         : (
            args.length == 2 && args[0].equals("set")
               ? getListOfStringsMatchingLastWord(args, new String[]{"day", "night"})
               : (args.length == 2 && args[0].equals("query") ? getListOfStringsMatchingLastWord(args, new String[]{"daytime", "gametime"}) : null)
         );
   }

   protected void setTime(ICommandSender sender, int time) {
      for(int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
         MinecraftServer.getServer().worldServers[i].setWorldTime((long)time);
      }
   }

   protected void addTime(ICommandSender sender, int time) {
      for(int i = 0; i < MinecraftServer.getServer().worldServers.length; ++i) {
         WorldServer worldserver = MinecraftServer.getServer().worldServers[i];
         worldserver.setWorldTime(worldserver.getWorldTime() + (long)time);
      }
   }
}
