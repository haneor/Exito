package kr.co.exito_com.www.exito;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.widget.Toast;

import kr.co.exito_com.www.exito.recycleview_item.HLVAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Button button_on;
    Button button_add;
    Button button_his;
    Boolean stand;
    private TextView textNoList;
    private TextView textOFF;

    private ArrayList<String> aStart = new ArrayList<>();
    private ArrayList<String> aGet = new ArrayList<>();
    private ArrayList<String> aSpace = new ArrayList<>();
    private ArrayList<String> aIndex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List_ItemActivity.checkActivity = "A";

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("화물배차");
        setSupportActionBar(toolbar);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onLoadDateItem();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 500);

        textNoList = findViewById(R.id.main_text2);
        textNoList.setVisibility(View.INVISIBLE);
        textOFF = findViewById(R.id.main_text1);
        textOFF.setVisibility(View.INVISIBLE);

        recyclerView = findViewById(R.id.main_recycleView);
        button_on = findViewById(R.id.main_button_On);
        stand = false;
        button_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stand) {
                    button_on.setText("배차대기 ON");
                    stand = false;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            List_ItemActivity.checkActivity = "A";
                            onLoadDateItem();
                        }
                    };
                    Handler handler = new Handler();
                    handler.postDelayed(runnable, 500);
                    textOFF.setVisibility(View.INVISIBLE);
                }else {
                    button_on.setText("배차대기 OFF");
                    stand = true;
                    clearList();
                }
            }
        });

        button_add = findViewById(R.id.main_button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        button_his = findViewById(R.id.history_button);
        button_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        List_ItemActivity.checkActivity = "A";
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("정말 종료 하시겠습니까?")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {   }
                        })
                .setPositiveButton("종료",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                finish();
                            }
                        }
                ).show();
    }

    private void onLoadDateItem () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_main_load.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");

                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);
                        aStart.add(result.getString("start"));
                        aGet.add(result.getString("get"));
                        aSpace.add(result.getString("space"));
                        aIndex.add(result.getString("auto_index"));

                        mAdapter = new HLVAdapter(MainActivity.this, aStart, aGet, aSpace, aIndex);
                        recyclerView.setAdapter(mAdapter);
                    }

                    if(aStart.size() != 0) {
                        recyclerView.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(mLayoutManager);
                    }else {
                        textNoList.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("check_id", "null");
                params.put("item_state", "ing");
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    private void clearList() {

        aStart.clear();
        aGet.clear();
        aSpace.clear();
        aIndex.clear();

        mAdapter = new HLVAdapter(MainActivity.this, aStart, aGet, aSpace, aIndex);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        textOFF.setVisibility(View.VISIBLE);
    }
}