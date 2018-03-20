package com.tr4android.appcompatextension.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tr4android.appcompatextension.FileAdapter;
import com.tr4android.appcompatextension.R;
import com.tr4android.support.extension.drawable.IndeterminateProgressDrawable;
import com.tr4android.support.extension.drawable.MediaControlDrawable;
import com.tr4android.support.extension.drawable.PlaceholderDrawable;
import com.tr4android.support.extension.internal.Account;
import com.tr4android.support.extension.internal.IAccount;
import com.tr4android.support.extension.widget.AccountHeaderView;
import com.tr4android.support.extension.widget.FlexibleToolbarLayout;
import com.tr4android.support.extension.widget.FloatingActionMenu;

public class SampleActivity extends BaseActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FlexibleToolbarLayout toolbarLayout = (FlexibleToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitleEnabled(true);

        // Setup DrawerLayout so we can close it later
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Setup NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return true;
            }
        });
        // Setup AccountHeaderView
        AccountHeaderView accountHeaderView = (AccountHeaderView) navigationView.inflateHeaderView(R.layout.account_header);
        accountHeaderView.addAccounts(new Account().setName("TR4Android").setEmail("tr4android@example.com").setIconResource(R.drawable.account_drawer_profile_image_tr4android),
                new Account().setName("Fountain Geyser").setEmail("fountaingeyser@example.com").setIconResource(R.drawable.account_drawer_profile_image_fountaingeyser),
                new Account().setName("Alpha Account").setEmail("alpha.account@example.de").setInfoIconResource(R.drawable.ic_mail_black_24dp).setInfoText("2"),
                new Account().setName("Beta Account").setEmail("beta.account@example.de").setPlaceholderIconEnabled(true).setPlaceholderCircleColor(Color.parseColor("#2196f3")),
                new Account().setName("SnailMail").setEmail(null).setPlaceholderIconEnabled(true).setPlaceholderCircleColor(Color.parseColor("#e51c23")))
        ;

        accountHeaderView.setAccountSelectedListener(new AccountHeaderView.OnAccountSelectedListenerAdapter() {
            @Override
            public boolean onAccountSelected(IAccount account) {
                Snackbar.make(findViewById(R.id.main_layout), account.getEmail(), Snackbar.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                return true;
            }

            @Override
            public void onAccountChecked(IAccount account, boolean isChecked) {
                Toast.makeText(SampleActivity.this, (isChecked ? "Selected: " : "Unselected: ") + account.getEmail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAccountAddSelected() {
                Snackbar.make(findViewById(R.id.main_layout), "Aha! So you want to add an account!", Snackbar.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
            }

            @Override
            public void onAccountManageSelected() {
                Snackbar.make(findViewById(R.id.main_layout), "Yes! Management is everything!", Snackbar.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
            }
        });

        // Setup the dimming of FloatingActionMenu
        FloatingActionMenu floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        floatingActionMenu.setupWithDimmingView(findViewById(R.id.dimming_view), Color.parseColor("#42000000"));
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton button = (FloatingActionButton) view;
                Toast.makeText(SampleActivity.this, "Creating " + button.getContentDescription(), Toast.LENGTH_LONG).show();
            }
        };
        findViewById(R.id.fab_document).setOnClickListener(listener);
        findViewById(R.id.fab_spreadsheet).setOnClickListener(listener);
        findViewById(R.id.fab_presentation).setOnClickListener(listener);

        // Setup the icon of the FlexibleToolbarLayout
        FlexibleToolbarLayout flexibleToolbarLayout = (FlexibleToolbarLayout) findViewById(R.id.toolbar_layout);
        PlaceholderDrawable placeholderDrawable =
                new PlaceholderDrawable.Builder(this).setPlaceholderText("M").build();
        flexibleToolbarLayout.setIcon(placeholderDrawable);

        // Setup the sample adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FileAdapter(this));

        float dp = getResources().getDisplayMetrics().density;

        // Setup delightful detail drawables
        ProgressBar artImageView = (ProgressBar) findViewById(R.id.art_imageview);
        IndeterminateProgressDrawable progressDrawable =
                new IndeterminateProgressDrawable.Builder(this)
                        .setColor(Color.WHITE)
                        .setPadding(16 * dp)
                        .setStrokeWidth(4 * dp)
                        .build();
        artImageView.setIndeterminateDrawable(progressDrawable);

        ImageView controlsImageView = (ImageView) findViewById(R.id.controls_imageview);
        final MediaControlDrawable controlDrawable =
                new MediaControlDrawable.Builder(this)
                        .setColor(Color.WHITE)
                        .setPadding(8 * dp)
                        .setInitialState(MediaControlDrawable.State.PLAY)
                        .build();
        controlsImageView.setImageDrawable(controlDrawable);
        controlsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // start animation on click
                controlDrawable.setMediaControlState(getNextState(controlDrawable.getMediaControlState()));
            }
        });

        // Setup DatePickerDialog and TimePickerDialog
        findViewById(R.id.pickers_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPickerDialogs();
            }
        });
    }

    private void launchPickerDialogs() {
        Intent intent = new Intent(this, PickerDialogActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean mReverse = true;

    /**
     * Helper for cycling through the {@link MediaControlDrawable} states
     */
    private MediaControlDrawable.State getNextState(MediaControlDrawable.State current) {
        switch (current) {
            case PLAY:
                mReverse = !mReverse;
                return mReverse
                        ? MediaControlDrawable.State.PAUSE
                        : MediaControlDrawable.State.STOP;
            case STOP:
                return mReverse
                        ? MediaControlDrawable.State.PLAY
                        : MediaControlDrawable.State.PAUSE;
            case PAUSE:
                return mReverse
                        ? MediaControlDrawable.State.STOP
                        : MediaControlDrawable.State.PLAY;
        }
        return null;
    }
}
