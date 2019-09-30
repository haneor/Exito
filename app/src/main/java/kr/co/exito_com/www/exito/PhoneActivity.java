package kr.co.exito_com.www.exito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PhoneActivity extends AppCompatActivity {

    EditText phone;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Toolbar toolbar = findViewById(R.id.phone_toolbar);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("휴대폰 번호 확인");
        setSupportActionBar(toolbar);

        onLoadDate();

        phone = findViewById(R.id.phone_editText);

        Button ok = findViewById(R.id.phone_button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone.getText().toString().equals(phone_number)) {
                    Log.d("message", phone_number);
                    startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                    finish();
                }

//                startActivity(new Intent(PhoneActivity.this, MainActivity.class));
//                finish();
            }
        });
    }

    private String onLoadDate () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_phone.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject_response = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonObject_response.get("response");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject result = (JSONObject) jsonArray.get(i);

                        phone.setText(result.getString("db_phone")); // 서버에서 아이디에 대한 전화번호를 가져온다.

                        phone_number = result.getString("db_phone");

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
                params.put("db_id", LoginActivity.LOGIN_ID);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);

        return phone_number;
    }


}
