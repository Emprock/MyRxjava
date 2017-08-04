// (c)2016 Flipboard Inc, All Rights Reserved.

package com.haoyan.myrxjava2.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haoyan.myrxjava2.entity.MapEntity;
import com.haoyan.myrxjava2.https.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

public class Database {
    private static final String TAG = "Database";
    private static String DATA_FILE_NAME = "data.db";

    private static Database INSTANCE;

    private Gson gson = new Gson();
    private File dataFile = new File(App.getInstance().getFilesDir(), DATA_FILE_NAME);;
    private Database() {
    }

    public static Database getInstance() {
            INSTANCE = new Database();
        return INSTANCE;
    }

    public List<MapEntity.ResultsBean> readItems() {
        //获取文件夹的绝对路径
        Log.i(TAG, "11");
        // Hard code adding some delay, to distinguish reading from memory and reading disk clearly
        try {
            Thread.sleep(100);
            Log.i(TAG, "12");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i(TAG, "13");
        }

        try {
            Log.i(TAG, "14");
            Reader reader = new FileReader(dataFile);
            Log.i(TAG, "15");
            return gson.fromJson(reader, new TypeToken<List<MapEntity.ResultsBean>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "16");
            return null;
        }
    }

    public void writeItems(List<MapEntity.ResultsBean> items) {
        String json = gson.toJson(items);
        try {
            if (!dataFile.exists()) {
                try {
                    dataFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Writer writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
//        dataFile.delete();
    }
}
