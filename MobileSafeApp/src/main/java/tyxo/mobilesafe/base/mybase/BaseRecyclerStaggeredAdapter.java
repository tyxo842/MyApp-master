package tyxo.mobilesafe.base.mybase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
* @author ly
* @created at 2016/8/25 16:11
* @des : recyclerView 的基类adapter
 *       用法:
 *        class GirlsAdapterMy extends BaseRecyclerStaggeredAdapter <MyHolder,NewslistBean>{...}
 *        参考 GirlsAdapterMy 内的adapter用法.
*/
public abstract class BaseRecyclerStaggeredAdapter<T extends RecyclerView.ViewHolder,E> extends
        RecyclerView.Adapter<T> {

    private Context context;
    protected ArrayList<E> mDatas = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected int layoutItemId;
    protected List<Integer> mHeights;                 //随机item的高度
    protected OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener<E> {
        void onItemClick(View view,E bean ,int position);
        void onItemLongClick(View view, E bean ,int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public BaseRecyclerStaggeredAdapter(Context context, ArrayList<E> datas, int layoutItemId) {
        this.context = context;
        //mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.layoutItemId = layoutItemId;
    }

    public void substituteDatas(ArrayList<E> datas){
        mDatas = datas;
    }
    public void clearDatas(){
        mDatas.clear();
    }
    public void addDatas(ArrayList<E> datas){
        mDatas.addAll(datas);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, layoutItemId, null);
        //MyViewHolder holder = new MyViewHolder(mInflater.inflate(layoutItemId, parent, false));
        return getViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final T holder, final int position) {

        final E bean = mDatas.get(position);
        initItemView(holder,bean,position);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView,bean,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView,bean,pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() >= 0) {
            return mDatas.size();
        } else {
            return 0;
        }
    }

    /** 返回 viewHolder */
    protected abstract T getViewHolder(View itemView);

    /** 设置数据 */
    protected abstract void initItemView(T holder,E bean,int position);

    public void addData(int position,E bean) {
        mDatas.add(position, bean);
        mHeights.add((int) (100 + Math.random() * 300));
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
}


































