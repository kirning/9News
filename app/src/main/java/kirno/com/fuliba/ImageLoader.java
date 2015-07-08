package kirno.com.fuliba;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kirno on 2015/7/5.
 */
public class ImageLoader {
    private static ImageLoader instance;
    private LruCache<String, Bitmap> mBitmapLruCache;
    private Set<ImageLoaderAsyncTask> mImageLoaderTaskList;


    private ImageLoader() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        mBitmapLruCache = new LruCache<>((int) (maxMemory / 4));
        mImageLoaderTaskList = new HashSet<>();
    }

    public static ImageLoader initImageLoader() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void binderImageView(final String picUrl, final ImageView img) {
        if (!TextUtils.isEmpty(picUrl) && img != null) {
            img.setTag(picUrl);
            Bitmap bitmap = mBitmapLruCache.get(picUrl);
            if (bitmap != null) {
                if (img.getTag().equals(picUrl)) {
                    img.setImageBitmap(bitmap);
                }
                return;
            }
        }

    }

    public void loaderImg(List<Word> mData, int mStart, int mEnd, ListView listView) {
        for (int i = mStart; i < mEnd; i++) {
            String url = mData.get(i).getPicUrl();
            Bitmap bitmap = mBitmapLruCache.get(url);
            if (bitmap != null) {
                ImageView imageView = (ImageView) listView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            } else {
                ImageLoaderAsyncTask task = new ImageLoaderAsyncTask(listView,url);
                task.execute(url);
                mImageLoaderTaskList.add(task);
            }
        }
    }

    public void cancalLoaderTask() {
        for (ImageLoaderAsyncTask task : mImageLoaderTaskList) {
            task.cancel(false);
        }
    }

    private class ImageLoaderAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private final ListView mListView;
        private final String mPicUrl;

        public ImageLoaderAsyncTask(ListView listView, String url) {
            this.mListView = listView;
            this.mPicUrl = url;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) mListView.findViewWithTag(mPicUrl);
            if (imageView != null && bitmap != null) {
                mBitmapLruCache.put(mPicUrl, bitmap);
                imageView.setImageBitmap(bitmap);
            }

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlStr = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(urlStr);
                URLConnection con = url.openConnection();
                con.setDoInput(true);
                InputStream input = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}
