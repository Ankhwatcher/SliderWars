package ie.appz.sliderwars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class playActivity extends Activity {

    public static String RESULT_INT = "result_int";
    Toast targetHit = null;
    TextView countdownText;
    int hitCount = 0;
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
    ArrayList<SeekBar> arrayList = new ArrayList<SeekBar>();
    Thread countDownThread = new Thread(new Runnable() {
        @Override
        public void run() {
            for (int j = 10; j > 0; j--) {
                final int seconds = j;
                synchronized (this) {
                    try {
                        ((Object) this).wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countdownText.setText(seconds + "s");
                        }
                    });


                }
            }
            synchronized (this) {
                appRunning = false;
                Intent intent = new Intent(playActivity.this, ResultActivity.class);
                intent.putExtra(RESULT_INT, hitCount);
                startActivity(intent);
            }
        }
    });
    Thread backgroundThread = new Thread(new Runnable() {
        public void run() {
            while (true)


            {

                synchronized (this) {
                    if (!appRunning)
                        break;

                    try {
                        ((Object) this).wait(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    List<SeekBar> seekBarListB = new ArrayList<SeekBar>();
                    for (SeekBar seekBar : arrayList) {
                        int progress = seekBar.getProgress();
                        int seekIncrement = seekBar.getMax() / 20;
                        progress += seekIncrement;
                        if (progress == seekBar.getMax()) {

                            seekBar.setProgress(0);
                            hitCount++;
                            targetHit.show();
                        } else {
                            seekBarListB.add(seekBar);
                            seekBar.setProgress(progress);
                        }

                    }
                    arrayList.clear();
                    arrayList.addAll(seekBarListB);
                }
            }

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.container);
        countdownText = (TextView) findViewById(R.id.countdown);
        targetHit = Toast.makeText(this, "Target Hit!", Toast.LENGTH_SHORT);
        backgroundThread.start();

        for (int i = 0; i < 3; i++) {
            RelativeLayout interract = (RelativeLayout) getLayoutInflater().inflate(R.layout.interract_layout, null);

            final SeekBar seekBar1 = ((SeekBar) interract.findViewById(R.id.seekBar1));
            seekBar1.setOnSeekBarChangeListener(disableSeek);

            Button fireButton = (Button) interract.findViewById(R.id.fireButton);
            fireButton.setText(String.valueOf(i + 1));
            fireButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {


                    synchronized (this) {
                        if (!countDownThread.isAlive())
                            countDownThread.start();
                        arrayList.add(seekBar1);
                    }

                }
            });
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
}

