package kirno.com.fuliba.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kirno.com.fuliba.MAdapter;
import kirno.com.fuliba.R;
import kirno.com.fuliba.Tools;
import kirno.com.fuliba.Word;

public class ItemFragment extends Fragment implements Tools.IRequestData<List<Word>> {

    public static final int FULI = 0;
    public static final int DOMGMAN = 1;
    public static final int IT_HOME = 2;

    public static final String TYPE = "type";

    private List<Word> mData;

    private MAdapter mAdapter;
    private int page = 1;
    private View loadMore;
    private ListView mListView;
    private int mType;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mType = getArguments().getInt(TYPE);
        mListView = (ListView) view.findViewById(R.id.listView);
        mData = new ArrayList<>();
        loadMore = LayoutInflater.from(getActivity()).inflate(R.layout.loader_more, null);
        mAdapter = new MAdapter(getActivity(), mData);

        mListView.setAdapter(mAdapter);
        mListView.addFooterView(loadMore);
        requestData();

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        page = page + 1;
                        requestData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = mData.get(position);
                Intent intent = new Intent(getActivity(), WordActivity.class);
                intent.putExtra("word", word);
                intent.putExtra(TYPE, mType);
                startActivity(intent);
            }
        });


        return view;
    }

    private void requestData() {
        switch (mType) {
            case 0:
                Tools.requestWord(this, page);
                break;
            case 1:
                Tools.request178Word(this, page);
                break;
            case 2:
                Tools.requestIthome(this, page);
                break;
        }

    }

    @Override
    public void obtainData(List<Word> list) {
        if (list.isEmpty()) {
            mListView.removeFooterView(loadMore);
        }
        mData.addAll(list);
        mAdapter.notifyDataSetChanged();
    }


}
