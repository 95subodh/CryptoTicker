package com.apps.sky.cryptoticker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view)
    {
        EditText editText = (EditText) findViewById(R.id.edittext_id);

        Intent intent = new Intent(MainActivity.this, ApiTestActivity.class);
        intent.putExtra("crypto",editText.getText().toString().toLowerCase());
        startActivity(intent);
    }
}
