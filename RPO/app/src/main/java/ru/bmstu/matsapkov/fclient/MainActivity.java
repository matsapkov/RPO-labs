package ru.bmstu.matsapkov.fclient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.bmstu.matsapkov.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements TransactionEvents {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;
    private String pin;
    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // обработка результата
                        //String pin = data.getStringExtra("pin");
                        //Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                        pin = data.getStringExtra("pin");
                        synchronized (MainActivity.this) {
                            MainActivity.this.notifyAll();
                        }
                    }
                }
        );

        int i = initRng();
        byte[] randomBytes = randomBytes(16);
        Log.println(Log.ERROR, "MISHA molodez", Arrays.toString(randomBytes));
        byte[] encrypted = encrypt(randomBytes, randomBytes);
        Log.println(Log.ERROR, "SEMEN molodez", Arrays.toString(encrypted));
        byte[] decrypted = decrypt(randomBytes, encrypted);
        Log.println(Log.ERROR, "ROMAN molodez", Arrays.toString(decrypted));
        findViewById(R.id.sample_button).setOnLongClickListener(view->{
            testHttpClient();
            return true;
        });

    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
    public native boolean transaction(byte[] trd);
    public void onButtonClick(View v)
    {
        new Thread(()-> {
            try {
                byte[] trd = stringToHex("9F0206000000000100");
                transaction(trd);

            } catch (Exception ex) {
                // todo: log error
            }
        }).start();
    }

    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }

    @Override
    public String enterPin(int ptc, String amount) {
        pin = new String();
        Intent it = new Intent(MainActivity.this, PinpadActivity.class);
        it.putExtra("ptc", ptc);
        it.putExtra("amount", amount);
        synchronized (MainActivity.this) {
            activityResultLauncher.launch(it);
            try {
                MainActivity.this.wait();
            } catch (Exception ex) {
                //todo: log error
            }
        }
        return pin;
    }

    @Override
    public void transactionResult(boolean result) {
        runOnUiThread(()-> {
            Toast.makeText(MainActivity.this, result ? "ok" : "failed", Toast.LENGTH_SHORT).show();
        });
    }
    protected String getPageTitle(String html)
    {
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        String p;
        if (matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
    }
    protected void testHttpClient()
    {
        new Thread(() -> {
            try {
                HttpURLConnection uc = (HttpURLConnection)
                         (new URL("http://10.0.2.2:8080/api/v1/title").openConnection());
                InputStream inputStream = uc.getInputStream();
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);
                runOnUiThread(() ->
                {
                    Toast.makeText(this, title, Toast.LENGTH_LONG).show();
                });

            } catch (Exception ex) {
                    Log.println(Log.ERROR, "Boba molodez", ex.toString());
            }
        }).start();
    }
}