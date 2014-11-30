package org.rickosborne.badger;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.rickosborne.badger.data.User;

import java.util.Collection;

public class BadgerApp extends Application {

    private Api badgerApi = null;
    private User currentUser = null;
    private Collection<User> patients = null;

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

    private class PatientsTask extends AsyncTask<AfterGetPatients, Void, AfterGetPatients> {
        @Override
        protected AfterGetPatients doInBackground(AfterGetPatients... params) {
            if (badgerApi != null) patients = badgerApi.getPatientsForUser(currentUser.getId());
            return params.length > 0 ? params[0] : null;
        }
        @Override
        protected void onPostExecute(AfterGetPatients after) {
            if (after != null) after.complete(patients);
        }
    }

    public void getPatients(AfterGetPatients after) {
        synchronized (Api.class) {
            if (patients != null) {
                if (after != null) after.complete(patients);
                return;
            }
            if (currentUser == null) return;
            if (badgerApi == null) openApi(currentUser.getUsername(), currentUser.getPassword());
            (new PatientsTask()).execute(after);
        }
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
            SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove(PREF_USER);
            editor.commit();
        }
    }


}
