package xyz.kjh.pp.service.model.res;

import java.util.List;

/**
 * Created by juniverse on 24/03/2017.
 */

public class MainM extends ResponseM
{
    public static class UserCommon
    {
        public static final int GENDER_NONE = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static final int STATUS_NONE = 0;
        public static final int STATUS_PROGRESSIVE = 1;
        public static final int STATUS_CONSERVATIVE = 2;

        public int sex;
        public int age;
        public int party;
        public int status;
    }

    public static class User extends UserCommon
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

    public static class Friend extends UserCommon
    {
        public String userkey;
        public String nickname;
        public int nickname_yn;
        public int relation_state;      //관계상태(0:요청함, 1:요청받음, 2:친구)
        public int is_connect;
        public long reg_dt;             //요청시작 시각
        public long upd_dt;             //친구된 시각
    }

    public User user;
    public List<Group> group;
    public List<Friend> friend_arr;
    public String real_url;
}
