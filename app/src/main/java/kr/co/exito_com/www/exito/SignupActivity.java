package kr.co.exito_com.www.exito;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SignupActivity extends AppCompatActivity {


    private EditText PhonNumber;
    private EditText name;
    private EditText password;
    private Button signup;
    private Button validateButton;

    private String db_id;
    private String db_pw;
    private String db_phone;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("회원가입");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PhonNumber = findViewById(R.id.signyupActivity_edittext_nickname);
        name = findViewById(R.id.signyupActivity_edittext_name);
        password = findViewById(R.id.signyupActivity_edittext_password);
        signup = findViewById(R.id.signyupActivity_button_signup);
        validateButton = findViewById(R.id.validateButton);
        // 판정 버튼
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db_phone = PhonNumber.getText().toString();
                db_id = name.getText().toString();
                db_pw = password.getText().toString();
                vailDateID();

            }
        });
        // 회원 가입 버튼
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SignupActivity" , "회원가입 완료");

                db_phone = PhonNumber.getText().toString();
                db_id = name.getText().toString();
                db_pw = password.getText().toString();
                joinID();
            }
        });
    }

    public void vailDateID () {
        if(validate){
            return;
        }else if(db_id.equals("") || db_pw.equals("") || db_phone.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("ID, PASSWORD, EMAIL은 반드시 입력해 주세요.").setPositiveButton("확인", null).show();
            return;
        }
        onCheckID();
    }

    private void onCheckID () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_id_check.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("사용할 수 있는 아이디 입니다.").setPositiveButton("확인", null).show();
                        name.setEnabled(false);
                        validate = true;
                        name.setBackgroundColor(Color.GRAY);
                        validateButton.setBackgroundColor(Color.GRAY);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("사용할 수 없는 아이디 입니다.").setNegativeButton("확인", null).show();
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
                params.put("db_id", db_id);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
    }

    public void joinID () {
        if(!validate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("먼저 중복 체크를 해주세요.").setNegativeButton("확인", null).show();
            return;
        }else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    onSignup();
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable, 500);

            finish();
        }
    }

    private void onSignup () {
        RequestQueue postRequestQueue = Volley.newRequestQueue(this);
        // 서버에 php파일 올리고 서버 주소 변경하기.
        StringRequest postStringRequest = new StringRequest(Request.Method.POST, "http://eor0601.dothome.co.kr/exito/exito_signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("회원가입에 성공하였습니다.").setPositiveButton("확인", null).show();
                        finish();
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage("회원가입에 실패하셨습니다.").setPositiveButton("확인", null).show();
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
                params.put("db_id", db_id);
                params.put("db_pw", db_pw);
                params.put("db_phone", db_phone);
                return params;
            }
        };
        postRequestQueue.add(postStringRequest);
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
}
