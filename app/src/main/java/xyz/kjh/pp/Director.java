package xyz.kjh.pp;

import xyz.kjh.pp.service.model.res.MainM;

/**
 * Created by juniverse on 14/04/2017.
 */

public class Director
{
    private static Director _instance;
    public static Director getInstance()
    {
        if (_instance == null)
            _instance = new Director();
        return _instance;
    }

    private Director() {}


    private MainM.User user;
    public void setUser(MainM.User user)
    {
        this.user = user;
    }

    public MainM.User getUser()
    {
        return this.user;
    }
}
