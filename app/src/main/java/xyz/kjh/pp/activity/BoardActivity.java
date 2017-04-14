package xyz.kjh.pp.activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.list.SimpleAdapter;
import xyz.juniverse.stuff.list.SimpleHolder;
import xyz.kjh.pp.Director;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.BoardListParams;
import xyz.kjh.pp.service.model.req.CommentListParams;
import xyz.kjh.pp.service.model.req.InsertParams;
import xyz.kjh.pp.service.model.res.BoardListM;
import xyz.kjh.pp.service.model.res.CommentListM;
import xyz.kjh.pp.service.model.res.ResponseM;
import xyz.kjh.pp.view.UserIconView;

public class BoardActivity extends AppCompatActivity
{
    private class Holder extends SimpleHolder<BoardListM.Post>
    {
        BoardListM.Post data;
        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(BoardListM.Post data) {
            this.data = data;
            ((UserIconView)itemView.findViewById(R.id.user)).setUserInfo(data);
            ((TextView)itemView.findViewById(R.id.content)).setText(data.content);
            ((TextView)itemView.findViewById(R.id.comments)).setText("" + data.comment_cnt);
            itemView.setOnClickListener(listener);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    private class WriteHolder extends SimpleHolder<WritePost>
    {
        public WriteHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(final WritePost data)
        {
            ((UserIconView)itemView.findViewById(R.id.user)).setUserInfo(data);
            itemView.findViewById(R.id.write).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editor = (EditText)itemView.findViewById(R.id.content);
                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editor.getWindowToken(), 0);

                    write(data, editor.getText().toString());
                }
            });
        }
    }

    private SimpleHolder.Factory factory = new SimpleHolder.Factory<BoardListM.Post>() {
        @Override
        public SimpleHolder onCreate(ViewGroup parent, int viewType) {
            if (viewType == 0)
                return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
            else
                return new WriteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_write, parent, false));
        }

        @Override
        public int getViewType(BoardListM.Post data) {
            if (data instanceof WritePost)
                return 1;
            else
                return 0;
        }
    };

    private class WritePost extends BoardListM.Post { }




    public static final String ARG_GROUP_GSN = "gsn";
    public static final String ARG_GROUP_TITLE = "title";
    public static final int REQ_WRITE = 8471;

    private BoardListM boardList;
    private int group_gsn;
    private String title;

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

        recyclerView = (RecyclerView) findViewById(R.id.list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        group_gsn = intent.getIntExtra(ARG_GROUP_GSN, -1);
        title = intent.getStringExtra(ARG_GROUP_TITLE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_post);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        refreshList();
    }

    private void refreshList()
    {
        BoardListParams params = new BoardListParams();
        params.group_gsn = group_gsn;
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

    private SimpleAdapter adapter;
    private RecyclerView recyclerView;
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            adapter = new SimpleAdapter(boardList.board_arr, factory);
            recyclerView.setAdapter(adapter);

        }
    };

    MenuItem item;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board, menu);
        item = menu.findItem(R.id.action_write);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_write)
        {
            if (!writingMode) {
                startWrite();
            }
            else {
                cancelWrite();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_WRITE && resultCode == RESULT_OK)
        {
            // todo refresh
            refreshList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean writingMode = false;
    private void startWrite()
    {
//        Intent intent = new Intent(getBaseContext(), WriteActivity.class);
//        intent.putExtra(WriteActivity.ARG_GROUP_GSN, group_gsn);
//        intent.putExtra(WriteActivity.ARG_TITLE, title);
//        startActivityForResult(intent, REQ_WRITE);

        writingMode = true;
        item.setIcon(R.drawable.ic_close);

        WritePost writePost = new WritePost();
        writePost.group_gsn = group_gsn;
        writePost.set(Director.getInstance().getUser());
        boardList.board_arr.add(0, writePost);

        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    private void cancelWrite()
    {
        writingMode = false;
        item.setIcon(R.drawable.ic_create);

        BoardListM.Post post = boardList.board_arr.get(0);
        if (post instanceof WritePost)
        {
            boardList.board_arr.remove(0);
            adapter.notifyItemRemoved(0);
        }
    }

    private void write(final WritePost data, String content)
    {
        InsertParams params = new InsertParams();
        params.group_gsn = data.group_gsn;
        params.content = content;
        Server.getInstance().call("/board/insert", params, new Server.ResponseListener<ResponseM>() {
            @Override
            public void onResult(ResponseM result) {
                if (!result.success) {
                    Snackbar.make(findViewById(R.id.activity_board), result.message, Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Snackbar.make(findViewById(R.id.activity_board), R.string.write_success, Snackbar.LENGTH_LONG).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // todo close
                            cancelWrite();
                            refreshList();
                        }
                    });
                }
            }
        });
    }
}
