package tyxo.mobilesafe.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import java.util.HashMap;
import java.util.List;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.BaseViewHolder;
import tyxo.mobilesafe.bean.MainGVItemBean;
import tyxo.mobilesafe.widget.BadgeView;

/**
 * Created by LY on 2016/7/21 14: 31.
 * Mail      1577441454@qq.com
 * Describe : 主界面 gridview item 适配器
 */
public class AdapterMainGridView extends BaseDynamicGridAdapter {

    private Context context;
    private List<MainGVItemBean> gvListInfos;
    private List<HashMap<String, Object>> dataSourceList;

    public AdapterMainGridView(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
        this.context = context;
        this.gvListInfos = (List<MainGVItemBean>) items;
        //this.dataSourceList = (List<HashMap<String, Object>>) items;
    }

    /*public AdapterMainGridView(Context context, List<MainGVItemBean> gvListInfos) {
        this.context = context;
        this.gvListInfos = gvListInfos;
    }*/

   /* @Override
    public int getCount() {
        return gvListInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.layout_main_griditem, null);
        }
        /** 查找控件 */
        ImageView main_header_gv_item_iv = BaseViewHolder.get(convertView, R.id.main_header_gv_item_iv);
        TextView main_header_gv_item_tv = BaseViewHolder.get(convertView, R.id.main_header_gv_item_tv);

        ImageView main_header_gv_item_iv_num = BaseViewHolder.get(convertView, R.id.main_header_gv_item_iv_num);

        /** 控件赋值 */
        MainGVItemBean bean = gvListInfos.get(position);
        main_header_gv_item_iv.setImageResource(bean.getIcon());
        main_header_gv_item_tv.setText(bean.getTitle());
        //main_header_gv_item_iv.setImageResource((Integer) dataSourceList.get(position).get("item_image"));
        //main_header_gv_item_tv.setText(dataSourceList.get(position).get("item_text").toString());

        if (2 == position) {
            main_header_gv_item_iv_num.setVisibility(View.VISIBLE);
            BadgeView badge = new BadgeView(context);
            badge.setTargetView(main_header_gv_item_iv_num);
            badge.setBadgeCount(6);
        }

        return convertView;
    }
}
