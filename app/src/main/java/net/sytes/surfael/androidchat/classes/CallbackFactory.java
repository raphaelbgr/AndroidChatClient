package net.sytes.surfael.androidchat.classes;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import net.sytes.surfael.androidchat.login.LoginActivity;
import net.sytes.surfael.androidchat.mainscreen.MainActivity;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.control.classes.MD5;
import net.sytes.surfael.api.control.sync.Status;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.clients.FacebookClient;
import net.sytes.surfael.api.model.exceptions.ServerException;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;
import net.sytes.surfael.api.model.messages.History;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
import net.sytes.surfael.data.MessageProxy;
import net.sytes.surfael.data.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by raphaelb.rocha on 03/02/2016.
 */
public class CallbackFactory {

    public static ApiReceiveInterface build(final Activity context, final Client client) {
        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                if (context instanceof LoginActivity) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(context, normalMessage.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (context instanceof MainActivity) {
                    final MainActivity ma = (MainActivity) context;
                    Client currentUser = Session.getCurrentUser();

                    if (normalMessage.getSenderId() != currentUser.getId()) {
                        ma.runOnUiThread(new Runnable() {
                            public void run() {
                                ma.notificateUser(normalMessage);
                            }
                        });
                    }
                    Session.messageList.add(new MessageProxy(normalMessage));
                    Session.storeHistory(new ArrayList<MessageProxy>(Session.messageList));

                    ma.runOnUiThread(new Runnable() {
                        public void run() {
                            ma.adapterMessages.notifyDataSetChanged();
                            ma.mRecyclerMessages.smoothScrollToPosition(ma.adapterMessages.getItemCount());
                        }
                    });
                }
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
                Log.d("server_callback", disconnectionMessage.toString());
            }

            @Override
            public void onReceiveServerMessage(final ServerMessage serverMessage) {
                if (context instanceof LoginActivity) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Message m = (Message) serverMessage;
                            if (m.getServresponse() != null) {
                                Toast.makeText(context, serverMessage.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else if (context instanceof MainActivity) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Message m = (Message) serverMessage;
                            if (m.getServresponse() != null) {
                                Toast.makeText(context, serverMessage.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onReceiveClient(Client client) {
                Log.d("server_callback", client.toString());

                if (context instanceof LoginActivity) {
                    Intent intent = new Intent(context, MainActivity.class);

                    if (client instanceof FacebookClient) {
                        client.setMD5Password(MD5.getMD5(client.getPassword()));

                        Session.setCurrentUser(client);

                        context.startActivity(intent);
                        context.finish();
                    } else {
                        Client mergedClient = Session.mergeFacebookClient(client, client);
                        Session.setCurrentUser(mergedClient);

                        context.startActivity(intent);
                        context.finish();
                    }
                } else if (context instanceof MainActivity) {
                    final MainActivity ma = (MainActivity) context;
                    Session.setCurrentUser(client);

                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            Snackbar snackbar = Snackbar.make(ma.mSendButton, "Connected.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null);
                            snackbar.show();
                        }
                    });
                }
            }

            @Override
            public void onReceiveServerException(final ServerException e) {
                Log.d("server_callback", e.toString());

                if (context instanceof MainActivity) {
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            String disconnected = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Disconnected";
                            Snackbar.make(((MainActivity) context).mSendButton, disconnected, Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onConnectionError(final Exception e) {
                context.runOnUiThread(new Runnable() {
                    public void run() {
                        String disconnected = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Error";
                        if (context instanceof MainActivity) {
                            Snackbar.make(((MainActivity) context).mSendButton, disconnected, Snackbar.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, disconnected, Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onUserMessageReceived(Message m) {
                Log.d("server_callback", "User Message Received.");
            }

            @Override
            public void onReceiveServerHistory(History h) {
                Log.d("server_callback", "Server History Received.");

                if (h.getMessagelogRows()!= null && h.getMessagelogRows().size() > 0) {
                    for (HashMap<String, String> row : h.getMessagelogRows()) {
                        int id = Integer.valueOf(row.get("ID"));
                        boolean contains = false;

                        for (MessageProxy mp : Session.messageList) {
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

                            Session.messageList.add(newMp);
                        }
                    }
                    Collections.sort(Session.messageList, new Comparator<MessageProxy>() {
                        @Override
                        public int compare(MessageProxy lhs, MessageProxy rhs) {
                            if (lhs.getMessageProxyServerCount() == rhs.getMessageProxyServerCount()) {
                                return 0;
                            } else if (lhs.getMessageProxyServerCount() > rhs.getMessageProxyServerCount()) {
                                return 1;
                            } else return -1;

                        }
                    });
                    Session.storeHistory(Session.messageList);
                    context.runOnUiThread(new Runnable() {
                        public void run() {
                            if (context instanceof MainActivity) {
                                ((MainActivity)context).adapterMessages.notifyDataSetChanged();
                                ((MainActivity)context).mRecyclerMessages.smoothScrollToPosition(((MainActivity)context).adapterMessages.getItemCount());
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnected() {
                Log.d("server_callback", "Connected.");

                writeOnSnack(context, "Conencted");
            }

        };

        return apiri;
    }

    public static void writeOnSnack(final Activity a, final String s) {
        if (a instanceof MainActivity) {
            a.runOnUiThread(new Runnable() {
                public void run() {
                    Snackbar.make(((MainActivity)a).mSendButton, s, Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }

}
