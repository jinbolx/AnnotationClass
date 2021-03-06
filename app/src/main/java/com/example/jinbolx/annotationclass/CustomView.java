package com.example.jinbolx.annotationclass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jinbolx.ioc.InjectUtil;
import com.jinbolx.ioc_annotation.BindView;
import com.jinbolx.ioc_annotation.OnClick;

public class CustomView extends RelativeLayout {
    @BindView(R.id.tv)
    TextView textView;
    @BindView(R.id.iv)
    ImageView imageView;
    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(final Context context){
        View.inflate(context,R.layout.custom_layout,this);
        InjectUtil.inject(this);
//        textView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
    @OnClick({R.id.tv})
    public void onclick(View view){
        if (view.getId()==R.id.tv){
            Toast.makeText(getContext(),"click",Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.b);
        }else if (view.getId()==R.id.iv){
            Toast.makeText(getContext(),"imageView",Toast.LENGTH_SHORT).show();
        }
    }
}
