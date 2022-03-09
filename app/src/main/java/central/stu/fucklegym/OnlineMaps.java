package central.stu.fucklegym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import fucklegym.top.entropy.NetworkSupport;

class GetOnlineMaps extends Thread{
    Handler handler;
    JSONObject maps;
    Message message;
    public GetOnlineMaps(Handler handler){
        this.handler = handler;
        message = handler.obtainMessage();
    }

    @Override
    public void run() {
        try {
            maps = NetworkSupport.getForReturn("https://foreverddb.github.io/FuckLegym/msg.json", new HashMap<String, String>());
            message.what = OnlineMaps.SUCCESS;
            message.obj = maps;
            handler.sendMessage(message);
        }catch (IOException e){
            e.printStackTrace();
            handler.sendEmptyMessage(OnlineMaps.FAIL);
        }
    }
}

public class OnlineMaps extends AppCompatActivity {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int IMPORTSUCCESS = 2;
    public JSONObject maps;
    private ArrayList<String> selectedMaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_maps);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case SUCCESS:
                        maps = (JSONObject) msg.obj;
                        LinearLayout layout = (LinearLayout) findViewById(R.id.activities_layout);
                        for(String str: maps.keySet()){
                            CheckBox checkBox = (CheckBox) View.inflate(OnlineMaps.this, R.layout.checkbox, null);
                            checkBox.setText(str);
                            layout.addView(checkBox);
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        selectedMaps.add(str);
                                    }else {
                                        selectedMaps.remove(str);
                                    }
                                    Log.d("acac", "onClick: " + selectedMaps);
                                }
                            });
                        }
                        break;
                    case FAIL:
                        Toast.makeText(OnlineMaps.this, "加载错误...", Toast.LENGTH_SHORT).show();
                }
            }
        };

        new GetOnlineMaps(handler).start();
    }
}