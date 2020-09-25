package com.example.deeplinkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class Main2Activity extends AppCompatActivity {
      public WebView myWebview;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myWebview =findViewById(R.id.webview);
        String urllink= getIntent().getStringExtra("link");
        myWebview.loadUrl(urllink);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

}
