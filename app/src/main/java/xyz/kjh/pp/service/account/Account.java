package xyz.kjh.pp.service.account;

/**
 * Created by juniverse on 20/03/2017.
 */

public class Account
{
    public static class Data
    {
        public String clientVersionCode;        // 클라이언트 버전(형식없음)
        public int platform_type;               // 플랫폼 타입(1:페이스북,2:GUEST,3:GameCenter(IOS),4:GooglePlus(AOS),99:구분 불가능)
        public int platform_id;                 // 아이디
        public String push_token;               // 푸시 토큰
        public int push_type;                   // push 수신 여부 (0:전체거부(항상), 1:야간만 허용, 2:주간만 허용, 3:수신허용(항상))
        public int sms_type;                    // SMS 수신 여부 (0:전체거부(항상), 1:야간만 허용, 2:주간만 허용, 3:수신허용(항상))
        public int join_os_type;                // 가입시 OS타입(1:IOS, 2:Android, 3:Tizen, 4:Windows)
        public String join_country;             // 국가코드(형식없음)
        public int join_market_type;            // 가입시 마켓 코드(11:앱스토어(PUB),12:안드로이드마켓(PUB),13:티스토어(PUB),14:올레(PUB),15:오즈(PUB),16:삼성(PUB),17:앱프리,18:N스토어,19:타오바오,20:중국앱스토어,21:대만앱스토어,22:대만구글스토어,23:대만카이엔테크,24:카카오스토어(PUB),99:기타)... todo 왜?
        public String phone_number;             // 전화번호
        public int age;                         // 나이대(0:비밀, 10:10대, 20:20대 ...)
        public int status;                      // 0:비밀, 1:진보, 2:보수
        public int party;                       // 정당(0:비밀, 1:민주당, 2:새누리, 3.국민의당, 4:바른정당, 5:정의당', 6:기타)
        public int sex;                         // 남여구분(0:비밀, 1:남, 2:여)
    }

}
