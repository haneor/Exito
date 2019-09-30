package kr.co.exito_com.www.exito;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;

public class ConsentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent);

        String url = "http://www.exito-com.co.kr/world/bbs/content.php?co_id=privacy";

        Toolbar toolbar = findViewById(R.id.consent_toolbar);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("개인정보동의서 확인");
        setSupportActionBar(toolbar);

        WebView webView = findViewById(R.id.consent_webview);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }
}
