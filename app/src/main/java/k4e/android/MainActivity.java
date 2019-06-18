package k4e.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.niqdev.mjpeg.DisplayMode;
import com.github.niqdev.mjpeg.Mjpeg;
import com.github.niqdev.mjpeg.MjpegInputStream;
import com.github.niqdev.mjpeg.MjpegView;

import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RaspberryPiCamera";
    private final String STREAM_URL = "http://192.168.11.119:8080/?action=stream";
    private MjpegView mjpegView;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mainActivity = this;

        mjpegView = (MjpegView)findViewById(R.id.mjpeg_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIpCam();
    }

    private void loadIpCam() {
        Log.d(TAG, "Connecting");
        Mjpeg.newInstance()
                .open(STREAM_URL, 30)
                .subscribe(new Subscriber<MjpegInputStream>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Subscription Completed");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(mainActivity, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(MjpegInputStream mjpegInputStream) {
                        mjpegView.setSource(mjpegInputStream);
                        mjpegView.setDisplayMode(DisplayMode.BEST_FIT);
                        mjpegView.showFps(true);

                    }
                });
    }
}
