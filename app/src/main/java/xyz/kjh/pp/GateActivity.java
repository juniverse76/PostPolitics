package xyz.kjh.pp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import xyz.juniverse.stuff.JuniversStuff;
import xyz.juniverse.stuff.console;

public class GateActivity extends AppCompatActivity
{
    private static final long MIN_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate);

        prepareJuniverseStuff();

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                startActivity(new Intent(getBaseContext(), ConstitutionActivity.class));
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        }.sendEmptyMessageDelayed(1, MIN_DELAY);
    }

    private void prepareJuniverseStuff()
    {
        JuniversStuff.init(getApplicationContext(), BuildConfig.DEBUG);
        JuniversStuff.initRPC(getString(R.string.server_url));

        console.i("what the hell is this?");
        FirebaseApp.initializeApp(getBaseContext());

    }

}
