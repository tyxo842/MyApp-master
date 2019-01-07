package tyxo.mobilesafe.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup{
	private int horizontalSpacing = 15;//水平间距
	private int verticalSpacing = 15;//行与行之间的垂直间距
	
	//用于存放所有的Line对象
	private ArrayList<Line> lineList = new ArrayList<Line>();
	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}
	/**
	 * 设置子TextView之间的水平间距
	 * @param horizontalSpacing
	 */
	public void setHorizontalSpacing(int horizontalSpacing){
		this.horizontalSpacing = horizontalSpacing;
	}
	
	/**
	 * 设置行与行之间的垂直间距
	 */
	public void setVerticalSpacing(int verticalSpacing){
		this.verticalSpacing = verticalSpacing;
	}
	
	
	/**
	 * 遍历所有的TextView，进行分行的逻辑，相当于排座位表
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//由于onMeasure可能被多次调用，所有先清空lineList
		lineList.clear();
		
		//1.获取当前FlowLayout的总宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		//2.获取实际用于比较的宽度，就是减去左右的padding值的宽度
		int noPaddingWidth = width-getPaddingLeft()-getPaddingRight();
		
		//3.遍历所有的TextView，在遍历过程中进行比较
		Line line = new Line();//预先创建Line对象，用于存放TextView
		for (int i = 0; i < getChildCount(); i++) {
			View childView = getChildAt(i);//获取当前的子View
			//会调用childView的内部的measure方法
			childView.measure(0, 0);//当它发现传入0是非法规则，将会按照控件本身的布局参数测量
			
			//4.如果当前Line中木有TextView，则直接将childView加入到line中,要保证每个line至少有一个
			if(line.getViewList().size()==0){
				line.addView(childView);//存放childView
			}else if (line.getWidth()+horizontalSpacing+childView.getMeasuredWidth()>noPaddingWidth) {
				//5.拿当前line的width+水平间距+childView的宽和noPaddingWidth进行比较,如果大于，则需要将childView
				//放入下一行,但是需要先保存上一个line对象
				lineList.add(line);//将旧的line对象保存
				
				line = new Line();//重新创建line
				line.addView(childView);//将childView放入新的Line
			}else {
				//6.如果小于noPaddingWidth，则将childView放入当了line
				line.addView(childView);
			}
			//7.如果当前childView是最后一个，会造成最后的Line对象丢失
			if(i==(getChildCount()-1)){
				//如果是最后一个，则手动将最后的Line对象保存
				lineList.add(line);
			}
		}
		//for循环结束了，lineList存放了所有的line对象，而每个line对象存放对应行的TextView
		//由于下面会需要摆放所有Line的TextView，所以要设置自己的宽高
		int height = getPaddingTop()+getPaddingBottom();//先计算上下的padding值
		for (int i = 0; i < lineList.size(); i++) {
			height += lineList.get(i).getHeight();//再计算上所有Line的高度
		}
		height += (lineList.size()-1)*verticalSpacing;//最后再算上行与行之间的垂直间距
		//设置自己的宽高
		setMeasuredDimension(width, height);
	}
	
	/**
	 * 当排完座位表，每个人需要安装座位表坐到自己的位置上：
	 * 将lineList中的每个line的所有TextView摆放到对应的位置上
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);//获取line对象
			
			//从第二行开始，每行的top都比上一行多个行高+垂直间距
			if(i>0){
				paddingTop += lineList.get(i-1).getHeight()+verticalSpacing;
			}
			
			ArrayList<View> viewList = line.getViewList();//获取line的TextView的集合
			//1.计算当前line的留白的区域
			float remainSpacing = getLineRemainSpacing(line);
			//2.计算每个TextView平均分到的值
			float perSpacing = remainSpacing/viewList.size();
			for (int j = 0; j < viewList.size(); j++) {
				View childView = viewList.get(j);
				//3.将perSapcing增加到childView原来的宽度上
				int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpacing)
						, MeasureSpec.EXACTLY);
				childView.measure(widthMeasureSpec,0);//重新测量childVIew
				
				if(j==0){
					//摆放第一个TextView，需要靠左边摆放
					childView.layout(paddingLeft,paddingTop,paddingLeft+childView.getMeasuredWidth()
							,paddingTop+childView.getMeasuredHeight());
				}else {
					//后面的TextView都可以参考前一个TextView
					View preView = viewList.get(j-1);//获取前一个TextView对象
					int left = preView.getRight()+horizontalSpacing;//获取自身的left
					childView.layout(left,preView.getTop(),left+childView.getMeasuredWidth()
							,preView.getBottom());
				}
			}
		}
	}
	/**
	 * 获取指定line的留白
	 * @return
	 */
	private float getLineRemainSpacing(Line line){
		//noPaddingWidth-line的宽度
		return getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getWidth();
	}
	
	/**
	 * 封装每一行的数据，就是每行存放哪几个TextView
	 * @author Administrator
	 *
	 */
	class Line{
		private ArrayList<View> viewList = new ArrayList<View>();//用于存放当前行的所有的TextView
		private int width;//行宽，表示当前行所有TextView的宽度+水平间距
		private int height;//行的高度
		
		/**
		 * 存放TextView
		 * @param view
		 */
		public void addView(View view){
			if(!viewList.contains(view)){
				//更新width
				if(viewList.size()==0){
					//说明木有TextView,当前的view是第一个
					width = view.getMeasuredWidth();
				}else {
					//如果已经有TextView，则应该在原来的基础上+水平间距+view的宽
					width += horizontalSpacing+view.getMeasuredWidth();
				}
				//更新height,谁大就是谁的高度
				height = Math.max(height, view.getMeasuredHeight());
				
				viewList.add(view);
			}
		}
		
		public ArrayList<View> getViewList(){
			return viewList;
		}
		public int getWidth(){
			return width;
		}
		public int getHeight(){
			return height;
		}
	}
	
}
