package xyz.kjh.pp.service.model.res;

import com.google.gson.Gson;

/**
 * Created by juniverse on 21/03/2017.
 */

public class ResponseM
{
    public boolean success;
    public String moduleName;
    public long time;
    public String message;
    public String ecode;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
