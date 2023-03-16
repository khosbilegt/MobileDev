package com.example.cards;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHelper {
    public static String readUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor pdf = context.getContentResolver().openFileDescriptor(uri, "r");

        assert pdf != null;
        assert pdf.getStatSize() <= Integer.MAX_VALUE;
        byte[] data = new byte[(int) pdf.getStatSize()];

        FileDescriptor fd = pdf.getFileDescriptor();
        FileInputStream fileStream = new FileInputStream(fd);
        fileStream.read(data);

        String str = new String(data);
        return str;
    }
}
