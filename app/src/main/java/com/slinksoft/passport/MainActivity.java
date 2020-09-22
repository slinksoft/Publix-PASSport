package com.slinksoft.passport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    WebView browser;
    String id, pass, version;
    int v, rev;
    boolean autofilled, initialcheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // file path object to check if file exists and open it; contains saved login info

        final File accPath = new File(getFilesDir() + "/PASSport/" + File.separator + "info.ss");
        this.setTitle("Publix PASSport - By Slink Soft");

        // set version info
        v = 1;
        rev = 0;
        version = v + "." + rev;

        browser = findViewById(R.id.browserWV); // get UI WebView reference and store it into instance WebView object

        // set WebViewClient to perform action to the UI WebView reference when a page finishes loading
        browser.setWebViewClient(new WebViewClient()
        {
            public void onPageFinished(WebView view, String url) {
                // if info.ss containing login info doesn't exist and initialcheck is false, display first usage message
                if (!accPath.exists() && !initialcheck)
                {
                    initialcheck = true;
                    AlertDialog note = new AlertDialog.Builder(MainActivity.this).create();
                    note.setTitle("INFO");
                    note.setMessage("If this is your first time using the app, go to \"Options\" and " +
                            "fill in your login information to make logging in easier and faster!\n\n" +
                            "(NOTE: You will not stay logged in if you currently are due to session " +
                            "expiration and security purposes; therefore, re-logging in will be necessary!)");
                    note.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    note.show();
                }
                // if file exists but info has yet to be autofilled, read login info from info.ss and fill in to respective fields on the WebView
                else if (!autofilled)
                {
                    try
                    {
                        Scanner read = new Scanner(new FileReader(accPath));
                        id = read.nextLine();
                        pass = read.nextLine();
                        read.close();
                        autofilled = true;
                        browser.evaluateJavascript("document.getElementById(\"USER\").value =\"" + id + "\"", null);
                        browser.evaluateJavascript("document.getElementById(\"password\").value =\"" + pass + "\"", null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // necessary settings for the WebView in order for the PASSport web service to work
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setDomStorageEnabled(true);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setDisplayZoomControls(false);
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setDefaultTextEncodingName("utf-8");
        browser.loadUrl("https://www.publix.org/"); // navigate to passPort

    }

    public void options(View v)
    {
        // Start new OptionsActivity reference and open it, allowing the user to set their credentials for autofill
        Intent options = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(options);
    }

    public void backBrowser(View v)
    {
        browser.goBack();
    }

    public void forwardBrowser(View v)
    {
        browser.goForward();
    }

    public void takeScreenshot(View v) {
        Date now = new Date(); // create date object for screenshot file name usage
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now); // get current date and time

        try {
            // take screenshot
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            // create bitmap to store screen capture
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            // use MediaStore to save screenshot to default Pictures directory
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap , now + "pubscreenshot", "N/A");

            Toast.makeText(getApplicationContext(), "Screenshot saved. Check your photo gallery!", Toast.LENGTH_SHORT).show();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            Toast.makeText(getApplicationContext(), "An error occurred!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onCredits(View v)
    {
        AlertDialog credits = new AlertDialog.Builder(MainActivity.this).create();
        credits.setTitle("Credits");
        credits.setMessage("PASSport App - Version: " + version + "\n\nDeveloped By: Slink (Dan)\n\nNOTE: This app is " +
                "not affiliated with Publix Super Markets Inc.! This app is written " +
                "by a college student with intent of practicing, utilizing, and growing his current skills " +
                "to become a prospective software developer at Publix's Information Systems department in Lakeland.\n\n" +
                "Thank you for using this app!\n\n- Slink\n\nTo check on the latest updates of this app, visit the PASSport Github releases page by tapping the button below.");
        credits.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        credits.setButton(AlertDialog.BUTTON_POSITIVE, "Visit SlinkSoft",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        // navigates user to my portfolio upon click of the button
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://realslinksoft.wixsite.com/slink-soft-portfolio"));
                        startActivity(browserIntent);
                        dialogInterface.dismiss();
                    }
                });
        credits.setButton(AlertDialog.BUTTON_NEGATIVE, "Visit PASSport Github Releases",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        // navigates user to my portfolio upon click of the button
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/slinksoft/Publix-PASSport/releases"));
                        startActivity(browserIntent);
                        dialogInterface.dismiss();
                    }
                });

        credits.show();
    }
}