package me.jathusan.android.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.jathusan.android.model.RoundedImageView;

/**
 * Created by jathusan on 7/11/15.
 */
public class ImageUtil {

    public static void loadFacebookImageIntoView(Context context, String facebookId, RoundedImageView imageView) {
        new FetchFacebookImage(context, facebookId, imageView).execute();
    }

    public static Intent getOpenFacebookIntent(Context context, String facebookId) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/" + facebookId)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/sentiapps")); //catches and opens a url to the desired page
        }
    }

    private static class FetchFacebookImage extends AsyncTask<Void, Void, Bitmap> {

        private Context mContext;
        private String mFacebookId;
        private RoundedImageView mImageView;

        private FetchFacebookImage(Context context, String facebookId, RoundedImageView imageView) {
            mContext = context;
            mFacebookId = facebookId;
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL("https://graph.facebook.com/" + mFacebookId + "/picture?width=200&height=200");
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
                mImageView.setImageBitmap(bitmap);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fbIntent = getOpenFacebookIntent(mContext, mFacebookId);
                        mContext.startActivity(fbIntent);
                    }
                });
            }
        }
    }

}
