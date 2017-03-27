package xyz.kjh.pp.service.model.res;

/**
 * Created by juniverse on 24/03/2017.
 */

public class CommonResponseM
{
    public static class Encrypted extends CommonResponseM
    {
        public String response;
    }

    public static class Plain extends CommonResponseM
    {
        public ResponseM response;
    }

    public String header;
    public boolean isResEncrypted;
}
