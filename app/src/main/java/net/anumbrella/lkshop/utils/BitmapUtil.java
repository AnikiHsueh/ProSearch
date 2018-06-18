package net.anumbrella.lkshop.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author valord577
 * @date 18-6-17 下午11:41
 */
public class BitmapUtil {

    private ImageView mImageView;

    public void showImageByThread(ImageView imageView, final String url) {
        mImageView = imageView;

        if (null != url) {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = getBitmapFromUrl(url);
                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    mHandler.sendMessage(msg);
                }
            }.start();
        }

    }

    private Bitmap getBitmapFromUrl(String url) {

        Bitmap bitmap;
        InputStream inputStream = null;

        try {

            URL mUrl = new URL(url);
            inputStream = mUrl.openStream();
            return BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != mImageView) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };
}
