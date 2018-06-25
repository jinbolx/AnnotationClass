package com.example.jinbolx.annotationclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.jinbolx.ioc.InjectUtil;
import com.jinbolx.ioc_annotation.BindView;
import com.jinbolx.ioc_annotation.OnClick;

public class MainActivity extends AppCompatActivity {
   // @BindView(R.id.csv)
    //CustomView customView;
    @BindView(R.id.bt)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.inject(this);

    }
    @OnClick(R.id.bt)
    public void onItemClick(View v){
        if (v.getId()==R.id.bt){
            Toast.makeText(getApplicationContext(),"toast",Toast.LENGTH_SHORT).show();
        }
    }
}
