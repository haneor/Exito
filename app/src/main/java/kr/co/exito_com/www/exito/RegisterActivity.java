package kr.co.exito_com.www.exito;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    EditText start, start_addre, get, get_addre, sub, space, price, phone;
    String sStart, sStart_addre, sGet, sGet_addre, sSub, sSpace, sPrice, sPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("배차 등록");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button ok = findViewById(R.id.register_button_ok);

        Log.d("message_time", "date('Y-m-d')");

        start = findViewById(R.id.register_edit_start);
        start_addre = findViewById(R.id.register_edit_start_addre);
        get = findViewById(R.id.register_edit_get);
        get_addre = findViewById(R.id.register_edit_get_addre);
        sub = findViewById(R.id.register_edit_sub);
        space = findViewById(R.id.register_edit_space);
        price = findViewById(R.id.register_edit_price);
        phone = findViewById(R.id.register_edit_phone);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start.getText().length()==0 || start_addre.getText().length()==0 ||
                        get.getText().length()==0 || get_addre.getText().length()==0 ||
                        sub.getText().length()==0 || space.getText().length()==0 ||
                        price.getText().length()==0) {
                    Toast.makeText(RegisterActivity.this, "모든 항목을 입력해야 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setMessage("정말 입력 하시겠습니까?")
                            .setNegativeButton("수정",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {   }
                                    })
                            .setPositiveButton("저장",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick( DialogInterface dialog, int which )
                                        {
                                            onPutDate();
                                            finish();
                                        }
                                    }
                            ).show();
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

    private void onPutDate () {

        sStart = start.getText().toString();
        sStart_addre = start_addre.getText().toString();
        sGet = get.getText().toString();
        sGet_addre = get_addre.getText().toString();
        sSub = sub.getText().toString();
        sSpace = space.getText().toString();
        sPrice = price.getText().toString();
        sPhone = phone.getText().toString();

        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_listitem_insert.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    Log.d("message", String.valueOf(jsonArray));

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
                params.put("start", sStart);
                params.put("start_addre", sStart_addre);
                params.put("get", sGet);
                params.put("get_addre", sGet_addre);
                params.put("oder_content", sSub);
                params.put("price", sPrice);
                params.put("space", sSpace);
                params.put("create_time", " ");
                params.put("check_id", "null");
                params.put("item_state", "ing");
                params.put("phone_number", sPhone);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }
}