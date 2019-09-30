package kr.co.exito_com.www.exito;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.co.exito_com.www.exito.recycleview_item.HLVAdapter;

public class List_ItemActivity extends AppCompatActivity {

    TextView text_start;
    TextView text_start_addre;
    TextView text_get;
    TextView text_get_addre;
    TextView text_demand;
    TextView text_space;

    public static String checkActivity = "A";

    private String item_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Toolbar toolbar = findViewById(R.id.list_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("화물배차");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button complite = findViewById(R.id.list_item_button_complite);

        Button call = findViewById(R.id.list_item_button_call);
        if(checkActivity.equals("A")) {
            call.setText("배차 요청");
        }else if (checkActivity.equals("B")){
            call.setText("배차 포기");
            complite.setVisibility(View.VISIBLE);
            complite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTranceState();
                    finish();
                }
            });
        }else if (checkActivity.equals("C")){
            call.setText("배차 삭제");
        }

        text_start = findViewById(R.id.list_item_text_start);
        text_start_addre = findViewById(R.id.list_item_text_start_addre);
        text_get = findViewById(R.id.list_item_text_get);
        text_get_addre = findViewById(R.id.list_item_text_get_addre);
        text_demand = findViewById(R.id.list_item_text_demand_sub);
        text_space = findViewById(R.id.list_item_text_space);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                item_index = null;
            }else{
                item_index = extras.getString("item_index");
            }
        }else {
            item_index = (String) savedInstanceState.getSerializable("item_index");
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onSetText();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 500);

        // 여기서 모든 데이터를 가져온다

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그
                if(checkActivity.equals("A")) {
                    onDialog_call();
                }else if(checkActivity.equals("B")) {
                    onDialog_close();
                }else if(checkActivity.equals("C")) {
                    onDialog_delet();
                }
            }
        });
    }

    @Override
    // 메뉴 백버튼(왼쪽) 활성화
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void onSetText () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_load.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);

                        text_start.setText(result.getString("start"));
                        text_start_addre.setText(result.getString("start_addre"));
                        text_get.setText(result.getString("get"));
                        text_get_addre.setText(result.getString("get_addre"));
                        text_demand.setText(result.getString("oder_content"));
                        text_space.setText("거리 : " + result.getString("space") + "Km");

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
                params.put("auto_index", item_index);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    private void onPutDate_call () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 요청 버튼을 누르면 index에 대한 항목의 check_id를 수정한다.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auto_index", item_index);
                params.put("check_id", LoginActivity.LOGIN_ID);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    private void onPutDate_close () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 포기 버튼을 누르면 index에 대한 항목의 check_id를 null로 수정한다.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auto_index", item_index);
                params.put("check_id", "null");
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    private void onPutDate_delet () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 삭제 버튼을 누르면 index에 대한 항목을 삭제한다.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_delet.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auto_index", item_index);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    private void onDialog_call (){
        new AlertDialog.Builder(List_ItemActivity.this)
                .setMessage("배차를 확정 하시겠습니까?")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                onPutDate_call();
                                finish();
                            }
                        }
                ).show();
    }
    private void onDialog_close (){
        new AlertDialog.Builder(List_ItemActivity.this)
                .setMessage("배차를 포기 하시겠습니까?")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                onPutDate_close();
                                finish();
                            }
                        }
                ).show();
    }

    private void onDialog_delet () {
        new AlertDialog.Builder(List_ItemActivity.this)
                .setMessage("배차를 삭제 하시겠습니까?")
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                onPutDate_delet();
                                finish();
                            }
                        }
                ).show();
    }

    private void onItemTranceState () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_state_update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auto_index", item_index);
                params.put("item_state", "finish");
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
}
