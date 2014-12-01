package org.rickosborne.badger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import org.rickosborne.badger.data.CheckIn;
import org.rickosborne.badger.data.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;

public class BadgerApp extends Application {

    private Api badgerApi = null;
    private User currentUser = null;
    private Collection<User> patients = null;
    private Bitmap photo = null;

    private static final String PREFS_FILE = "badger";
    private static final String PREF_USER = "user";

    public boolean isSignedIn () {
        return (getUser() != null);
    }

    public User getUser() {
        synchronized (Api.class) {
            if (currentUser == null) {
                SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
                String json = settings.getString(PREF_USER, null);
                if (json != null) currentUser = User.fromJSON(json);
            }
            return currentUser;
        }
    }

    public User getUser(String username, String password) {
        synchronized (Api.class) {
            Api api = openApi(username, password);
            if (api == null) return null;
            currentUser = api.getUserById((Math.round(Math.random() * 3) % 2) + 1);
            if (currentUser != null) {
                currentUser.setEmail(username);
                currentUser.setPassword(password); // hack
                cacheUser();
            }
            return currentUser;
        }
    }

    public static interface AfterGetPatients {
        public void complete(Collection<User> patients);
    }

    private class PatientsTask extends AsyncTask<AfterGetPatients, Void, Void> {
        private AfterGetPatients after;
        private WeakReference<Activity> activity;
        public PatientsTask(Activity activity) {
            this.activity = new WeakReference<Activity>(activity);
        }
        @Override
        protected Void doInBackground(AfterGetPatients... params) {
            after = params.length > 0 ? params[0] : null;
            if (badgerApi != null) patients = badgerApi.getPatientsForUser(currentUser.getId());
            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            Activity context = activity.get();
            if ((after != null) && (context != null)) context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    after.complete(patients);
                }
            });
        }
    }

    public void getPatients(final AfterGetPatients after, Activity context) {
        synchronized (Api.class) {
            if (patients != null) {
                if (after != null) context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        after.complete(patients);
                    }
                });
                return;
            }
            if (currentUser == null) return;
            if (badgerApi == null) openApi(currentUser.getUsername(), currentUser.getPassword());

            (new PatientsTask(context)).execute(after);
        }
    }

    public User getPatientById(long patientId) {
        if (patients != null) for (User patient : patients) {
            if (patient.getId() == patientId) return patient;
        }
        return null;
    }

    public Api openApi(String username, String password) {
        synchronized (Api.class) {
            if (badgerApi == null) badgerApi = BadgerApi.build(username, password);
            return badgerApi;
        }
    }

    public void saveUser() {
        synchronized (Api.class) {
            if (currentUser == null) return;
            cacheUser();
            if (badgerApi == null) return;
            badgerApi.updateUser(currentUser.getId(), currentUser);
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void cacheUser() {
        synchronized (Api.class) {
            SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_USER, currentUser.toJSON());
            editor.commit();
        }
    }

    public void logout() {
        synchronized (Api.class) {
            currentUser = null;
            badgerApi = null;
            patients = null;
            photo = null;
            SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(PREF_USER);
            editor.commit();
        }
    }

    public void checkInsForUser(final AfterGetCheckIns after, User user, Activity context) {
        if ((user == null) || (currentUser == null)) return;
        synchronized (Api.class) {
            if (badgerApi == null) openApi(currentUser.getUsername(), currentUser.getPassword());
            (new CheckInsTask(context, user)).execute(after);
        }
    }

    public static interface AfterGetCheckIns {
        public void complete(Collection<CheckIn> checkIns);
    }

    private class CheckInsTask extends AsyncTask<AfterGetCheckIns, Void, Collection<CheckIn>> {
        private AfterGetCheckIns after;
        private WeakReference<Activity> activity;
        private User user;
        public CheckInsTask(Activity activity, User user) {
            this.activity = new WeakReference<Activity>(activity);
            this.user = user;
        }
        @Override
        protected Collection<CheckIn> doInBackground(AfterGetCheckIns... params) {
            after = params.length > 0 ? params[0] : null;
            return badgerApi.getCheckInsForUser(user.getId());
        }
        @Override
        protected void onPostExecute(final Collection<CheckIn> checkIns) {
            Activity context = activity.get();
            if ((after != null) && (context != null)) context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    after.complete(checkIns);
                }
            });
        }
    }

    public void savePhoto(Bitmap photo) {
        if ((photo == null) || (currentUser == null)) return;
        File photoFile = new File(getFilesDir(), currentUser.getEmail() + ".jpg");
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(photoFile);
            photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhoto(int width, int height) {
        if (photo != null) return photo;
        if (currentUser == null) return null;
        File photoFile = new File(getFilesDir(), currentUser.getEmail() + ".jpg");
        if (!photoFile.exists() && !photoFile.canRead()) return null;
        BitmapFactory.Options photoOptions = new BitmapFactory.Options();
        photoOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFile.toString(), photoOptions);
        int scale = Math.min(photoOptions.outWidth / Math.max(100, width), photoOptions.outHeight / Math.max(100, height));
        photoOptions.inJustDecodeBounds = false;
        photoOptions.inSampleSize = scale;
        photoOptions.inPurgeable = true;
        return BitmapFactory.decodeFile(photoFile.toString(), photoOptions);
    }

}
