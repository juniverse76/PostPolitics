package xyz.kjh.pp.activity;

import android.os.Bundle;
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

import java.util.List;

import xyz.juniverse.stuff.console;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.res.MainM;
import xyz.kjh.pp.view.UserIconView;

public class MainActivity extends AppCompatActivity implements Server.WebSocketObserver
{
    class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.Holder>
    {
        class Holder extends RecyclerView.ViewHolder
        {
            Holder(View itemView) {
                super(itemView);
            }
        }

        private List<MainM.Group> list;
        GroupListAdapter(List<MainM.Group> list)
        {
            this.list = list;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_group, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position)
        {
            MainM.Group group = list.get(position);
            ((TextView)holder.itemView.findViewById(R.id.number)).setText("" + group.number);
            ((TextView)holder.itemView.findViewById(R.id.title)).setText(group.title);
        }

        @Override
        public int getItemCount() {
            if (list == null) return 0;
            return list.size();
        }
    }

    private MainM mainData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        registerActions();

        console.i("calling main");
        Server.getInstance().call("/main", new Server.ResponseListener<MainM>() {
            @Override
            public void onResult(MainM result) {
                if (result.success)
                {
                    mainData = result;
                    if (mainData.real_url != null)
                        Server.getInstance().connectWebSocket(mainData.real_url);

                    console.i("mainData?", mainData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
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

    private void updateUI()
    {
        console.i("updating ui!!!");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(false);
            ((UserIconView)findViewById(R.id.user_icon)).setUserInfo(mainData.user);
        }

        ((RecyclerView)findViewById(R.id.list)).setAdapter(new GroupListAdapter(mainData.group));
    }

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
