package me.jathusan.wheelzo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.jathusan.wheelzo.framework.RoundedImageView;

/**
 * Created by jathusan on 7/11/15.
 */
public class ImageUtil {

    public static void loadFacebookImageIntoView(String facebookId, RoundedImageView imageView) {
        new FetchFacebookImage("https://graph.facebook.com/" + facebookId + "/picture?width=200&height=200", imageView).execute();
    }

    private static class FetchFacebookImage extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private RoundedImageView imageView;

        private FetchFacebookImage(String url, RoundedImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                Log.e("Facebook Image Load", "Exception while downloading Faceobok image", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
