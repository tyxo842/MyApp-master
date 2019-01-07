package tyxo.mobilesafe.widget.recyclerdivider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import tyxo.mobilesafe.activity.StaggeredGridLayoutActivity;

/**
 * Created by LY on 2016/7/21 11: 26.
 * Mail      1577441454@qq.com
 * Describe :
 */
public class ClickableSpanMy extends ClickableSpan {

    private Context context;
    private String content;

    public ClickableSpanMy(Context context,String content){
        this.context = context;
        this.content = content;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(context, StaggeredGridLayoutActivity.class);
        Bundle bun = new Bundle();
        bun.putString("content",content);
        intent.putExtras(bun);
        context.startActivity(intent);
    }
}















