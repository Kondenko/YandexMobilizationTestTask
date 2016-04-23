package com.kondenko.mobilizationtesttask.utils;

import android.content.Context;

import com.kondenko.mobilizationtesttask.Constants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class JsonCacheHelper {

    /**
     * Saves a file with JSON code into internal storage
     *
     * @param jsonContents json code
     * @throws IOException
     */
    public static void cacheJson(Context context, String jsonContents) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(Constants.CACHED_FILE_NAME, Context.MODE_PRIVATE);
        outputStream.write(jsonContents.getBytes());
        outputStream.close();
    }

    /**
     * Read the file with JSON code from internal storage
     *
     * @return JSON code string
     * <p/>
     * The snippet is taken from <a href="http://www.stackoverflow.com/questions/14768191/how-do-i-read-the-file-content-from-the-internal-storage-android-app">here</a>
     */
    public static  String getCachedJson(Context context) throws IOException {
        FileInputStream inputStream = context.openFileInput(Constants.CACHED_FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

}
