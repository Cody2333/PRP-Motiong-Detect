package com.cn.stepcounter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by 科迪 on 2015/4/27.
 */

public class TitleView extends FrameLayout {

    private Button leftButton;
    private Button rightButton;
    private TextView titleText;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        titleText = (TextView) findViewById(R.id.title_text);
        leftButton = (Button) findViewById(R.id.button_left);
        rightButton=(Button)findViewById(R.id.button_right);
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    public void hideRightButton(){
        rightButton.setVisibility(GONE);
    }
    public  void hideLeftButton(){
        leftButton.setVisibility(GONE);
    }
    public void setTitleText(String text) {
        titleText.setText(text);
    }

    public void setLeftButtonText(String text) {
        leftButton.setText(text);
    }

    public void setLeftButtonListener(OnClickListener l) {
        leftButton.setOnClickListener(l);
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public TextView getTitleText() {
        return titleText;
    }
    public void setRightButtonText(String text){
        rightButton.setText(text);
    }
    public Button getRightButton(){
        return rightButton;
    }
}
