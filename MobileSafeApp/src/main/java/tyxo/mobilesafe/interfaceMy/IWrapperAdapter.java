package tyxo.mobilesafe.interfaceMy;

import android.support.v7.widget.RecyclerView;

/**
* @author ly
* @created at 2016/7/14 9:49
* @des : recyclerview 添加头尾布局 adapter 接口
*/
public interface IWrapperAdapter {

    RecyclerView.Adapter getWrappedAdapter() ;
}
