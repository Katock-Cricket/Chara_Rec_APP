package android.king.signature.util;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecNet {
    public final String module, dict;


    public RecNet(String module, String dict) {
        this.module = module;
        this.dict = dict;
    }

    public static String inference(Bitmap bitmap, String module,String dict) {
        String imgPath = saveBitmap(bitmap);
        Python python = Python.getInstance();
        PyObject pyObject = python.getModule("demo").callAttr("demo", imgPath, module, dict);
        String ret = pyObject.toJava(String.class);
        return ret;
    }

    public static String saveBitmap(Bitmap bm) {
        Log.e(TAG, "保存图片");
        String path = Environment.getExternalStorageDirectory() +"/test.png";
        File f = new File(path);
        if (f.exists()) {
            System.out.println("del exist Img: " + f.delete());
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}