package xyz.kjh.pp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import xyz.juniverse.stuff.JuniverseStuff;
import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.social.LoginMethod;
import xyz.kjh.pp.BuildConfig;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.LoginParams;
import xyz.kjh.pp.service.model.res.ResponseM;

public class GateActivity extends AppCompatActivity
{
    private static final long MIN_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);

        prepareJuniverseStuff();

        // 그냥 딜레이 주자.
        findViewById(R.id.activity_gate).postDelayed(startApp, MIN_DELAY);
//        start();
    }

    private void prepareJuniverseStuff()
    {
        JuniverseStuff.init(getApplicationContext(), BuildConfig.DEBUG);

        console.i("what the hell is this?");
        FirebaseApp.initializeApp(getBaseContext());

    }

    private Runnable startApp = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };

    private void start()
    {
        if (tryLogIn())
            return;

        goToConstitution();
    }

    private boolean tryLogIn()
    {
        LoginMethod method = LoginMethod.Factory.getLastUsedMethod(this);
        if (method.isLoggedIn())
        {
            LoginParams model = new LoginParams();
            model.platform_type = method.getPlatformId();
            model.platform_id = method.getUserId();

            Server.getInstance().setLoginMethod(method);
            Server.getInstance().call("/user/login", model, new Server.ResponseListener<ResponseM>() {
                @Override
                public void onResult(ResponseM result) {
                    if (result.success)
                        goToMain();
                    else
                        goToConstitution();
                }
            });

            return true;
        }
        return false;
    }

    private void goToMain()
    {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    private void goToConstitution()
    {
        startActivity(new Intent(getBaseContext(), ConstitutionActivity.class));
        finish();
    }
}
