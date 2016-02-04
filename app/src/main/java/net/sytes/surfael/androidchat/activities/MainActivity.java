package net.sytes.surfael.androidchat.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import net.sytes.surfael.androidchat.classes.H_ApiReceiver;
import net.sytes.surfael.androidchat.util.SimpleDividerItemDecoration;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.ApiSendFacade;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.data.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FloatingActionButton mSendButton;
    private EditText mEditText;
    public RecyclerView mRecyclerMessages;
    public List<Message> messageList;
    public MessagesRecycleAdapter adapterMessages;
    private boolean isPaused;
    private ApiReceiveInterface apiri;
    private boolean stored;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView mHeader = (TextView)headerView.findViewById(R.id.email_drawer);
        mHeader.setText(Session.getCurrentUser().getEmail());

        TextView mSubtitle = (TextView) headerView.findViewById(R.id.name_drawer);
        mSubtitle.setText(Session.getCurrentUser().getName());

        mSendButton = (FloatingActionButton) findViewById(R.id.fab);
        mEditText = (EditText) findViewById(R.id.editText);

        setSendAction();

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
                String text = mEditText.getText().toString();
                if (text.trim().equals("")) {
                    Snackbar snackbar = Snackbar.make(mEditText, "Please, type in a message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null);
                    snackbar.setActionTextColor(Color.MAGENTA);
                    snackbar.show();
                } else {
                    ApiSendFacade.sendNormalMessage(text);
                    mEditText.setText("");
                }
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

    public void notificateUser(String title, String message) {
        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.sym_action_chat)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL);

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

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            stored = bundle.getBoolean("storedUser");
            if (stored || Session.getCurrentUser() != null) {
                buildApiListenerWithoutLogin();
            } else {
                if (Session.getCurrentUser() == null) {
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;
    }

    private void buildApiListenerWithoutLogin() {
        apiri = H_ApiReceiver.buildApiCallbackForChatMessagesWithoutLogin(this);

        try {
            ApiSendFacade.overwriteListener(apiri);
            ApiSendFacade.aSyncConnect(Session.SERVER_IP, Session.SERVER_PORT, apiri,
                    Session.getCurrentUser().getEmail(), Session.getCurrentUser().getMD5Password(), false);
//            ApiSendFacade.connect("192.168.2.11", 2001, apiri, mEmail, mPassword);
        } catch (LocalException e) {
            e.printStackTrace();
            if (!isPaused) {
                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (!isPaused) {
                Toast.makeText(getBaseContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
