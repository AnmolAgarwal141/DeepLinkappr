package com.example.deeplinkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class MainActivity extends AppCompatActivity {
    public TextView textView;
    public Button sharebutton,webviewbutton;
    public EditText urllink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textview);
        urllink=findViewById(R.id.edittext);
        sharebutton=findViewById(R.id.button);
        webviewbutton=findViewById(R.id.button2);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedata();
            }
        });
        Dynamiclink();

    }
    public void sharedata(){
        if(!TextUtils.isEmpty(urllink.getText().toString())) {
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(buildDynamicLink())
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {

                            if (task.isSuccessful()) {
                                //Uri previewLink = task.getResult().getPreviewLink();
                                Uri shortLink = task.getResult().getShortLink();
                                shareApp(shortLink.toString());
                            } else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Empty fields not allowed ,Enter a link", Toast.LENGTH_SHORT).show();
        }
    }
    private Uri buildDynamicLink(){
        String str=urllink.getText().toString();

        String uri =  "https://mydeeplink141.page.link/" +
                "?link=" + str +""+
                "&apn=" + getPackageName() +
                "&ibn=" + "name"+
                "&st=" + "My Deep Link" +
                "&sd=" + "This app page consist information of link"+
                "&afl="+"https://appdistribution.firebase.dev/i/bfa94b677fd95a4d";
        return Uri.parse(uri);
        }
    private void shareApp(String uri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,uri);
        intent.setType("text/plain");
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Dynamiclink();
    }

    public void Dynamiclink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Uri deeplink = null;
                if (pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.getLink();
                }
                if (deeplink != null) {

                    final String text=deeplink.toString();
                    String linkedText = "Text with a " +
                            String.format("<a href=\"%s\">link</a> ", text);

                    textView.setText(Html.fromHtml(linkedText));
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                    webviewbutton.setVisibility(View.VISIBLE);
                    webviewbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent webviewIntent=new Intent(MainActivity.this,Main2Activity.class);
                            webviewIntent.putExtra("link",text);
                            startActivity(webviewIntent);

                        }
                    });




                    Log.i("MainActivity", "deep link is" + deeplink.toString());
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("MainActivity", "exception" + e);
            }
        });
    }
}