package kirno.com.fuliba.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import kirno.com.fuliba.IRequestWord;
import kirno.com.fuliba.R;
import kirno.com.fuliba.Tools;
import kirno.com.fuliba.Word;
import kirno.com.fuliba.WorldCotnext;


public class WordActivity extends Activity implements Tools.IRequestData<WorldCotnext> {

    private WebView webView;
    private Word mWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        TextView textView = (TextView) findViewById(R.id.word_title);

        webView = (WebView) findViewById(R.id.word_content);

        mWord = (Word) getIntent().getSerializableExtra("word");

        textView.setText(mWord.getTitle());

        requestData();

    }

    private void requestData() {
        int type = getIntent().getIntExtra(ItemFragment.TYPE, -1);
        switch (type) {
            case ItemFragment.FULI:
                Tools.requestWord(this, mWord.getLink());
                break;
            case ItemFragment.DOMGMAN:
                Tools.request178(this, mWord.getLink());
                break;
        }

    }

    @Override
    public void obtainData(WorldCotnext list) {
        webView.loadDataWithBaseURL(null, list.getHtml(), "html/text", "utf-8", null);
    }
}
