package tyxo.mobilesafe.utils.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.bean.TextValue;

/**
* author tyxo
* created at 2016/10/8 16:24
* des :
*/
public class DialogListViewAdapter<T extends TextValue> extends BaseAdapter {

    private Context mContext;
    private List<T> mList;
    private int checkItemPosition = 0;

    public DialogListViewAdapter(List<T> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    public void setCheckPosition(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public int getCheckPosition() {
        return checkItemPosition;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, android.R.layout.simple_list_item_1, null);
        }
        holder = ViewHolder.getHolder(convertView);
        fillValue(position, holder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.textView.setTextSize(19);
        viewHolder.textView.setText(mList.get(position).getValue());

        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.textView.setTextColor(Color.BLUE);
            } else {
                viewHolder.textView.setTextColor(mContext.getResources().getColor(R.color.text_black_light_66));
            }
        }
    }

    static class ViewHolder {
        TextView textView;

        private ViewHolder(View convertView) {
            textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setGravity(Gravity.CENTER);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
