package net.sytes.surfael.androidchat.classes;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import net.sytes.surfael.androidchat.activities.LoginActivity;
import net.sytes.surfael.androidchat.activities.MainActivity;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.control.classes.MD5;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.ServerException;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
import net.sytes.surfael.data.Session;

/**
 * Created by raphaelb.rocha on 03/02/2016.
 */
public class H_ApiReceiver {

    public static ApiReceiveInterface buildApiCallbackForChatMessagesWithoutLogin(final MainActivity context) {

        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                Client currentUser = Session.getCurrentUser();
                if (normalMessage.getSenderId() != currentUser.getId()) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            context.notificateUser(normalMessage.getOwnerName(), normalMessage.getText());
                        }
                    });
                }
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        context.messageList.add(normalMessage);
                        context.adapterMessages.notifyDataSetChanged();
                        context.mRecyclerMessages.smoothScrollToPosition(context.adapterMessages.getItemCount());
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
                Log.d("server_callback", disconnectionMessage.toString());
            }

            @Override
            public void onReceiveServerMessage(final ServerMessage serverMessage) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Message m = (Message) serverMessage;
                        if (m.getServresponse() != null) {
                            Toast.makeText(context, serverMessage.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onReceiveClient(Client client) {
                Log.d("server_callback", client.toString());

                Session.setCurrentUser(client);

                Snackbar snackbar = Snackbar.make(context.mSendButton, "Connected.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                snackbar.setActionTextColor(Color.MAGENTA);
                snackbar.show();
            }
            @Override
            public void onReceiveServerException(ServerException e) {
                Log.d("server_callback", e.toString());
            }

            @Override
            public void onConnectionError(Exception e) {
                if (e.getLocalizedMessage() != null) {
                    Log.d("server_callback", e.getLocalizedMessage());
                }
            }

            @Override
            public void onUserMessageReceived(Message m) {
                Log.d("server_callback", m.toString());
            }

        };

        return apiri;
    }

    public static ApiReceiveInterface buildApiCallbackForChatMessagesOnLoginScreen(final LoginActivity context, final String mPassword) {
        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
                Log.d("server_callback", disconnectionMessage.toString());
            }

            @Override
            public void onReceiveServerMessage(final ServerMessage serverMessage) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Message m = (Message) serverMessage;
                        if (m.getServresponse() != null) {
                            Toast.makeText(context, serverMessage.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onReceiveClient(Client client) {
                Intent intent = new Intent(context, MainActivity.class);

                Log.d("server_callback", client.toString());
                client.setMD5Password(MD5.getMD5(mPassword));

                Session.setCurrentUser(client);

                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onReceiveServerException(ServerException e) {
                Log.d("server_callback", e.toString());
            }

            @Override
            public void onConnectionError(Exception e) {
                if (e.getLocalizedMessage() != null) {
                    Log.d("server_callback", e.getLocalizedMessage());
                }
            }

            @Override
            public void onUserMessageReceived(Message m) {

            }

        };

        return apiri;
    }

    public static ApiReceiveInterface buildApiCallbackForChatMessagesOnLoginScreenForFacebook(final LoginActivity context, final Client fbClient) {
        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
                Log.d("server_callback", disconnectionMessage.toString());
            }

            @Override
            public void onReceiveServerMessage(final ServerMessage serverMessage) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Message m = (Message) serverMessage;
                        if (m.getServresponse() != null) {
                            Toast.makeText(context, serverMessage.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onReceiveClient(Client client) {
                Intent intent = new Intent(context, MainActivity.class);

                Log.d("server_callback", client.toString());

                Client mergedClient = Session.mergeFacebookClient(client, fbClient);
                Session.setCurrentUser(mergedClient);

                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onReceiveServerException(ServerException e) {
                Log.d("server_callback", e.toString());
            }

            @Override
            public void onConnectionError(Exception e) {
                if (e.getLocalizedMessage() != null) {
                    Log.d("server_callback", e.getLocalizedMessage());
                }
            }

            @Override
            public void onUserMessageReceived(Message m) {

            }

        };

        return apiri;
    }

}
