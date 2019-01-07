package tyxo.mobilesafe.activity.activityGrid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tyxo.mobilesafe.R;
import tyxo.mobilesafe.activity.ScanActivity;
import tyxo.mobilesafe.utils.ToastUtil;
import tyxo.mobilesafe.widget.arcmenu.ArcMenu;
import tyxo.mobilesafe.widget.arcmenu.RayMenu;

/**
 * Created by LY on 2016/7/29 16: 04.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class Aciticity2 extends Activity implements View.OnClickListener {

    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with};

    private TextView tv_test_scan;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gride_2);

        initView();
        initListener();
        initData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_test_scan:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initView() {
        ArcMenu arcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);
        initArcMenu(arcMenu, ITEM_DRAWABLES);
        initArcMenu(arcMenu2, ITEM_DRAWABLES);

        tv_test_scan = (TextView) findViewById(R.id.tv_test_scan);
    }

    private void initListener() {
        tv_test_scan.setOnClickListener(this);
    }

    private void initData() {
        RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
        final int itemCount = ITEM_DRAWABLES.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(ITEM_DRAWABLES[i]);

            final int position = i;
            rayMenu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ToastUtil.showToastS(Aciticity2.this, "position:" + position);
                }
            });// Add a menu item
        }
    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ToastUtil.showToastS(Aciticity2.this, "position:" + position);
                }
            });
        }
    }

}
