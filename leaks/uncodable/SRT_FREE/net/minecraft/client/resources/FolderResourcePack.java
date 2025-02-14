package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

public class FolderResourcePack extends AbstractResourcePack {
   public FolderResourcePack(File resourcePackFileIn) {
      super(resourcePackFileIn);
   }

   @Override
   protected InputStream getInputStreamByName(String name) throws IOException {
      return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, name)));
   }

   @Override
   protected boolean hasResourceName(String name) {
      return new File(this.resourcePackFile, name).isFile();
   }

   @Override
   public Set<String> getResourceDomains() {
      Set<String> set = Sets.newHashSet();
      File file1 = new File(this.resourcePackFile, "assets/");
      if (file1.isDirectory()) {
         for(File file2 : Objects.requireNonNull(file1.listFiles(DirectoryFileFilter.DIRECTORY))) {
            String s = getRelativeName(file1, file2);
            if (!s.equals(s.toLowerCase())) {
               this.logNameNotLowercase(s);
            } else {
               set.add(s.substring(0, s.length() - 1));
            }
         }
      }

      return set;
   }
}
