package xyz.kjh.pp.popup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.juniverse.stuff.console;
import xyz.juniverse.stuff.social.LoginMethod;
import xyz.kjh.pp.activity.JoinActivity;
import xyz.kjh.pp.activity.MainActivity;
import xyz.kjh.pp.R;
import xyz.kjh.pp.service.Server;
import xyz.kjh.pp.service.model.req.LoginParams;
import xyz.kjh.pp.service.model.res.ResponseM;

/**
 * Created by juniverse on 21/03/2017.
 */

public class LoginPopup extends DialogFragment
{

    public static LoginPopup instance()
    {
        return new LoginPopup();
    }

    private LoginMethod method;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_login, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getView().findViewById(R.id.login_view).setVisibility(View.INVISIBLE);

        view.findViewById(R.id.facebook).setOnClickListener(loginButtonListener);
        view.findViewById(R.id.google).setOnClickListener(loginButtonListener);

        method = LoginMethod.Factory.getLastUsedMethod(getActivity());
        if (!method.isLoggedIn())
        {
            getView().findViewById(R.id.login_view).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
        else
            loginToPPServer();
    }

    private View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            console.i("onLoginButton", view.getId());
            if (view.getId() == R.id.facebook) {
                if (method.getPlatformId() != LoginMethod.Facebook)
                    method = LoginMethod.Factory.create(LoginMethod.Facebook, getActivity());
            } else if (view.getId() == R.id.google) {
                if (method.getPlatformId() != LoginMethod.Google)
                    method = LoginMethod.Factory.create(LoginMethod.Google, getActivity());
            }

            method.login(new LoginMethod.OnLoginResultListener() {
                @Override
                public void onResult(boolean success, String message) {
                    if (success)
                        loginToPPServer();
                    else
                        Snackbar.make(getActivity().findViewById(R.id.activity_constitution), message, Snackbar.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (method != null)
            method.onActivityResult(requestCode, resultCode, data);
    }

    private void loginToPPServer()
    {
        getView().findViewById(R.id.login_view).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);

        LoginParams model = new LoginParams();
        model.platform_type = method.getPlatformId();
        model.platform_id = method.getUserId();

        Server.getInstance().setLoginMethod(method);
        Server.getInstance().call("/user/login", model, new Server.ResponseListener<ResponseM>() {
            @Override
            public void onResult(ResponseM result) {
                if (!result.success)
                {
                    if ("E_10008".equals(result.ecode))          // 없는 유저.
                        goToSignIn();
                    else
                        Snackbar.make(getActivity().findViewById(R.id.activity_constitution), result.message, Snackbar.LENGTH_LONG).show();
                }
                else
                    goToMain();
            }
        });
    }

    private void goToSignIn()
    {
        dismiss();
        startActivity(new Intent(getContext(), JoinActivity.class));
        getActivity().finish();
    }

    private void goToMain()
    {
        dismiss();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}

