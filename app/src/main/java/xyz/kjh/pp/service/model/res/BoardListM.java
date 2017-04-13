package xyz.kjh.pp.service.model.res;

import java.util.List;

/**
 * Created by juniverse on 13/04/2017.
 */

public class BoardListM extends ResponseM
{
    public static class Post extends UserM.Common
    {
        public int group_gsn;
        public boolean is_good;
        public boolean is_hate;
        public int good;
        public int board_gsn;
        public String content;
        public int comment_cnt;
        public boolean is_me;
        public int hate;
    }

    public List<Post> board_arr;
}
