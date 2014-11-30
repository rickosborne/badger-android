package org.rickosborne.badger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class CheckInPainActivity extends ActivityWithSVG {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_pain);
        loadSvgResource(R.drawable.smile, R.id.controlledContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.frown, R.id.moderateContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.grimace, R.id.severeContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.no, R.id.noContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.some, R.id.someContainer, R.dimen.icon_80);
        loadSvgResource(R.drawable.yes, R.id.yesContainer, R.dimen.icon_80);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_in_pain, menu);
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

    public void startMedications (View view) {
        startActivity(new Intent(this, PatientMedications.class));
    }
}
