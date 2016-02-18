package net.sytes.surfael.androidchat.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import net.sytes.surfael.androidchat.R;

import net.sytes.surfael.androidchat.adapters.MessagesRecycleAdapter;
import net.sytes.surfael.androidchat.classes.CircleTransform;
import net.sytes.surfael.androidchat.classes.H_ApiReceiver;
import net.sytes.surfael.androidchat.util.SimpleDividerItemDecoration;
import net.sytes.surfael.api.ApiReceiveInterface;
import net.sytes.surfael.api.ApiSendFacade;
import net.sytes.surfael.api.model.clients.Client;
import net.sytes.surfael.api.model.exceptions.LocalException;
import net.sytes.surfael.api.model.messages.Message;
import net.sytes.surfael.data.MessageProxy;
import net.sytes.surfael.data.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FloatingActionButton mSendButton;
    private EditText mEditText;
    public RecyclerView mRecyclerMessages;
    public List<MessageProxy> messageList;
    public MessagesRecycleAdapter adapterMessages;
    public boolean isPaused;
    private ApiReceiveInterface apiri;
    private boolean stored;
    protected MainActivity mContext;
    private int onPauseTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        Session.startHawk(this);

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

        messageList = Session.getHistory();
        adapterMessages = new MessagesRecycleAdapter(messageList, this, getSupportFragmentManager());

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

        mRecyclerMessages.setAdapter(adapterMessages);
        mRecyclerMessages.setHasFixedSize(true);
        mRecyclerMessages.setLayoutManager(layoutManager);
        SimpleDividerItemDecoration sd = new SimpleDividerItemDecoration(getResources());
        mRecyclerMessages.addItemDecoration(sd);

        Client client = Session.getCurrentUser();

        ImageView profilePicDrawer = (ImageView) headerView.findViewById(R.id.drawer_profile_pic);
        Picasso.with(this)
                .load(Session.getCurrentUser().getPhotoUrl())
                .resize(170, 170)
                .transform(new CircleTransform())
                .into(profilePicDrawer);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    mSendButton.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });
        mEditText.setTypeface(Typeface.DEFAULT);
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
//        getMenuInflater().inflate(R.menu.main, menu);
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

//        if (id == R.id.nav_camara) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void notificateUser(final Message m) {

        final Notification notification;


        if (mContext.isPaused) {
            android.support.v4.app.NotificationCompat.Builder mBuilder;
            mBuilder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.sym_action_chat)
                            .setContentTitle(m.getOwnerName())
                            .setContentText(m.getText())
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setStyle(new NotificationCompat.BigPictureStyle());

            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(mContext, MainActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            notification = mBuilder.build();
            mNotificationManager.notify(1, notification);

            int iconId = android.R.id.icon;
//            int bigIconId = getResources().getIdentifier("android:id/big_picture", null, null);

            // Get RemoteView and id's needed
            RemoteViews contentView = notification.contentView;
//            RemoteViews bigContentView = notification.bigContentView;

            Picasso.with(mContext).load(m.getSenderPhotoUrl()).transform(new CircleTransform()).resize(1024, 1024).into(contentView, iconId, 1, notification);
//            Picasso.with(mContext).load(m.getSenderPhotoUrl()).into(bigContentView, bigIconId, 1, notification);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        onPauseTimes++;

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

        ApiSendFacade.overwriteListener(H_ApiReceiver.buildApiCallbackForChatMessagesWithoutLogin(this));

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);

        messageList = Session.getHistory();
        adapterMessages = new MessagesRecycleAdapter(messageList, this, getSupportFragmentManager());
        mRecyclerMessages.setAdapter(adapterMessages);
        runOnUiThread(new Runnable() {
            public void run() {
                adapterMessages.notifyDataSetChanged();
                mRecyclerMessages.smoothScrollToPosition(adapterMessages.getItemCount());
            }
        });
        if (onPauseTimes == 1) {
            ApiSendFacade.requestHistory(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPaused = true;

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void buildApiListenerWithoutLogin() {
        apiri = H_ApiReceiver.buildApiCallbackForChatMessagesWithoutLogin(this);

        try {
            ApiSendFacade.overwriteListener(apiri);
            ApiSendFacade.aSyncConnect(Session.SERVER_IP, Session.SERVER_PORT, apiri,
                    Session.getCurrentUser().getEmail(), Session.getCurrentUser().getMD5Password(), false);
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
