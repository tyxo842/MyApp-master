package tyxo.mobilesafe.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.mybase.BaseRecyclerStaggeredAdapter;
import tyxo.mobilesafe.bean.BeanGirls;
import tyxo.mobilesafe.widget.recyclerdivider.recyclerbase.RatioImageView;

/**
 * Created by LY on 2016/8/26 13: 34.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class GirlsAdapterMy extends BaseRecyclerStaggeredAdapter<GirlsAdapterMy.MyHolder,BeanGirls.ShowapiResBodyBean.NewslistBean> {

    protected List<Integer> mHeights;                 //随机item的高度
    private Context context;

    public GirlsAdapterMy(Context context, ArrayList <BeanGirls.ShowapiResBodyBean.NewslistBean> datas, int layoutItemId) {
        super(context, datas, layoutItemId);
        this.context = context;

        mHeights = new ArrayList<>();
        for (int i = 0; i < mDatas.size(); i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }
    }

    @Override
    protected MyHolder getViewHolder(View itemView) {
        return new MyHolder(itemView);
    }

    @Override
    protected void initItemView(MyHolder holder, BeanGirls.ShowapiResBodyBean.NewslistBean bean,int position) {
        //int position = holder.getLayoutPosition();

        /*// 设置随机高度 ---> 报空了...
        ViewGroup.LayoutParams lp = holder.mGirlImage.getLayoutParams();
        lp.height = mHeights.get(position);
        holder.mGirlImage.setLayoutParams(lp);
        */

        /** 设置item的 view内容数据 */
        holder.itemView.setTag(position);
        Glide.with(context)
                .load(bean.getPicUrl())
                .centerCrop()
                .placeholder(R.color.global_background)
                .into(holder.mGirlImage);
    }

    class MyHolder extends RecyclerView.ViewHolder{
        RatioImageView mGirlImage;
        public MyHolder(View itemView) {
            super(itemView);
            mGirlImage = (RatioImageView) itemView.findViewById(R.id.girl_image_item);
            mGirlImage.setRatio(0.618f);// set the ratio to golden ratio.
        }
    }
}














