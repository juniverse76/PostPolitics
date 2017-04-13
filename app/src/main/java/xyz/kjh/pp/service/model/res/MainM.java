package xyz.kjh.pp.service.model.res;

import java.util.List;

/**
 * Created by juniverse on 24/03/2017.
 */

public class MainM extends ResponseM
{
    public static class User extends UserM
    {
        public int out_nickname;
        public int total_payment;
    }

    public static class Group
    {
        public int group_gsn;
        public int number;
        public String title;
        public int comment_yn;
        public int out_yn;
        public int cnt;
        public long reg_date;
    }

    public static class Friend extends UserM.Common
    {
        public int relation_state;      //관계상태(0:요청함, 1:요청받음, 2:친구)
        public long upd_dt;             //친구된 시각
    }

    public User user;
    public List<Group> group;
    public List<Friend> friend_arr;
    public String real_url;
}
