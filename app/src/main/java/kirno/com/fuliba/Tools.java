package kirno.com.fuliba;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kirno.com.fuliba.view.ItemFragment;

/**
 * Created by kirno on 2015/7/1.
 */
public class Tools {

    public static void saveToSdCard(Activity activity, String url, Bitmap bitmap) {
        File file = activity.getCacheDir();
        String bitmapPath = file.getPath();


    }

    public static void binderText(String text, TextView textView) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
        }
    }

    public static void requestIthomeWord(final IRequestData<WorldCotnext> requestData,String url) {
        new MAsyncTask<>(requestData, new MAsyncTask.IHandlerHtml<WorldCotnext>() {
            @Override
            public WorldCotnext getData(Document document) {
                WorldCotnext worldCotnext = new WorldCotnext();

                String html = document.getElementById("paragraph").html();
                worldCotnext.setHtml(html);
                return worldCotnext;
            }
        }).execute(url);

    }

    public static void requestIthome(final IRequestData<List<Word>> requestData, int page) {
        String url;
        if (page == 1) {
            url = "http://win10.ithome.com/";
        }else {
            url = "http://win10.ithome.com/category/21_" + page + ".html";
        }

        new MAsyncTask<>(requestData, new MAsyncTask.IHandlerHtml<List<Word>>() {
            @Override
            public List<Word> getData(Document document) {

                Elements elementsByClass = document.getElementsByClass("content");
                Element contentFl = elementsByClass.get(0);

                Elements cate_list = contentFl.getElementsByClass("cate_list");
                Element cateList = cate_list.get(0);
                Elements liLIst = cateList.getElementsByTag("li");
                ArrayList<Word> wordData = new ArrayList<>(liLIst.size() - 1);

                for (int i = 1; i < liLIst.size(); i++) {
                    try {
                        Word word = new Word();

                        Element item = liLIst.get(i);

                        Elements font = item.getElementsByTag("font");
                        String title = font.get(0).text();
                        word.setTitle(title);

                        Elements span = item.getElementsByTag("span");
                        String date = span.get(0).text();
                        word.setType(date);

                        Elements p = item.getElementsByTag("p");
                        String content = p.get(0).text();
                        word.setContext(content);

                        Elements img = item.getElementsByTag("img");
                        String imgUrl = img.get(0).attr("data-original");
                        word.setPicUrl(imgUrl);

                        String link = item.getElementsByTag("a").get(0).attr("href");
                        word.setLink(link);

                        wordData.add(word);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return wordData;
            }
        }).execute(url);
    }


    public interface IRequestData<T> {
        void obtainData(T list);
    }

    public static void request178Word(final IRequestData<List<Word>> requestData, final int page) {
        String url;
        if (page == 1) {
            url = "http://acg.178.com/index.html";
        } else {
            url = "http://acg.178.com/index_" + page + ".html";
        }

        if (!TextUtils.isEmpty(url)) {
            new MAsyncTask<>(requestData, new MAsyncTask.IHandlerHtml<List<Word>>() {
                @Override
                public List<Word> getData(Document document) {
                    Element left = document.getElementsByClass("left").get(0);
                    Elements newsBoxList = left.getElementsByClass("news_box");
                    List<Word> list = new ArrayList<>(newsBoxList.size());
                    for (int i = 0; i < newsBoxList.size(); i++) {
                        try {
                            Element box = newsBoxList.get(i);

                            Elements divs = box.getElementsByTag("div");

                            Element divTitle = divs.get(0);
                            String type = divTitle.getElementsByTag("span").text();
                            String title = divTitle.getElementsByTag("h2").text();

                            Elements img = divs.get(3).getElementsByTag("img");
                            String imgUrl = img.get(0).attr("src");

                            Element divContext = divs.get(4);
                            String context = divContext.text();
                            String link = divContext.getElementsByTag("a").attr("href");

                            Word word = new Word();
                            word.setPicUrl(imgUrl);
                            word.setContext(context);
                            word.setType(type);
                            word.setTitle(title);
                            word.setLink(link);
                            list.add(word);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return list;
                }
            }).execute(url);


        }
    }

    public static void request178(final IRequestData<WorldCotnext> requestWord, String url) {

        new MAsyncTask<>(requestWord, new MAsyncTask.IHandlerHtml<WorldCotnext>() {
            @Override
            public WorldCotnext getData(Document document) {
                WorldCotnext cotnext = new WorldCotnext();
                String text = null;
                try {
                    Element text1 = document.getElementById("text");
                    text = text1.html();
                } catch (NullPointerException e) {
                    text = "内容加载错误！";
                }
                cotnext.setHtml(text);
                return cotnext;
            }
        }).execute("http://acg.178.com/" + url);

    }


    public static void requestWord(final IRequestData<WorldCotnext> requestWord, String url) {


        new AsyncTask<String, Void, WorldCotnext>() {

            @Override
            protected void onPostExecute(WorldCotnext worldCotnext) {
                requestWord.obtainData(worldCotnext);
            }

            @Override
            protected WorldCotnext doInBackground(String... params) {
                WorldCotnext cotnext = null;
                if (params.length > 0) {
                    String u = params[0];
                    cotnext = new WorldCotnext();
                    try {
                        Document document = Jsoup.connect(u)
                                .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36")
                                .get();

                        Element content = document.getElementById("content");
                        Elements img = content.getElementsByTag("img");
                        img.remove(1);
                        Element element = content.getElementsByClass("entry-content").get(0);
                        element.getElementsByTag("img").get(0).remove();
                        String html = element.html();

                        cotnext.setHtml(html);

                    } catch (Exception e) {

                    }
                }
                return cotnext;
            }
        }.execute(url);
    }

    public static void requestWord(final IRequestData<List<Word>> requestData, final int page) {
        new AsyncTask<Integer, Void, List<Word>>() {

            @Override
            protected void onPostExecute(List<Word> words) {
                requestData.obtainData(words);

            }

            @Override
            protected List<Word> doInBackground(Integer... params) {
                List<Word> wordData = new ArrayList<>();
                String page = "";
                if (params.length > 0) {
                    int p = params[0];
                    if (p > 1) {
                        page = "/page/" + p;
                    }
                }
                try {
                    Document document = Jsoup.connect("http://fuliba.asia" + page)
                            .userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.90 Safari/537.36")
                            .get();
                    Element content = document.getElementById("content");
                    Elements article = content.getElementsByTag("article");
                    wordData = new ArrayList<>(article.size());
                    for (int i = 0; i < article.size(); i++) {
                        try {
                            Word word = new Word();
                            Element element = article.get(i);

                            Node typeHead = element.childNode(1);
                            Node typeDiv = typeHead.childNode(1);
                            Node typeDiv2 = typeDiv.childNode(1);
                            Node typeA = typeDiv2.childNode(1);
                            String type = typeA.childNode(0).toString();
                            word.setType(type);

                            String title = element.childNode(1).childNode(3).childNode(0).childNode(0).toString();
                            word.setTitle(title);

                            Element contentElement = element.getElementsByTag("div").get(4);
                            String context = contentElement.childNode(1).childNode(2).toString();
                            word.setContext(context);

                            String img = element.getElementsByTag("img").get(0).attr("src");
                            word.setPicUrl(img);

                            String link = element.getElementsByTag("a").get(1).attr("href");
                            word.setLink(link);

                            wordData.add(word);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (wordData.isEmpty()) {
                        return null;
                    } else {
                        return wordData;
                    }


                }
                return wordData;
            }
        }.execute(page);

    }


}
