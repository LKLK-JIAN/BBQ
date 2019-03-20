package com.example.benetech.bbq.wight;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.benetech.bbq.R;


public class MenuPopupContent extends PopupWindow {

    private View mMenuView;
    private LinearLayout setting,save,clear;

    public MenuPopupContent(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.menu_content_item, null);
        save=mMenuView.findViewById(R.id.menu_curve);
        save.setOnClickListener(itemsOnClick);
        setting=mMenuView.findViewById(R.id.menu_datalist);
        setting.setOnClickListener(itemsOnClick);
        clear=mMenuView.findViewById(R.id.menu_import);
        clear.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction()== MotionEvent.ACTION_UP) {
                    if (y < height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
