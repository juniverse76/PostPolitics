package xyz.kjh.pp.service;

import android.net.Uri;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import xyz.juniverse.stuff.comm.HttpRPC;
import xyz.juniverse.stuff.comm.RPC;
import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.crypt.Aes128Crypt;
import xyz.juniverse.stuff.crypt.Crypt;
import xyz.juniverse.stuff.social.LoginMethod;
import xyz.kjh.pp.service.model.res.CommonResponseM;
import xyz.kjh.pp.service.model.res.ResponseM;

/**
 * Created by juniverse on 24/03/2017.
 */

public class Server
{
    abstract public static class ReqParams
    {
        @Override
        public String toString() {
            return new Gson().toJson(this);
        }
    }

    public static final int OS_TYPE_IOS = 1;
    public static final int OS_TYPE_ANDRIOD = 2;
    public static final int OS_TYPE_TIZEN = 3;
    public static final int OS_TYPE_WINDOWS = 4;

    public static final int MARKET_TYPE_ANDROID_PLAY_STORE = 12;


    private static Server _instance;

    public static Server getInstance() {
        if (_instance == null)
            _instance = new Server();
        return _instance;
    }






    public static final String SERVER_URL = "http://121.166.76.188:18001/pp";
    public static final String CMD_KEY = "jhkim,./!@#$%^&";
    public static final String RES_KEY = "jhkim,./!@#$%^*";

    private final Crypt crypt;
//    private final RPC rpc;
    private String cheader;
    private LoginMethod method = null;
    private Server()
    {
        RPC.setServerCredential(SERVER_URL);
        crypt = new Aes128Crypt(CMD_KEY, RES_KEY);
//        rpc = new HttpRPC();
        cheader = "";
    }

    public void setLoginMethod(LoginMethod method)
    {
        this.method = method;
    }

    public LoginMethod getLoginMethod()
    {
        return method;
    }

    public void call(final String cmd, final ResponseListener listener)
    {
        call(cmd, null, listener);
    }

    public void call(final String cmd, final ReqParams params, final ResponseListener listener)
    {
        // todo 나중에 이 Server도 common library 로 뺄 수 있겠다. createParameter, handleResponse
        final Gson gson = new Gson();

        Uri.Builder builder = new Uri.Builder();
        if (params != null && crypt != null)
        {
            String json = gson.toJson(params);
            builder.appendQueryParameter("cmd", crypt.encrypt(json));
        }
        builder.appendQueryParameter("cheader", cheader);
        String param = builder.build().getEncodedQuery();

        console.i("param?", param);
        RPC rpc = new HttpRPC();
        rpc.call(cmd, param, new RPC.OnResultListener() {
            @Override
            public void onResult(String response) {
                try {
                    CommonResponseM top = gson.fromJson(response, CommonResponseM.class);
                    if (top != null)
                    {
                        cheader = top.header;

                        // todo 아래는 response가 암호화냐 아니냐에 따라 타입이 달라서 일단은 임시로 이렇게 처리. 나중에 통일화 되면 바꿔.
                        String result;
                        if (top.isResEncrypted)
                        {
                            CommonResponseM.Encrypted encrypted = gson.fromJson(response, CommonResponseM.Encrypted.class);
                            result = crypt.decrypt(encrypted.response);
                        }
                        else
                        {
                            CommonResponseM.Plain plain = gson.fromJson(response, CommonResponseM.Plain.class);
                            result = gson.toJson(plain.response);
                        }

                        console.i("result?", result);
                        if (listener != null)
                        {
                            ResponseM res = listener.createResultInstance(result);
                            listener.onResult(res);
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onResult(incorrectModelError());
                }
            }
        });
    }

    private ResponseM incorrectModelError()
    {
        ResponseM res = new ResponseM();
        res.success = false;
        res.ecode = "E_20001";
        return res;
    }

    public static abstract class ResponseListener<E extends ResponseM>
    {
        public E createResultInstance(String json)
        {
            Type sooper = getClass().getGenericSuperclass();
            Type t = ((ParameterizedType)sooper).getActualTypeArguments()[0];
            return new Gson().fromJson(json, t);
        }

        abstract public void onResult(E result);
    }


    WebSocketClient webSocketClient = null;
    public void connectWebSocket(String wsUrl)
    {
        if (webSocketClient != null)
            return;

        URI uri;
        try {
            uri = new URI(wsUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                console.i("WebSocket Opened");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                notifyObservers(s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                console.i("Websocket Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                console.i("Websocket Error " + e.getMessage());
            }
        };

        console.i("connecting to WebSocket!!!");
        webSocketClient.connect();
    }

    public void addWebSocketCallback(WebSocketObserver observer)
    {
        if (observers == null)
            observers = new ArrayList<>();

        // 이미 같은 애가 있어. 무시.
        for (WebSocketObserver ob : observers)
            if (ob == observer)
                return;
        observers.add(observer);
    }

    public void removeWebSocketCallback(WebSocketObserver observer)
    {
        if (observers == null || observer == null)
            return;
        observers.remove(observer);
    }

    private void notifyObservers(String message)
    {
        if (observers == null)
            return;
        for (WebSocketObserver observer : observers)
            observer.onMessage(message);
    }


    private ArrayList<WebSocketObserver> observers;
    public interface WebSocketObserver
    {
        void onMessage(String message);
    }
}
