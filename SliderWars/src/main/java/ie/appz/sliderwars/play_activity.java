package ie.appz.sliderwars;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class play_activity extends Activity {
    Runnable updateSeek = new UpdateRunnable();
    ArrayList<RelativeLayout> interractList = new ArrayList<RelativeLayout>();
    Boolean appRunning = true;
    SeekBar.OnSeekBarChangeListener disableSeek = new SeekBar.OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            progress = seekBar.getProgress();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.setProgress(progress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.container);


        for (int i = 0; i < 3; i++) {
            RelativeLayout interract = (RelativeLayout) getLayoutInflater().inflate(R.layout.interract_layout, null);
            ((TextView) interract.findViewById(R.id.fireButton)).setText(String.valueOf(i + 1));
            ((SeekBar) interract.findViewById(R.id.seekBar1)).setOnSeekBarChangeListener(disableSeek);
            ((SeekBar) interract.findViewById(R.id.seekBar2)).setOnSeekBarChangeListener(disableSeek);
            mainLayout.addView(interract, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
            interractList.add(interract);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (this) {
            appRunning = false;
        }
    }

    private class UpdateRunnable implements Runnable {
        ArrayList<SeekBar> arrayList = new ArrayList<SeekBar>();

        void setFiring(SeekBar seekBar) {
            arrayList.add(seekBar);
        }

        @Override
        public void run() {

            while (true) {
                synchronized (this) {
                    if (!appRunning)
                        break;
                    try {
                        ((Object) this).wait(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    for (SeekBar seekBar : arrayList) {
                        int progress = seekBar.getProgress();
                        int seekIncrement = seekBar.getMax() / 10;
                        progress += seekIncrement;
                        if (progress == seekBar.getMax()) {
                            arrayList.remove(seekBar);
                            seekBar.setProgress(0);
                        } else {

                        }

                    }
                }
            }
        }
    }
}
