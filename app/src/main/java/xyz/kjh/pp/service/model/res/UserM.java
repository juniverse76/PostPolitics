package xyz.kjh.pp.service.model.res;

/**
 * Created by juniverse on 13/04/2017.
 */

public class UserM {
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

    public static class Common extends UserM
    {
        public long reg_dt;             //요청시작 시각
        public String userkey;
        public String nickname;
        public int nickname_yn;
        public int is_connect;
    }

    public void set(UserM clone)
    {
        this.sex = clone.sex;
        this.age = clone.age;
        this.party = clone.party;
        this.status = clone.status;
    }
}
