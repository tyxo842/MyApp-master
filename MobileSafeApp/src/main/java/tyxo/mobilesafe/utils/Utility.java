package tyxo.mobilesafe.utils;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.base.MyApp;


public class Utility {

	public static final int CHECKBOX_NUM = 98465; //企业 订单详情界面,多个条目的checkbox数值位置
	public static final int STOCKUP_DELETE_ID = 98466;//企业 订单详情 发货明细删除按钮位置
	public static final int ACTIVITY_REQUEST_CODE_UNBARCODE = 10;//企业 订单 非条码备货,新增界面销毁activity返回数据
	public static final int UNBARCODE_ITEM_DELETE = 98467;//企业 订单 非条码备货,长按删除 position
	public static final int ORDER_SEARCH_STATE = 98468;//企业 订单查询 搜索 条目的阅读状态
	public static final int ORDER_MODIFYDD_CHANGEMSG = 11;//企业 订单详情 发货详情 修改 替换产品新 界面销毁
	public static boolean isCommitSuccess = false;
	//private Context mContext= MyApp.getAppContext();

	/**
	 * 根据ListView的子项重新计算ListView的高度，然后把高度再作为LayoutParams设置给ListView
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
            // pre-condition
            return;
        }
		
		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
    }
	
	/**
	 * 显示弹窗(关闭)
	 * @param context 上下文
	 * @param message 弹窗内容
	 * @param title 弹窗标题
	 * @param requestCode 回调函数请求代码，用于在回调函数在判断调用者来源
	 * @param dialogCallBack 回调接口
	 */
	public static void showMessageDialog(Context context,String message, String title, final int requestCode, final DialogCallBack dialogCallBack){
		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (dialogCallBack != null) {
					// 服务绑定取消成功，返回上级activity，并刷新其中的 listview
					dialogCallBack.doCallBack(requestCode);
				}
			}
		});
		builder.create().show();
	}
	
	/**
	 * 显示弹窗(确定、取消)
	 * @param context 上下文
	 * @param message 弹窗内容
	 * @param title 弹窗标题
	 * @param requestCode 回调函数请求代码，用于在回调函数在判断调用者来源
	 * @param dialogCallBack 回调接口
	 */
	public static void showMessageDialogWithPositiveButton(Context context,String message, String title, final int requestCode, final DialogCallBack dialogCallBack){
		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				if (dialogCallBack != null) {
					// 服务绑定取消成功，返回上级activity，并刷新其中的 listview
					dialogCallBack.doCallBack(requestCode);
					
				}
			}
		});
		builder.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/** 弹窗的回调接口 */
	public interface DialogCallBack{
		public void doCallBack(int requestCode);
	}
	
	
	/** 检测网络状态 */
	public static boolean detectNetworkState(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}

	/**dialog消失同时,收起输入法*/
	public static void clearEditFocuse(Context context,EditText et){
		et.findFocus();
		//dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, 0); //显示软键盘??
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘??
	}

	/** 从右边开始去掉i个字符 */
	public static String getStringTime(String str,int i){
		str = str.substring(0,str.length()-i);
		return str.trim().replace("/","-");
	}

	/** 判断数据是否为null,为null 返回"" */
	public static Object isDataNull(Object obj){
		obj= (obj == null ? "" : obj);
		return obj;
	}

	// 加减按钮
	public static int switchNum(int disNum,boolean isAdd){
		if (isAdd){
			++disNum;
		}else {
			if (disNum > 0){
				--disNum;
			}
		}
		return disNum;
	}

	// 判断数据是否为空,用于金额处的0.00
	public static String isDataEmpty(String da){
		if(da==null||da==""){
			return 0+"";
		}else
		return da;
	}

	// 订单详情列表,根据出入state,修改阅读状态:只有未阅读会改为已阅读
	public static String modifyState(String state){
		if ("未阅读".equals(state)) {
			return "已阅读";
		} else {
			return state;
		}
	}

	// 根据订单编码,返回相应订单状态
	public static String switchOrderState(String ordertype) {
		switch (ordertype){
			case "" :
				return "";
			case "10":
				return "未阅读";
			case "20":
				return "已阅读";
			case "30":
				return "已发货";
			case "40":
				return "处理中";
			case "50":
				return "部分到货";
			case "60":
				return "已到货";
			case "70":
				return "作废";
			case "80":
				return "完成";
			default:
				break;
		}
		return "";
	}
	// 根据订单编码,返回相应订单状态
	public static String switchOrderDetailState(String ordertype) {
		switch (ordertype){
			case "" :
				return "";
			case "10":
				return "未发货";
			case "20":
				return "已发货";
			case "30":
				return "处理中";
			case "40":
				return "部分到货";
			case "50":
				return "已到货";
			case "60":
				return "作废";
			case "70":
				return "完成";
			default:
				break;
		}
		return "";
	}
	// 根据订单编码,返回相应订单状态
	public static String switchOrderDetailContentState(String ordertype) {
		switch (ordertype){
			case "" :
				return "";
			case "10":
				return "未发货";
			case "20":
				return "已发货";
			case "30":
				return "已到货";
			case "40":
				return "完成";
			case "50":
				return "作废";
			default:
				break;
		}
		return "";
	}

	// 根据订单类型编码,返回相应订单类型
	public static String switchOrderType(String ordertype) {
		switch (ordertype){
			case "" :
				return "";
			case "10":
				return "普通订单";
			case "20":
				return "高值类订单";
			case "30":
				return "骨科类高值订单";
			case "40":
				return "其它订单";
			default:
				break;
		}
		return "";
	}

	// 根据订单紧急编码,返回相应紧急状态,OrderList
	public static String switchOrderDetailList(String ordertype, TextView tv) {
		switch (ordertype) {
			case "" :
//				tv.setTextColor(MyApp.getAppContext().getResources().getColor(R.color.order_text_font_h_color_333333));
				return "";
			case "10":
				return "普通";
			case "20":
				return "部分紧急";
			case "30":
				tv.setTextColor(MyApp.getAppContext().getResources().getColor(R.color.order_text_color_ea5967));
				return "紧急";
		}
		return "";
	}

	// 根据订单紧急编码,返回相应紧急状态,OrderDetail
	public static String switchOrderDetailDegree(String ordertype, TextView tv) {
		switch (ordertype) {
			case "":
				return "";
			case "10":
				tv.setTextColor(MyApp.getAppContext().getResources().getColor(R.color.order_text_color_ea5967));
				return "急需";
			case "20":
				return "不急需";
		}
		return "";
	}

	// 根据传入的状态,设置字体颜色
	public static void setTextColor(String degree, TextView tv ){
		if ("紧急".equals(degree)){
			tv.setTextColor(MyApp.getAppContext().getResources().getColor(R.color.order_text_color_ea5967));
		}else {
			tv.setTextColor(MyApp.getAppContext().getResources().getColor(R.color.order_text_font_h_color_333333));
		}
	}

	// 发货详情界面,删除按钮可否点击
	public static boolean switchDeliveryState(String state){
		if ("未发货".equals(state)) {
			return true;
		} else {
			return false;
		}
	}
	// 发货详情界面,删除+修改 按钮显示状态
	public static void switchDeliveryDeleteVisible(View delete,View modify,boolean isVisible){
		if (isVisible) {
			delete.setVisibility(View.VISIBLE);
		}else{
			delete.setVisibility(View.INVISIBLE);
		}
	}
	// 发货详情界面,删除+修改 按钮显示状态
	public static void switchDeliveryDeleteVisible(Context context,TextView delete,TextView modify,String state){
		delete.setEnabled(false);
		modify.setEnabled(false);
		setDrawLeftIcon(context,delete,R.drawable.icon_shanchu_hui);
		setDrawLeftIcon(context,modify,R.drawable.icon_xiugai_hui);
		delete.setTextColor(context.getResources().getColor(R.color.order_text_font_color_666666));
		modify.setTextColor(context.getResources().getColor(R.color.order_text_font_color_666666));
		if ("未发货".equals(state)) {		//10
			delete.setEnabled(true);
			delete.setTextColor(context.getResources().getColor(R.color.order_timer_bg_2ebbed));
			setDrawLeftIcon(context,delete,R.drawable.icon_shanchu);
		} else if ("已发货".equals(state)) { //20
			modify.setEnabled(true);
			modify.setTextColor(context.getResources().getColor(R.color.order_timer_bg_2ebbed));
			setDrawLeftIcon(context,modify,R.drawable.icon_xiugai);
		} else {							//>=30
			//delete.setEnabled(false);
			//modify.setEnabled(false);
		}
	}

	//订单明细 根据传入的state判断 备货按钮是否可以点击
	public static boolean switchBtnClickState(String state, String orderState){
		if ("70".equals(orderState)||"80".equals(orderState)||"作废".equals(state)||"完成".equals(state)) {
			return false;
		}
		return true;
	}

	//textView按钮设置是否可点击,字体颜色
	public static void setTextViewState(Context context,TextView view,boolean enable){
		view.setEnabled(enable);
		if (enable) {
			view.setTextColor(context.getResources().getColor(R.color.order_timer_bg_2ebbed));

			Drawable drawable= context.getResources().getDrawable(R.drawable.icon_beihuo);
			// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			view.setCompoundDrawables(drawable,null,null,null);
		}else {
			view.setTextColor(context.getResources().getColor(R.color.order_text_font_color_666666));

			Drawable drawable= context.getResources().getDrawable(R.drawable.icon_beihuo_hui);
			// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			view.setCompoundDrawables(drawable,null,null,null);
		}
	}

	// 设置保存按钮 状态
	public static void setBtnSaveStateGray(Context context, TextView tv, boolean isOk){
		if (isOk){
			tv.setEnabled(isOk);
			tv.setTextColor(context.getResources().getColor(R.color.white));
		}else {
			tv.setEnabled(false);
			tv.setTextColor(context.getResources().getColor(R.color.order_text_font_color_666666));
		}
	}

	//订单详情界面 checkbox是否可点击
	public static boolean switchCheckboxEnable(String able){
		switch (able) {
			case "True":
				return true;
			case "False":
				return false;
		}
		return false;
	}
	//订单详情界面 checkbox 设置是否可点击,背景颜色
	public static void setCheckBoxState(Context context,CheckBox cb,boolean enable){
		cb.setEnabled(enable);
		if (enable) {
//			cb.setBackgroundColor(Color.WHITE);
		} else {
//			cb.setBackgroundColor(context.getResources().getColor(R.color.order_bg_f0f3f5));
		}
	}

	// 设置 textview 左侧图标 drawleft
	public static void setDrawLeftIcon(Context context,TextView tv, int iconId){
		Drawable drawable= context.getResources().getDrawable(iconId);
		// 这一步必须要做,否则不会显示.
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		tv.setCompoundDrawables(drawable,null,null,null);
	}
}
