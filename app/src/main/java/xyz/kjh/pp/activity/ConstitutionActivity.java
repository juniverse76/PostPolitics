package xyz.kjh.pp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import xyz.juniverse.stuff.console;
import xyz.kjh.pp.BuildConfig;
import xyz.kjh.pp.R;
import xyz.kjh.pp.popup.LoginPopup;

public class ConstitutionActivity extends AppCompatActivity
{
    private static final String CONSTITUTION_FILENAME = "/resources/constitution.html";

    private static class Scroller extends Handler
    {
        private static final long DELAY_BEFORE_SCROLL = 3000;
        private static final int SCROLL_DISTANCE = 2;


        private WebView scrollView;
        private OnScrollEnd listener;

        interface OnScrollEnd
        {
            void onEnd();
        }

        Scroller(WebView scrollView, OnScrollEnd listener)
        {
            this.scrollView = scrollView;
            this.listener = listener;
        }

        @Override
        public void handleMessage(Message msg)
        {
            scrollView.scrollBy(0, SCROLL_DISTANCE);

            if (scrollView.getHeight() + scrollView.getScrollY() < scrollView.getContentHeight())
                sendEmptyMessageDelayed(1, 20);
            else
                if (listener != null) listener.onEnd();
        }

        void start()
        {
            if (BuildConfig.DEBUG) return;
            sendEmptyMessageDelayed(1, DELAY_BEFORE_SCROLL);
        }

        void pause()
        {
            removeMessages(1);
        }
    }

    private Scroller scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constitution);

        final WebView webView = (WebView) findViewById(R.id.constitution);

        findViewById(R.id.click_interface).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });

        webView.loadUrl(getString(R.string.server_url) + CONSTITUTION_FILENAME);
        webView.setVerticalScrollBarEnabled(false);

        scroller = new Scroller(webView, new Scroller.OnScrollEnd() {
            @Override
            public void onEnd() {
                nextStep();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        scroller.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scroller.start();
    }

    private void nextStep()
    {
        // todo
        console.i("done");
        LoginPopup.instance().show(getSupportFragmentManager(), "login");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("login");
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
    }
}
