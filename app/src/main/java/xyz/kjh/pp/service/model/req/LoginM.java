package xyz.kjh.pp.service.model.req;

import xyz.kjh.pp.BuildConfig;
import xyz.kjh.pp.service.Server;


/**
 * Created by juniverse on 24/03/2017.
 */

public class LoginM extends Server.ReqParams
{
    public String clientVersionCode = BuildConfig.VERSION_NAME;
    public int platform_type;
    public String platform_id;
    public int login_os_type = Server.OS_TYPE_ANDRIOD;
    public String push_token;
    public int login_market_type = Server.MARKET_TYPE_ANDROID_PLAY_STORE;
    public String phone_number;
}
