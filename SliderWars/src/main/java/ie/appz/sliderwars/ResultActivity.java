package ie.appz.sliderwars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ResultActivity extends Activity {
    public static final String PREFS_NAME = "ie.appz.sliderwars.preferences";
    public static final String HIGHEST_HITS = "highest_hits";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highestHits = sharedPreferences.getInt(HIGHEST_HITS, 0);
        int resultInt = getIntent().getIntExtra(playActivity.RESULT_INT, 0);
        if (resultInt > highestHits) {
            highestHits = resultInt;
            sharedPreferences.edit().putInt(HIGHEST_HITS, highestHits).commit();
        }
        ((TextView) findViewById(R.id.resultText)).setText("Good Job, you scored " + resultInt + "!");
        ((TextView) findViewById(R.id.highestHits)).setText("Your highest score is " + highestHits + ".");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I just got " + getIntent().getIntExtra(playActivity.RESULT_INT, 0) + " hits playing " + getResources().getString(R.string.app_name) + "!" + " http://j.mp/1hd3EBz");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share with..."));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
