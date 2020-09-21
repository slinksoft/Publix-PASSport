package com.slinksoft.passport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class OptionsActivity extends AppCompatActivity {

    String id, pass;
    EditText uid, password;
    File accPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        // file path object to check if file exists and open it; contains saved login info
        accPath = new File(getFilesDir() + "/PASSport/" + File.separator + "info.ss");

        // get UI references and store them into instance objects respectively
        uid = findViewById(R.id.idInput);
        password = findViewById(R.id.passwordInput);
        // if file doesn't exist, display message
        if (!accPath.exists())
        {
            AlertDialog note = new AlertDialog.Builder(OptionsActivity.this).create();
            note.setTitle("INFO");
            note.setMessage("Set your User ID and Password to allow the app to auto-fill your " +
                    "information into each respective text-boxes to make logging in easier and faster. \n\nPLEASE NOTE: YOUR INFORMATION" +
                    " IS ONLY STORED ONTO YOUR LOCAL DEVICE! IT IS NOT SENT ANYWHERE ELSE!\n\nBy entering your " +
                    "credentials and tapping \"Save\", a file will be created on your device storing your credentials and" +
                    " you will no longer see this message.");
            note.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

            note.show();
        }
        // if file does exist, read saved login info and fill them into the text fields onto the UI
        else
        {
            try
            {
                Scanner read = new Scanner(new FileReader(accPath));
                id = read.nextLine();
                pass = read.nextLine();
                uid.setText(id);
                password.setText(pass);
                read.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // restart app when Back button is pressed
    public void backToMain (View v) {
        restartAppMain();
    }

    // close and restart app
    private void restartAppMain()
    {
        finish();
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Intent) i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    // restart app when system back button is pressed
    @Override
    public void onBackPressed() {
        restartAppMain();
    }

    public void saveCredentials (View v) throws IOException
    {
        // if any text fields blank, display error message
        if (uid.getText().equals("") || password.getText().equals(""))
        {
            AlertDialog error = new AlertDialog.Builder(OptionsActivity.this).create();
            error.setTitle("ERROR");
            error.setMessage("Do not leave the \"User ID\" and/or \"Password\" fields blank!");
            error.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

            error.show();
        }
        else
        {
            File path = new File(getFilesDir() + "/PASSport");
            // if file and directory do not exist, credit directory based on above local variable and file from accPath instance variable
            if (!accPath.exists()) {
                path.mkdirs();
                accPath.createNewFile();
            }
            // create new print writer object, open info.ss then write credentials to file
            PrintWriter write = new PrintWriter(new FileOutputStream(accPath, false));
            write.println(uid.getText());
            write.print(password.getText());
            write.close(); // close printwriter instance to provent any future IO exceptions
            Toast.makeText(getApplicationContext(), "Successfully Saved Info!", Toast.LENGTH_SHORT).show();
        }
    }
}