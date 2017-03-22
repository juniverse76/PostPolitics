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
import xyz.kjh.pp.R;

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

        view.findViewById(R.id.facebook).setOnClickListener(loginButtonListener);
        view.findViewById(R.id.google).setOnClickListener(loginButtonListener);

        method = LoginMethod.Factory.getLastUsedMethod(getActivity());
        if (!method.isLoggedIn())
        {
            getView().findViewById(R.id.login_view).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
        }
        else
            goToMain();
    }

    private View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            console.i("onLoginButton", view.getId());
            if (view.getId() == R.id.facebook) {
                if (method.getId() != LoginMethod.Facebook)
                    method = LoginMethod.Factory.create(LoginMethod.Facebook, getActivity());
            } else if (view.getId() == R.id.google) {
                if (method.getId() != LoginMethod.Google)
                    method = LoginMethod.Factory.create(LoginMethod.Google, getActivity());
            }

            method.login(new LoginMethod.OnLoginResultListener() {
                @Override
                public void onResult(boolean success, String message) {
                    if (success)
                        goToMain();
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

    private void goToMain()
    {
        dismiss();
        getActivity().finish();
        // todo service에다 로그인을 시도하고, 사용자 정보가 있으면 Main으로, 아니면 가입 창으로.
//        startActivity(new Intent(getContext(), MainActivity.class));
//        startActivity(new Intent(getContext(), LoginActivity.class));
    }
}

