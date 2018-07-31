package com.example.a16004798.p11knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView lvFacts;
    private EditText etAccessCode;
    ArrayList<String> alSingaporeFacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAccessCode = findViewById(R.id.etAccessCode);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Boolean logged = prefs.getBoolean("logged", false);

        alSingaporeFacts = new ArrayList<>();

        ArrayAdapter<String> aaSingaporeFacts;

        lvFacts = (ListView) findViewById(R.id.lvFacts);
        alSingaporeFacts.add("You can find the national anthem in microtext on the back of the $1000 note.");
        alSingaporeFacts.add("National day falls on the 9th August");
        alSingaporeFacts.add("In 9th August 2018, Singapore turns 53");

        aaSingaporeFacts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alSingaporeFacts);
        lvFacts.setAdapter(aaSingaporeFacts);
        if(!logged){
            loginDialog();
        }
    }

    private void loginDialog() {

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout passPhrase =(LinearLayout) inflater.inflate(R.layout.login_dialog, null);
        final EditText etAccessCode = (EditText) passPhrase.findViewById(R.id.etAccessCode);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Login")
                .setView(passPhrase)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (etAccessCode.getText().toString().equals("738964")) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor prefEdit = prefs.edit();
                            prefEdit.putBoolean("logged", true);
                            prefEdit.apply();

                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Access Code", Toast.LENGTH_SHORT).show();
                            loginDialog();
                        }
                    }
                })
                .setNegativeButton("NO ACCESS CODE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "You need to have an access code", Toast.LENGTH_SHORT).show();
                        loginDialog();
                    }
                });

        AlertDialog alertDialog = builder.create();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor edit = pref.edit();

        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor prefEdit = prefs.edit();
                            prefEdit.putBoolean("logged", false);
                            prefEdit.apply();


                            finish();
                        }
                    })
                    .setNegativeButton("NOT REALLY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.send) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            String listContent = "";
                            if (which == 0) {
                                for (int i = 0; i < alSingaporeFacts.size(); i++) {
                                    listContent += alSingaporeFacts.get(i) + "\n";
                                }
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("plain/text");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"stupidpig@hotmail.com"});
                                intent.putExtra(Intent.EXTRA_SUBJECT, "National day facts");
                                intent.putExtra(Intent.EXTRA_TEXT, listContent);
                                startActivity(Intent.createChooser(intent, "TEST"));


                                Snackbar sb = Snackbar.make(lvFacts, "Email sent!", Snackbar.LENGTH_SHORT);

                                sb.show();
                            } else {
                                for (int i = 0; i < alSingaporeFacts.size(); i++) {
                                    listContent += alSingaporeFacts.get(i);
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + 90673011));
                                intent.putExtra("sms_body", listContent);
                                startActivity(intent);

                                Snackbar sb = Snackbar.make(lvFacts, "Message sent!", Snackbar.LENGTH_SHORT);

                                sb.show();
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            RadioGroup rg1 = quiz.findViewById(R.id.rg1);
                            RadioGroup rg2 = quiz.findViewById(R.id.rg2);
                            RadioGroup rg3 = quiz.findViewById(R.id.rg3);

                            //Get the ID of the selected radio button in the RadioGroup
                            int qn1SelectedId = rg1.getCheckedRadioButtonId();
                            int qn2SelectedId = rg2.getCheckedRadioButtonId();
                            int qn3SelectedId = rg3.getCheckedRadioButtonId();

                            //Get the radio button object from the Id we had gotten above
                            final RadioButton rb1 = quiz.findViewById(qn1SelectedId);
                            final RadioButton rb2 = quiz.findViewById(qn2SelectedId);
                            final RadioButton rb3 = quiz.findViewById(qn3SelectedId);

                            if ((rb1.getText().equals("No")) && (rb2.getText().equals("Yes")) && (rb3.getText().equals("Yes"))) {
                                Toast.makeText(MainActivity.this, "WELL DONE! ALL ANSWERS ARE CORRECT!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Not all answers are correct. Try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("DON'T KNOW LAH!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
