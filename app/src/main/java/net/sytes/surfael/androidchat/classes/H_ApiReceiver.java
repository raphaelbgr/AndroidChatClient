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
import net.sytes.surfael.api.model.messages.History;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
import net.sytes.surfael.data.MessageProxy;
import net.sytes.surfael.data.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
                Client currentUser = Session.getCurrentUser();
                if (normalMessage.getSenderId() != currentUser.getId()) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            context.notificateUser(normalMessage);
                        }
                    });
                }
                context.messageList.add(new MessageProxy(normalMessage));
                Session.storeHistory(new ArrayList<MessageProxy>(context.messageList));

                context.runOnUiThread(new Runnable() {
                    public void run() {
                        context.adapterMessages.notifyDataSetChanged();
                        context.mRecyclerMessages.smoothScrollToPosition(context.adapterMessages.getItemCount());
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
//                Log.d("server_callback", disconnectionMessage.toString());
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
//                Log.d("server_callback", client.toString());

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
//                    Log.d("server_callback", e.getLocalizedMessage());
                }
            }

            @Override
            public void onUserMessageReceived(Message m) {

            }

            @Override
            public void onReceiveServerHistory(History h) {
                if (h.getMessagelogRows()!= null && h.getMessagelogRows().size() > 0) {
                    for (HashMap<String, String> row : h.getMessagelogRows()) {
                        int id = Integer.valueOf(row.get("ID"));
                        boolean contains = false;

                        for (MessageProxy mp : context.messageList) {
                            if (mp.getMessageProxyServerCount() == id) {
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            String timestamp = row.get("Timestamp");
                            String owner = row.get("Owner");
                            String message = row.get("Message");
                            String photo_url = row.get("Photo_URL");
                            String ownerid = row.get("OwnerID");
                            String email = row.get("Email");

                            MessageProxy newMp = new MessageProxy();
                            newMp.setMessageProxyServerCount(id);
                            newMp.setTimestamp(timestamp);
                            newMp.setOwnerName(owner);
                            newMp.setText(message);
                            newMp.setSenderPhotoUrl(photo_url);
                            newMp.setOwnerID(Integer.valueOf(ownerid));
                            newMp.setOwnerEmail(email);

                            context.messageList.add(newMp);
                        }
                    }
                    Collections.sort(context.messageList, new Comparator<MessageProxy>() {
                        @Override
                        public int compare(MessageProxy lhs, MessageProxy rhs) {
                            if (lhs.getMessageProxyServerCount() == rhs.getMessageProxyServerCount()) {
                                return 0;
                            } else if (lhs.getMessageProxyServerCount() > rhs.getMessageProxyServerCount()) {
                                return 1;
                            } else return -1;

                        }
                    });
                    Session.storeHistory(context.messageList);
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            context.adapterMessages.notifyDataSetChanged();
                            context.mRecyclerMessages.smoothScrollToPosition(context.adapterMessages.getItemCount());
                        }
                    });
                }
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
//                Log.d("server_callback", normalMessage.toString());
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
//                Log.d("server_callback", disconnectionMessage.toString());
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

//                Log.d("server_callback", client.toString());
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

            @Override
            public void onReceiveServerHistory(History h) {

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
//                Log.d("server_callback", normalMessage.toString());
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
//                Log.d("server_callback", disconnectionMessage.toString());
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

//                Log.d("server_callback", client.toString());

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

            @Override
            public void onReceiveServerHistory(History h) {

            }

        };

        return apiri;
    }

}
