package kr.co.exito_com.www.exito.request;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class LoginRequest extends StringRequest {

    // 서버에 php파일 올리고 주소 변경하기.
    final static private String URL = "http://eor0601.dothome.co.kr/exito/exitoLogin.php";
    private Map<String, String> parameters;

    public LoginRequest(String db_id, String db_pw, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("db_id", db_id);
        parameters.put("db_pw", db_pw);

    }

    public Map<String, String> getParams() {
        return parameters;
    }

}

