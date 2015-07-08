package kirno.com.fuliba.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import kirno.com.fuliba.R;


public class MainActivity extends Activity {

    private Button[] btnList;
    private ItemFragment fuli;
    private ItemFragment dongman;
    private TextView mTitle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();

        ImageLoader.getInstance().init(config);

        findView();

        initEvent();


        fuli = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ItemFragment.TYPE, ItemFragment.FULI);
        fuli.setArguments(args);

        dongman = new ItemFragment();
        Bundle dongmanBundle = new Bundle();
        dongmanBundle.putInt(ItemFragment.TYPE, ItemFragment.DOMGMAN);
        dongman.setArguments(dongmanBundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, dongman).commit();

        mTitle.setText("178动漫新闻");
    }

    private void initEvent() {
        btnList[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, dongman).commit();
                mTitle.setText("178动漫新闻");
            }
        });

        btnList[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, fuli).commit();
                mTitle.setText("福利控");
            }
        });
    }

    private void findView() {
        btnList = new Button[4];
        btnList[0] = (Button) findViewById(R.id.btn_178);
        btnList[1] = (Button) findViewById(R.id.btn_fuli);
        mTitle = (TextView) findViewById(R.id.activity_title);
    }


}
