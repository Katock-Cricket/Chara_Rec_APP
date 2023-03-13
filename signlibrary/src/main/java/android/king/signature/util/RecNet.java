package android.king.signature.util;

import android.graphics.Bitmap;

import org.pytorch.IValue;
import org.pytorch.MemoryFormat;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.util.Arrays;
import java.util.Map;

public class RecNet {
    private final Module module;
    private final Map<String, Integer> dict;


    public RecNet(Module module, Map<String, Integer> dict){
        this.module = module;
        this.dict = dict;
    }

    public String inference(Bitmap bitmapSrc){
        Bitmap bitmap = Bitmap.createScaledBitmap(bitmapSrc, 32, 32, true);
        bitmap = processBMP(bitmap);
        Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap, new float[]{0, 0, 0}, new float[]{1, 1, 1}, MemoryFormat.CHANNELS_LAST);
        System.out.println(Arrays.toString(inputTensor.getDataAsFloatArray()));
        final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
        final float[] scores = outputTensor.getDataAsFloatArray();
        final int index = getMax(scores);
        String result = "Error";
        for (Map.Entry<String, Integer> entry : dict.entrySet())
            if (entry.getValue().equals(index))
                result = entry.getKey();
        return result;
    }

    private Bitmap processBMP(Bitmap bitmap_ori){
        final int pixCnt = 32*32;
        final int[] pixels = new int[pixCnt];
        bitmap_ori.getPixels(pixels, 0, 32, 0,0,32,32);
        for(int i=0;i<pixels.length;i++){
            pixels[i]--;
        }
        return Bitmap.createBitmap(pixels, 32, 32, Bitmap.Config.RGBA_F16);
    }

    private void outputBMP(Bitmap bitmap){
        final int pixCnt = 32*32;
        final int[] pixels = new int[pixCnt];
        bitmap.getPixels(pixels, 0, 32, 0,0,32,32);
        System.out.println(Arrays.toString(pixels));
    }

    private static int getMax(float[] scores) {
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }
        System.out.println("index: " + maxScoreIdx);
        System.out.println("max val: " + scores[maxScoreIdx]);
        return maxScoreIdx;
    }


}