package ru.bmstu.matsapkov.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;

import ru.bmstu.matsapkov.fclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        TextView tv = binding.sampleText;
        tv.setText(stringFromJNI());

        int i = initRng();
        byte[] randomBytes = randomBytes(16);
        Log.println(Log.ERROR,"MISHA LOX" , Arrays.toString(randomBytes));
        byte[] encrypted = encrypt(randomBytes, randomBytes);
        Log.println(Log.ERROR, "SEMEN LOX", Arrays.toString(encrypted));
        byte[] decrypted = decrypt(randomBytes, encrypted);
        Log.println(Log.ERROR, "ROMAN LOX", Arrays.toString(decrypted));

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
}