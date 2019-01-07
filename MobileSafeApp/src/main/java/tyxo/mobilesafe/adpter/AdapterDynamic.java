package tyxo.mobilesafe.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.List;

import tyxo.mobilesafe.R;

/**
 * Created by LY on 2016/7/26 09: 34.
 * Mail      1577441454@qq.com
 * Describe : recycler activity 界面的 gridView 适配器,可拖动条目
 */
public class AdapterDynamic extends BaseDynamicGridAdapter {

    public AdapterDynamic(Context context, int columnCount) {
        super(context, columnCount);
    }

    public AdapterDynamic(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_griditem_recycler, null);
        }
        return convertView;
    }
}































