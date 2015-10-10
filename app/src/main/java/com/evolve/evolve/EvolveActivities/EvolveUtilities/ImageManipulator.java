package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shubhomoy on 19/9/15.
 */
public class ImageManipulator {
    public void writeExifInfo(String path, int id) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        exif.setAttribute(ExifInterface.TAG_MAKE, String.valueOf(id));
        exif.saveAttributes();
    }

    public int readExifInfo(String path) throws Exception {
        ExifInterface exif = new ExifInterface(path);
        return Integer.parseInt(exif.getAttribute(ExifInterface.TAG_MAKE));
    }

    public void deleteFromLocal(Context context,String filename)throws Exception{

        EvolveDatabase database=new EvolveDatabase(context);

        File file=new File(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename);
        file.delete();

        int exifId=readExifInfo(Environment.getExternalStorageDirectory().toString() + "/Evolve/" + filename);
        database.open();
        database.deletePicInfo(exifId);
        database.close();
    }
}
