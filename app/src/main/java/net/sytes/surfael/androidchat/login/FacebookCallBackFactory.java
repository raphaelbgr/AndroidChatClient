package net.sytes.surfael.androidchat.login;

import android.os.Bundle;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import net.sytes.surfael.androidchat.classes.CallbackFactory;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.ApiSendFacade;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.data.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;


/**
 * Created by Raphael on 13/02/2016.
 */
public class FacebookCallBackFactory {

    public static FacebookCallback createCallBackForLoginScreen(final LoginActivity context) {

        FacebookCallback fbCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String accessToken = loginResult.getAccessToken().getToken().toString();
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                LoginManager.getInstance().logOut();
                                try {
                                    String email;
                                    String picUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
//                                    String birthday = object.getString("birthday");
                                    String gender = object.getString("gender");
                                    String name = object.getString("name");

                                    Client client = new Client();
                                    client.setPhotoUrl(picUrl);
                                    if (object.has("email")) {
                                        email = object.getString("email");
                                    } else {
                                        email = "private_email@" + UUID.randomUUID().toString() + ".com";
                                    }
                                    client.setEmail(email);
                                    client.setSex(gender);
                                    client.setFbToken(accessToken);
                                    client.setName(name);
                                    try {
//                                        client.setBirthDate(DateFormat.getDateInstance().parse(birthday));
                                        ApiReceiveInterface apiri = CallbackFactory.build(context, client);
                                        ApiSendFacade.connectFacebookAsync(Session.SERVER_IP,
                                                Session.SERVER_PORT, apiri, client, context);
                                    } catch (LocalException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email,gender,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
            }

        };

        return fbCallback;
    }
}
