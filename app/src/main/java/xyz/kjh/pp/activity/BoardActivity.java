package xyz.kjh.pp.activity;

import android.content.Intent;
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

import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.list.SimpleAdapter;
import xyz.juniverse.stuff.list.SimpleHolder;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.BoardListParams;
import xyz.kjh.pp.service.model.req.CommentListParams;
import xyz.kjh.pp.service.model.res.BoardListM;
import xyz.kjh.pp.service.model.res.CommentListM;
import xyz.kjh.pp.view.UserIconView;

public class BoardActivity extends AppCompatActivity
{
    private class Holder extends SimpleHolder<BoardListM.Post>
    {
        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(BoardListM.Post data) {
            ((UserIconView)itemView.findViewById(R.id.user)).setUserInfo(data);
            ((TextView)itemView.findViewById(R.id.content)).setText(data.content);
            ((TextView)itemView.findViewById(R.id.comments)).setText("" + data.comment_cnt);
        }
    }

    private SimpleHolder.Factory factory = new SimpleHolder.Factory() {
        @Override
        public SimpleHolder onCreate(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new Holder(view);
        }
    };

    private SimpleHolder.ItemInteraction interaction = new SimpleHolder.ItemInteraction<BoardListM.Post>() {
        @Override
        public void onItemInteracted(SimpleHolder.ItemAction action, BoardListM.Post data) {
            // todo 상세 보기 화면으로...
            CommentListParams params = new CommentListParams();
            params.board_gsn = data.board_gsn;
            Server.getInstance().call("/comment/list", params, new Server.ResponseListener<CommentListM>() {
                @Override
                public void onResult(CommentListM result) {
                    console.i("comment list result================");
                }
            });
        }
    };





    public static final String ARG_GROUP_GSN = "gsn";
    public static final String ARG_GROUP_TITLE = "title";

    private BoardListM boardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(ARG_GROUP_GSN))
        {
            finish();
            return;
        }

        setContentView(R.layout.activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && intent.hasExtra(ARG_GROUP_TITLE))
            actionBar.setTitle(intent.getStringExtra(ARG_GROUP_TITLE));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_post);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        BoardListParams params = new BoardListParams();
        params.group_gsn = intent.getIntExtra(ARG_GROUP_GSN, -1);
        Server.getInstance().call("/board/list", params, new Server.ResponseListener<BoardListM>() {
            @Override
            public void onResult(BoardListM result) {
                if (result.success)
                {
                    boardList = result;
                    runOnUiThread(updateUI);
                }
                else
                    Snackbar.make(findViewById(R.id.activity_board), result.message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            ((RecyclerView)findViewById(R.id.list)).setAdapter(new SimpleAdapter(boardList.board_arr, factory, interaction));

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_write)
        {
            // todo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
