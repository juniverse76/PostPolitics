package xyz.kjh.pp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.list.SimpleAdapter;
import xyz.juniverse.stuff.list.SimpleHolder;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.BoardListParams;
import xyz.kjh.pp.service.model.res.MainM;
import xyz.kjh.pp.service.model.res.ResponseM;
import xyz.kjh.pp.service.model.res.UserM;
import xyz.kjh.pp.view.UserIconView;

public class MainActivity extends AppCompatActivity implements Server.WebSocketObserver
{
    private class MainViewHolder extends SimpleHolder<MainM.Group>
    {
        public MainViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(MainM.Group data) {
            ((TextView)itemView.findViewById(R.id.number)).setText("" + data.number);
            ((TextView)itemView.findViewById(R.id.title)).setText(data.title);
        }
    }

    private SimpleHolder.Factory viewFactory = new SimpleHolder.Factory() {
        @Override
        public SimpleHolder onCreate(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_group, parent, false);
            return new MainViewHolder(view);
        }
    };

    private SimpleHolder.ItemInteraction interaction = new SimpleHolder.ItemInteraction<MainM.Group>() {
        @Override
        public void onItemInteracted(SimpleHolder.ItemAction action, MainM.Group data) {
            Intent intent = new Intent(getBaseContext(), BoardActivity.class);
            intent.putExtra(BoardActivity.ARG_GROUP_GSN, data.group_gsn);
            intent.putExtra(BoardActivity.ARG_GROUP_TITLE, data.title);
            startActivity(intent);
        }
    };






    private MainM mainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(false);
        }

        registerActions();

        Server.getInstance().call("/main", new Server.ResponseListener<MainM>() {
            @Override
            public void onResult(MainM result) {
                if (result.success)
                {
                    mainData = result;
                    if (mainData.real_url != null)
                        Server.getInstance().connectWebSocket(mainData.real_url);

                    runOnUiThread(updateUI);
                }
                else
                    Snackbar.make(findViewById(R.id.activity_main), result.message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void registerActions()
    {
        findViewById(R.id.user_icon).setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.user_icon)
            {
                // todo 쪽지 보내기??
            }
        }
    };

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            console.i("updating ui!!!");
            mainData.user.sex = UserM.GENDER_FEMALE;
            mainData.user.status = UserM.STATUS_PROGRESSIVE;
            ((UserIconView)findViewById(R.id.user_icon)).setUserInfo(mainData.user);
            // todo 이건 서버에서 데이터를 다르게 줘야 한다...

            ((RecyclerView)findViewById(R.id.list)).setAdapter(new SimpleAdapter(mainData.group, viewFactory, interaction));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Server.getInstance().addWebSocketCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Server.getInstance().removeWebSocketCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_write:
            {
                break;
            }
            case R.id.action_reply:
            {
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessage(String message)
    {
        console.i("got ws message: ", message);
    }
}
