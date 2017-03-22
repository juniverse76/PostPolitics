package xyz.kjh.pp.service;

import com.google.gson.Gson;

import org.json.JSONObject;

import xyz.juniverse.stuff.crypt.Crypt;

/**
 * Created by juniverse on 21/03/2017.
 */

public class Response
{
    public static Response create(String jsonString, Crypt crypt, Class<? extends Response> tClass)
    {
        try {
            JSONObject json = new JSONObject(jsonString);
            String result = json.getString("response");
            boolean isEncrypted = json.getBoolean("isResEncrypted");
            if (isEncrypted && crypt != null && result != null)
                result = crypt.decrypt(result);

            Gson gson = new Gson();
            return gson.fromJson(result, tClass);
        } catch (Exception e) { }
        return null;
    }

    public boolean success;
    public String moduleName;
    public long time;
    public String message;
    public int ecode;
}
