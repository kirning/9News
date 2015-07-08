package kirno.com.fuliba;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *
 * Created by kirno on 2015/7/1.
 */
public class MAdapter extends BaseAdapter{

    private final List<Word> mData;
    private final LayoutInflater mInflater;

    public MAdapter(Activity activity,List<Word> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Word item = mData.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, parent, false);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.type = (TextView) convertView.findViewById(R.id.type);
            viewHolder.context = (TextView) convertView.findViewById(R.id.context);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(item.getPicUrl(), viewHolder.img);
        viewHolder.img.setTag(item.getPicUrl());
        Tools.binderText(item.getTitle(), viewHolder.title);
        Tools.binderText(item.getType(), viewHolder.type);
        Tools.binderText(item.getContext(), viewHolder.context);
        return convertView;
    }

    private class ViewHolder{
        ImageView img;
        TextView title;
        TextView type;
        TextView context;
    }

}
