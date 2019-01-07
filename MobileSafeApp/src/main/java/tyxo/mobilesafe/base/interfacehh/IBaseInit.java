package tyxo.mobilesafe.base.interfacehh;

public interface IBaseInit {

	/** 绑定View */
	void initView();

	/** 设置View的数据,网络请求等 */
	void initData();

	/** 设置View的监听 */
	void initListener();
}
