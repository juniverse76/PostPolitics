package xyz.kjh.pp.service.model.res;

import java.util.List;

/**
 * Created by juniverse on 13/04/2017.
 */

public class CommentListM extends ResponseM {

    public static class Post extends UserM.Common
    {
        public int group_gsn;
        public int board_gsn;
        public int comment_gsn;
        public boolean is_me;
    }

    public List<CommentListM.Post> comment_arr;
}
