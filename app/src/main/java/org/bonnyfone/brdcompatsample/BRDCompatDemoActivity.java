package org.bonnyfone.brdcompatsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.ImageView;

import org.bonnyfone.brdcompat.BitmapRegionDecoderCompat;
import org.bonnyfone.brdcompat.demo.R;

import java.io.IOException;

/**
 * BRDCompat usage example.<br>
 * See <a href="https://github.com/bonnyfone/brdcompat">BRDCompat on Github</a> for more info.
 */
public class BRDCompatDemoActivity extends AppCompatActivity {

    private ImageView imgOriginal;
    private ImageView imgRegionNative1;
    private ImageView imgRegionNative2;
    private ImageView imgRegionFallback1;
    private ImageView imgRegionFallback2;

    private String sampleImage = "sample_img.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup layout
        setContentView(R.layout.activity_main);
        imgOriginal = (ImageView) findViewById(R.id.img_original);
        imgRegionNative1 = (ImageView) findViewById(R.id.region_native_1);
        imgRegionNative2 = (ImageView) findViewById(R.id.region_native_2);
        imgRegionFallback1 = (ImageView) findViewById(R.id.region_fallback_1);
        imgRegionFallback2 = (ImageView) findViewById(R.id.region_fallback_2);

        //Invoke BRDCompat
        try {
            initBRDCompat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Start BRDCompat
     * @throws IOException
     */
    private void initBRDCompat() throws IOException {
        //Load original image
        imgOriginal.setImageBitmap(BitmapFactory.decodeStream(getAssets().open(sampleImage)));

        //BitmapFactory.Options
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        //Arbitrary region
        Rect region = new Rect(1059, 570 , 1500, 1026);

        //Best region size/ratio
        int bestWidth = 200;
        int bestHeight = 300;

        /*
            N.B.: if you test this on a device/emulator running API < 10 BRDCompat will ALWAYS use the fallback implementation,
            despite the value set by BitmapRegionDecoderCompat.setForceFallbackImplementation();
         */

        BitmapRegionDecoderCompat brdCompat = BitmapRegionDecoderCompat.newInstance(getAssets().open(sampleImage), false);
        imgRegionNative1.setImageBitmap(brdCompat.decodeRegion(region, opt));
        imgRegionNative2.setImageBitmap(brdCompat.decodeBestRegion(bestWidth, bestHeight, Gravity.CENTER));

        ///Force Fallback region decoder
        BitmapRegionDecoderCompat.setForceFallbackImplementation(true);
        BitmapRegionDecoderCompat brdFallback = BitmapRegionDecoderCompat.newInstance(getAssets().open(sampleImage), false);
        imgRegionFallback1.setImageBitmap(brdFallback.decodeRegion(region, opt));
        imgRegionFallback2.setImageBitmap(brdFallback.decodeBestRegion(bestWidth, bestHeight, Gravity.CENTER));
    }
}
