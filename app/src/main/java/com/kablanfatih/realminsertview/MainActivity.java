package com.kablanfatih.realminsertview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Realm realm;
    EditText userName, password, name;
    RadioGroup genderGroup;
    Button saveButton, updateButton;
    Button deleteYesButton, deleteNoButton, updateYesButton, updateNoButton;//alert buttons
    ListView listView;
    Integer positionT = 0;
    String usernameText, nameText, genderText, passwordText;
    RealmResults<PersonInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DefineRealm();
        Inıt();
        show();
        positionFind();
        Add();
    }

    public void DefineRealm() {

        realm = Realm.getDefaultInstance();

    }

    public void Inıt() {
        listView = (ListView) findViewById(R.id.listView);
        userName = (EditText) findViewById(R.id.editTextUserName);
        name = (EditText) findViewById(R.id.editTextName);
        genderGroup = (RadioGroup) findViewById(R.id.GenderRadio);
        password = (EditText) findViewById(R.id.editTextPassword);
        saveButton = (Button) findViewById(R.id.registerButton);
        updateButton = (Button) findViewById(R.id.update);
    }


    public void Add() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takeInfo();

                write(genderText, passwordText, usernameText, nameText);

                userName.setText("");
                name.setText("");
                password.setText("");

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bringFromDatabase();
                final PersonInfo person = list.get(positionT);
                takeInfo();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        person.setGender(genderText);
                        person.setUsername(usernameText);
                        person.setName(nameText);
                        person.setPassword(passwordText);

                    }
                });

                show();
            }
        });
    }

    public void write(final String gender, final String name, final String username, final String password) {

        if (usernameText.isEmpty() || nameText.isEmpty() || passwordText.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention");
            builder.setMessage("Please,fill in all fields");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        } else {

            realm.executeTransactionAsync(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {


                    PersonInfo personInfo = realm.createObject(PersonInfo.class);
                    personInfo.setGender(gender);
                    personInfo.setName(name);
                    personInfo.setUsername(username);
                    personInfo.setPassword(password);

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    show();

                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {

                    Toast.makeText(getApplicationContext(), "UnSuccess", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void show() {

        bringFromDatabase();
      /* for (PersonInfo k : personInfo){

            Log.i("Gelenler: ",k.toString());
        }*/

        if (list.size() > 0) {

            adapter adapter = new adapter(list, getApplicationContext());

            listView.setAdapter(adapter);
        }
    }

    public void positionFind() {

        bringFromDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Log.i("Position",""+position);
                //delete(position);
                openAlert(position);
                userName.setText(list.get(position).getUsername());
                password.setText(list.get(position).getPassword());
                name.setText(list.get(position).getName());

                if (list.get(position).getGender().equals("Man")) {

                    ((RadioButton) genderGroup.getChildAt(0)).setChecked(true);

                } else {
                    ((RadioButton) genderGroup.getChildAt(1)).setChecked(true);
                }

                positionT = position;


            }
        });
    }

    public void delete(final int position) {


        //Log.i("PositionGelen",""+position);

        bringFromDatabase();

        Log.i("List", "" + list.get(position).getUsername());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                PersonInfo person = list.get(position);
                person.deleteFromRealm();
                show();
                userName.setText("");
                name.setText("");
                password.setText("");
                //genderGroup.clearCheck();
            }
        });
    }

    public void openAlert(final int position) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alertlayout, null);
        deleteYesButton = (Button) view.findViewById(R.id.yes);
        deleteNoButton = (Button) view.findViewById(R.id.no);
        updateYesButton = (Button) view.findViewById(R.id.yesUpdate);
        updateNoButton = (Button) view.findViewById(R.id.noUpdate);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        deleteYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
                dialog.cancel();
            }
        });

        dialog.show();

        deleteNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

                userName.setText("");
                name.setText("");
                password.setText("");
                //genderGroup.clearCheck();
            }
        });

        updateYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Update");
                builder.setMessage("If you want to update.Changed the columns and click the update button");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                dialog.cancel();

            }
        });

        updateNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

                userName.setText("");
                name.setText("");
                password.setText("");
                // genderGroup.clearCheck();
            }
        });
    }

    public void takeInfo() {

        Integer id = genderGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(id);
        genderText = radioButton.getText().toString();
        usernameText = userName.getText().toString();
        nameText = name.getText().toString();
        passwordText = password.getText().toString();
    }

    public void bringFromDatabase() {

        list = realm.where(PersonInfo.class).findAll();

    }
}
