package tyxo.mobilesafe.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tyxo.mobilesafe.R;

/**
 * Created by LY on 2016/7/8 10: 15.
 * Mail      1577441454@qq.com
 * Describe : main 界面的 recyclerView 的adapter
 */
public class AdapterMainRecycler extends RecyclerView.Adapter<AdapterMainRecycler.MyViewHolder> {

    private List<String> mDatas ;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    public AdapterMainRecycler(Context context,ArrayList<String> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(View view , int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_recycler, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));

        // 如果设置了回调, 则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int po = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,po);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int po = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,po);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv ;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.id_num);
        }
    }

    public void addData(int position){
        mDatas.add(position,"添加的item");
        notifyItemInserted(position);
    }

    public void removeData(int positon){
        mDatas.remove(positon);
        notifyItemRemoved(positon);
    }
}






















