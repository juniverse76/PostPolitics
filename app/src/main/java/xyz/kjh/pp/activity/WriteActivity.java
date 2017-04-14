package xyz.kjh.pp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.InsertParams;
import xyz.kjh.pp.service.model.res.ResponseM;

public class WriteActivity extends AppCompatActivity
{
    public static final String ARG_GROUP_GSN = "gsn";
    public static final String ARG_TITLE = "title";

    private InsertParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(ARG_GROUP_GSN))
        {
            finish();
            return;
        }

        params = new InsertParams();
        // todo nickname_yn이 뭐하는건지???
//        params.nickname_yn = Director.getInstance().getUser().out_nickname;
        params.group_gsn = intent.getIntExtra(ARG_GROUP_GSN, -1);

        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && intent.hasExtra(ARG_TITLE))
            actionBar.setTitle(intent.getStringExtra(ARG_TITLE));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertPost();
            }
        });

        findViewById(R.id.post).requestFocus();
    }

    private boolean aboutToFinish = false;
    private void insertPost()
    {
        if (aboutToFinish)
            return;

        // todo write
        params.content = ((EditText)findViewById(R.id.post)).getText().toString();
        if (params.content.isEmpty())
        {
            // todo should do something more...
            return;
        }

        Server.getInstance().call("/board/insert", params, new Server.ResponseListener<ResponseM>() {
            @Override
            public void onResult(ResponseM result) {
                if (!result.success) {
                    Snackbar.make(findViewById(R.id.activity_write), result.message, Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    aboutToFinish = true;

                    View writeView = findViewById(R.id.activity_write);
                    Snackbar.make(writeView, R.string.write_success, Snackbar.LENGTH_LONG).show();
                    writeView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 1000);
                }
            }
        });
    }

}
