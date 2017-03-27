package xyz.kjh.pp.service.model.req;

import java.util.Locale;

import xyz.kjh.pp.BuildConfig;
import xyz.kjh.pp.service.Server;

/**
 * Created by juniverse on 24/03/2017.
 */

public class JoinM extends Server.ReqParams
{
    public String clientVersionCode = BuildConfig.VERSION_NAME;       // 클라이언트 버전(형식없음)
    public int platform_type;               // 플랫폼 타입(1:페이스북,2:GUEST,3:GameCenter(IOS),4:GooglePlus(AOS),99:구분 불가능)
    public String platform_id;                 // 아이디
    public String push_token;               // 푸시 토큰
    public int push_type;                   // push 수신 여부 (0:전체거부(항상), 1:야간만 허용, 2:주간만 허용, 3:수신허용(항상))
    public int sms_type;                    // SMS 수신 여부 (0:전체거부(항상), 1:야간만 허용, 2:주간만 허용, 3:수신허용(항상))
    public int join_os_type = Server.OS_TYPE_ANDRIOD;
    public String join_country;             // 국가코드(형식없음)
    public int join_market_type = Server.MARKET_TYPE_ANDROID_PLAY_STORE;
    public String phone_number;             // 전화번호
    public int age;                         // 나이대(0:비밀, 10:10대, 20:20대 ...)
    public int status;                      // 0:비밀, 1:진보, 2:보수
    public int party;                       // 정당(0:비밀, 1:민주당, 2:새누리, 3.국민의당, 4:바른정당, 5:정의당', 6:기타)
    public int sex;                         // 남여구분(0:비밀, 1:남, 2:여)

    public JoinM()
    {
        join_country = Locale.getDefault().getCountry();
    }
}
