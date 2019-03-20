package com.example.benetech.bbq.wight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.benetech.bbq.R;


public class ShoppingCartView extends LinearLayout {
    private TextView tv_shopping_cart_subtract,tv_shopping_cart_plus;
    private EditText et_shopping_cart_num;
    private int num=1;
    private View view;

    public ShoppingCartView(Context context) {
    this(context,null);
    }

    public ShoppingCartView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ShoppingCartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        view= LayoutInflater.from(context).inflate(R.layout.shoppint_cart_view,this,true);
        tv_shopping_cart_subtract=view.findViewById(R.id.tv_shopping_cart_subtract);
        tv_shopping_cart_plus=view.findViewById(R.id.tv_shopping_cart_plus);
        et_shopping_cart_num=view.findViewById(R.id.et_shoppping_cart_num);

        et_shopping_cart_num.setText(num+"");

        tv_shopping_cart_subtract.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                num--;
                et_shopping_cart_num.setText(num+"");
                if (numlistener!=null){
                    numlistener.getNum(num);
                }
            }
        });
        tv_shopping_cart_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                et_shopping_cart_num.setText(num+"");
                if(numlistener!=null){
                    numlistener.getNum(num);
                }
            }
        });
    }
    private NumListener numlistener;
    public void setNumListener(NumListener numlistener){
        this.numlistener=numlistener;
    }
    public interface NumListener {
        void getNum(int num);
    }
}
