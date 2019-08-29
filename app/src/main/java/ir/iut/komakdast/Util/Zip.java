package ir.iut.komakdast.Util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    public boolean unpackZip(String path, int id, Context context) {

        String newPath = context.getFilesDir().getPath() + "/Packages/package_"+id+"/";

        Logger.e("path", "newPath: " + newPath);

        File fmd1 = new File(newPath);
        fmd1.mkdirs();
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                Logger.d("ZIP", filename);

                if (ze.isDirectory()) {
                    File fmd = new File(newPath + filename);
                    fmd.mkdirs();
                    continue;
                }


                FileOutputStream fout = new FileOutputStream(newPath + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
