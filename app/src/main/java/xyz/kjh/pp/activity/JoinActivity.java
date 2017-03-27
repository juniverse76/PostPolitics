package xyz.kjh.pp.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.social.LoginMethod;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.JoinM;
import xyz.kjh.pp.service.model.res.ResponseM;

public class JoinActivity extends AppCompatActivity
{
    LoginMethod method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        method = Server.getInstance().getLoginMethod();
        if (method == null)
        {
            console.d("SOMETHIGN WRONG!! no login method???");
            finish();
            return;
        }
        setContentView(R.layout.activity_join);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.additional_info);
    }

    public void join(View button)
    {
        JoinM model = new JoinM();

        Server server = Server.getInstance();

        model.phone_number = ((EditText)findViewById(R.id.field_phone_no)).getText().toString();
        model.age = ((Spinner)findViewById(R.id.field_age)).getSelectedItemPosition() * 10;
        model.platform_type = method.getPlatformId();
        model.platform_id = method.getUserId();

        Server.getInstance().call("/user/join", model, new Server.ResponseListener<ResponseM>() {
            @Override
            public void onResult(ResponseM result) {
                if (!result.success)
                    Snackbar.make(findViewById(R.id.activity_join), result.message, Snackbar.LENGTH_LONG).show();
                else
                    goToMain();
            }
        });
    }

    private void goToMain()
    {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }
}
