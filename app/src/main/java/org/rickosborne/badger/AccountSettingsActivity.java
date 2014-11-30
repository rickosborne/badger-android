package org.rickosborne.badger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.rickosborne.badger.data.User;


public class AccountSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BadgerApp app = (BadgerApp) getApplication();
        User user = app.getUser();
        if (user == null) { finish(); return; }
        ((EditText) findViewById(R.id.firstName)).setText(user.getNameFirst());
        ((EditText) findViewById(R.id.lastName)).setText(user.getNameLast());
    }

    @Override
    protected void onPause() {
        super.onPause();
        BadgerApp app = (BadgerApp) getApplication();
        User user = app.getUser();
        if (user != null) {
            String firstName = ((EditText) findViewById(R.id.firstName)).getText().toString();
            String lastName = ((EditText) findViewById(R.id.lastName)).getText().toString();
            boolean changed = false;
            if (!lastName.equals(user.getNameLast())) { changed = true; user.setNameLast(lastName); }
            if (!firstName.equals(user.getNameFirst())) { changed = true; user.setNameFirst(firstName); }
            if (changed) app.saveUser();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_settings, menu);
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
}
