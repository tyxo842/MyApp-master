package tyxo.mobilesafe.widget.recyclerdivider.recyclerbase;
/*
 * Copyright (C) 2016 Johnny Shieh Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import tyxo.mobilesafe.R;

/**
 * description
 *
 * @author Johnny Shieh (JohnnyShieh17@gmail.com)
 * @version 1.0
 */
public class LoadMoreView extends FrameLayout {

    public static final int STATUS_INIT = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_NO_MORE = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_INIT, STATUS_LOADING, STATUS_FAIL, STATUS_NO_MORE})
    public @interface LoadStatus{}

    private @LoadStatus int mStatus = STATUS_INIT;

    LoadingIndicatorView vLoadingIndicator;
    TextView vLoadTip;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.base_load_more_content, this);
        vLoadingIndicator = (LoadingIndicatorView) findViewById(R.id.loading_indicator);
        vLoadTip = (TextView) findViewById(R.id.load_tip);

        setStatus(mStatus);
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(@LoadStatus int status) {
        mStatus = status;
        switch (status) {
            case STATUS_INIT:
                setVisibility(INVISIBLE);
                break;
            case STATUS_LOADING:
                vLoadingIndicator.setVisibility(VISIBLE);
                vLoadTip.setVisibility(INVISIBLE);
                setVisibility(VISIBLE);
                break;
            case STATUS_FAIL:
                vLoadingIndicator.setVisibility(INVISIBLE);
                vLoadTip.setText("Load failed, Please slide up to try again");
                vLoadTip.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;
            case STATUS_NO_MORE:
                vLoadingIndicator.setVisibility(INVISIBLE);
                vLoadTip.setText("There is nothing more");
                vLoadTip.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
                break;
        }
    }
}
