package net.sytes.surfael.androidchat.classes;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import net.sytes.surfael.androidchat.activities.MainActivity;
import net.sytes.surfael.api.ApiReceiveInterface;
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

    public static ApiReceiveInterface buildApiCallbackForChatMessagesWithLogin(final MainActivity context) {

        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                if (!normalMessage.getOwnerLogin().equalsIgnoreCase(Session.getCurrentUser().getLogin())) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
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
                Snackbar snackbar = Snackbar.make(context.mSendButton, "Connected.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                snackbar.setActionTextColor(Color.MAGENTA);
                snackbar.show();

                Session.setCurrentUser(client);
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

    public static ApiReceiveInterface buildApiCallbackForChatMessagesWithoutLogin(final MainActivity context) {

        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                if (!normalMessage.getOwnerLogin().equalsIgnoreCase(Session.getCurrentUser().getLogin())) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
//                            Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
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

}
