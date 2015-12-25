package net.sytes.surfael.androidchat.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import net.sytes.surfael.androidchat.R;

import net.sytes.surfael.androidchat.adapters.MessagesRecycleAdapter;
import net.sytes.surfael.androidchat.util.SimpleDividerItemDecoration;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.ApiSendFacade;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.ServerException;
import net.sytes.surfael.api.model.messages.DisconnectionMessage;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.api.model.messages.NormalMessage;
import net.sytes.surfael.api.model.messages.ServerMessage;
import net.sytes.surfael.data.Session;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Client client = Session.currentUser;
    private FloatingActionButton mSendButton;
    private EditText mEditText;
    private RecyclerView mRecyclerMessages;
    private List<Message> messageList;
    private MessagesRecycleAdapter adapterMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        String clientGson = getIntent().getStringExtra("client");
//        Client client = new Gson().fromJson(clientGson, Client.class);

        TextView mHeader = (TextView)findViewById(R.id.email_drawer);
        mHeader.setText(client.getEmail());

        TextView mSubtitle = (TextView)findViewById(R.id.name_drawer);
        mSubtitle.setText(client.getName());

        mSendButton = (FloatingActionButton) findViewById(R.id.fab);
        mEditText = (EditText) findViewById(R.id.editText);

        setSendAction();

        ApiReceiveInterface apiri = new ApiReceiveInterface() {
            @Override
            public void onReceive(Object o) {
                Log.d("server_callback", o.toString());
            }

            @Override
            public void onReceiveNormalMessage(final NormalMessage normalMessage) {
                Log.d("server_callback", normalMessage.toString());
                if (!normalMessage.getOwnerLogin().equalsIgnoreCase(Session.currentUser.getLogin())) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), normalMessage.toString(), Toast.LENGTH_LONG).show();
                            notificateUser(normalMessage.getOwnerName(), normalMessage.getText());
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageList.add(normalMessage);
                        adapterMessages.notifyDataSetChanged();
                        mRecyclerMessages.smoothScrollToPosition(adapterMessages.getItemCount());
                    }
                });
            }

            @Override
            public void onReceiveDisconnectionMessage(DisconnectionMessage disconnectionMessage) {
                Log.d("server_callback", disconnectionMessage.toString());
            }

            @Override
            public void onReceiveServerMessage(final ServerMessage serverMessage) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Message m = (Message) serverMessage;
                        if (m.getServresponse() != null) {
                            Toast.makeText(getApplicationContext(), serverMessage.toString(), Toast.LENGTH_LONG).show();
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
                Log.d("server_callback", e.getLocalizedMessage());
            }

            @Override
            public void onUserMessageReceived(Message m) {
                Log.d("server_callback", m.toString());
            }

        };
        ApiSendFacade.overwriteListener(apiri);

        mRecyclerMessages = (RecyclerView) findViewById(R.id.recycler_transactions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        if (messageList == null) {
            messageList = new ArrayList<Message>();
        }
        adapterMessages = new MessagesRecycleAdapter(messageList, this, getSupportFragmentManager());

        mRecyclerMessages.setAdapter(adapterMessages);
        mRecyclerMessages.setHasFixedSize(true);
        mRecyclerMessages.setLayoutManager(layoutManager);
        mRecyclerMessages.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
    }

    private void setSendAction() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiSendFacade.sendNormalMessage(mEditText.getText().toString());
                mEditText.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void notificateUser(String title, String message) {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.side_nav_bar)
                        .setContentTitle(title)
                        .setContentText(message);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }
}
