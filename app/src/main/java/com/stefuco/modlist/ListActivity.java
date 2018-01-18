package com.stefuco.modlist;

import android.content.res.Resources;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class ListActivity extends AppCompatActivity {

    ImageButton addButton = null;
    ImageButton delButton = null;
    ImageButton clearButton = null;
    ListView list = null;
    Resources res = null;
    EditText listTitle = null;
    ArrayList<String> dataList = null;
    ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        res = getResources();
        addButton = findViewById(R.id.ibtn_add);
        delButton = findViewById(R.id.ibtn_delete);
        clearButton = findViewById(R.id.ibtn_clean);
        list = findViewById(R.id.list_content);
        listTitle = findViewById(R.id.et_titre);

        //dataList = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.list_content)));
        dataList = new ArrayList<>();
        /*adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, dataList );*/
        adapter = new ArrayAdapter<>(this, R.layout.item_list,
                R.id.task_title, dataList );
        loadList();

        list.setAdapter(adapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(ListActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(ListActivity.this)
                        .setTitle("Add a new task")
                        .setMessage("")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                dataList.add(task);
                                adapter.notifyDataSetChanged();
                                saveList();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }

        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataList.size() > 0){
                    dataList.remove(dataList.size()-1);
                    adapter.notifyDataSetChanged();
                    saveList();
                }
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.clear();
                adapter.notifyDataSetChanged();
                saveList();
            }
        });

    }

    private void saveList(){
        String outputString = "";
        for(String element : dataList )
        {
            outputString = outputString + element + ";";
        }
        writeToFile(outputString, this);
    }

    private void loadList(){
        String inputString = readFromFile(this);
        String[] elements = inputString.split(";");
        dataList.clear();
        for(String e : elements){
            dataList.add(e);
        }
        adapter.notifyDataSetChanged();
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("list.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("list.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}
