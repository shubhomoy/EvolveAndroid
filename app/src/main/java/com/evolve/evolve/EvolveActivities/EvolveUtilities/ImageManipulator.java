package com.evolve.evolve.EvolveActivities.EvolveUtilities;

import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

/**
 * Created by shubhomoy on 19/9/15.
 */
public class ImageManipulator {
    public static void writeExifInfo(String path, int id) throws IOException {
        ExifInterface exif = new ExifInterface(path);
        exif.setAttribute(ExifInterface.TAG_MAKE, String.valueOf(id));
        exif.saveAttributes();
    }
}
