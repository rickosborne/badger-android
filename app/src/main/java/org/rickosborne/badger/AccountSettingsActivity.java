package org.rickosborne.badger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.rickosborne.badger.data.User;

import java.io.File;
import java.io.IOException;


public class AccountSettingsActivity extends ActivityWithSVG {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        loadSvgResource(R.drawable.camera, R.id.photoChange, R.dimen.icon_120);
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

    public void takePhoto(View v) {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        BadgerApp app = (BadgerApp) getApplication();
        User currentUser = app.getUser();
        if (currentUser == null) return;
        if (photoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = File.createTempFile(String.valueOf(Math.random()), ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
//                photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                photoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == RESULT_OK) && (data != null)) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = extras.getParcelable("data");
                ((BadgerApp) getApplication()).savePhoto(imageBitmap);
            }
        }
    }
}
