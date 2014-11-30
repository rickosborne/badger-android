package org.rickosborne.badger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVGImageView;

import org.rickosborne.badger.data.User;


public class PatientHomeActivity extends ActivityWithSVG {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        loadSvgResource(R.drawable.settings, R.id.cogContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.switchit, R.id.switchContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.pulse, R.id.pulseContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.person, R.id.personContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.pie, R.id.historyContainer, R.dimen.icon_120);
        loadSvgResource(R.drawable.pencil, R.id.checkInContainer, R.dimen.icon_120);
        BadgerApp app = (BadgerApp) getApplication();
        User user = app.getUser();
        if (user == null) { signOut(null); return; }
        ((TextView) findViewById(R.id.activeUserName)).setText(user.getNameFirst() + " " + user.getNameLast());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startCheckIn(View v) {
        startActivity(new Intent(this, CheckInPainActivity.class));
    }

    public void startHistory(View v) {
//        startActivity(new Intent(this, ));
    }

    public void startSettings(View v) {
        startActivity(new Intent(this, AccountSettingsActivity.class));
    }

    public void signOut(View v) {
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }
}
