package kirno.com.fuliba;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by kirno on 2015/7/8.
 */
public class MAsyncTask<T> extends AsyncTask<String, Void, T> {

    private final Tools.IRequestData<T> mRequestData;

    interface IHandlerHtml<T> {
        T getData(Document document);
    }

    private final IHandlerHtml<T> mHandlerHtml;

    public MAsyncTask(Tools.IRequestData<T> requestData,IHandlerHtml<T> handlerHtml)  {
        this.mHandlerHtml = handlerHtml;
        this.mRequestData = requestData;
    }

    @Override
    protected void onPostExecute(T t) {
        mRequestData.obtainData(t);
        super.onPostExecute(t);
    }

    @Override
    protected T doInBackground(String... params) {
        String url = params[0];
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mHandlerHtml.getData(document);
    }
}