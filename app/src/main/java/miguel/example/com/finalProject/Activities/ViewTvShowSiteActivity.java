package miguel.example.com.finalProject.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import miguel.example.com.finalProject.FirebaseServices;
import miguel.example.com.finalProject.R;

public class ViewTvShowSiteActivity extends AppCompatActivity {
    private String showOfficialURL;
    private String showURL;
    private String showName;
    private WebView myWebView;
    private ProgressBar mProgressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tv_show_site);

        showOfficialURL = getIntent().getStringExtra("officialSite");
        showURL = getIntent().getStringExtra("url");
        showName = getIntent().getStringExtra("name");
        FirebaseServices.getInstance(this).addToRoutine("Viste " + showName, "Ver TV");
        WebView myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(showOfficialURL);
        mProgressBar = findViewById(R.id.progressBar);
        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
                //Toast.makeText(ViewTvShowSiteActivity.this, "Estoy lleando aqui", Toast.LENGTH_SHORT).show();
                super.onPageCommitVisible(view, url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //mProgressBar.setVisibility(View.GONE);
                //Toast.makeText(ViewTvShowSiteActivity.this, "Estoy lleando aqui", Toast.LENGTH_SHORT).show();
                super.onPageFinished(view, url);
            }

        });
    }
}
