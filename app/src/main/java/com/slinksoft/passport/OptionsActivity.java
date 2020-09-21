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
        accPath = new File(getFilesDir() + "/PASSport/" + File.separator + "info.ss");

        uid = findViewById(R.id.idInput);
        password = findViewById(R.id.passwordInput);
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

    public void backToMain (View v) {
        restartAppMain();
    }

    private void restartAppMain()
    {
        finish();
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Intent) i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        restartAppMain();
    }

    public void saveCredentials (View v) throws IOException
    {
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
            if (!accPath.exists()) {
                path.mkdirs();
                accPath.createNewFile();
            }
            PrintWriter write = new PrintWriter(new FileOutputStream(accPath, false));
            write.println(uid.getText());
            write.print(password.getText());
            write.close();
            Toast.makeText(getApplicationContext(), "Successfully Saved Info!", Toast.LENGTH_SHORT).show();
        }
    }
}