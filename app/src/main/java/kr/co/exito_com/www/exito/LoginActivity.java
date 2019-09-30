package kr.co.exito_com.www.exito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.util.regex.Pattern;

import kr.co.exito_com.www.exito.request.LoginRequest;

public class LoginActivity extends AppCompatActivity {

    public static String LOGIN_ID;

    String login_id;
    String login_pw;
    EditText id;
    EditText pw;
    CheckBox auto_checkBox;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = findViewById(R.id.login_toolbar);
        toolbar.setTitle("로그인");
        toolbar.setTitleTextColor(0xAAFFFFFF);
        setSupportActionBar(toolbar);

        id = findViewById(R.id.login_edit_id);
        id.setFilters(new InputFilter[] {filterAlphaNumber});
        pw = findViewById(R.id.login_edit_pw);
        Button login = findViewById(R.id.login_button);

        checkBox = findViewById(R.id.login_checkBox);

        // 자동 저장 관련 데이터 저장
        auto_checkBox = findViewById(R.id.auto_login_checkBox);

        loadPref();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_id = id.getText().toString();
                login_pw = pw.getText().toString();

                if(login_id.equals("") || login_pw.equals("") || !checkBox.isChecked()) {
                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요.\n개인정보동의 후 사용이 가능합니다.", Toast.LENGTH_SHORT).show();
                }else if(checkBox.isChecked()) {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Log.d("response", String.valueOf(jsonResponse));
                                if(success) {
                                    LOGIN_ID = login_id;

                                    if(auto_checkBox.isChecked()) {
                                        savePref_true(login_id, login_pw);
                                    }else{
                                        savePref_false();
                                    }

                                    startActivity(new Intent(LoginActivity.this, PhoneActivity.class));
                                    finish();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("계정을 다시 확인하세요.").setNegativeButton("다시 시도",null).show();
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(login_id, login_pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            }
        });

        TextView consent = findViewById(R.id.login_consent);
        consent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ConsentActivity.class));
            }
        });

    }

    // 영문 및 숫자만 입력이 가능하도록 하는 메소드 생성
    public InputFilter filterAlphaNumber = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 아이디 및 비번 내부 저장
    public void savePref_true(String ID, String PW) {
        SharedPreferences pref =getSharedPreferences("Exito", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id", ID ); //키값, 저장값
        editor.putString("user_pw", PW );

        if(auto_checkBox.isChecked()) {
            editor.putBoolean("user_login", true);
            editor.putBoolean("auto_check", true);
        }
        editor.commit();
    }

    public void savePref_false() {
        SharedPreferences pref =getSharedPreferences("Exito", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id", "" ); //키값, 저장값
        editor.putString("user_pw", "" );

        if(!auto_checkBox.isChecked()) {
            editor.putBoolean("user_login", false);
        }
        editor.commit();
    }

    public void loadPref() {
        SharedPreferences prefs =getSharedPreferences("Exito", MODE_PRIVATE);
        String user_id = prefs.getString("user_id", ""); //키값, 값이 없을때 기본값
        String user_pw = prefs.getString("user_pw", "");
        Boolean user_login = prefs.getBoolean("user_login", false);
        Boolean auto_check = prefs.getBoolean("auto_check", false);

        id.setText(user_id);
        pw.setText(user_pw);
        auto_checkBox.setChecked(user_login);
        checkBox.setChecked(auto_check);

    }
}
