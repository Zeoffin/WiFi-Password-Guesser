package lv.slysoft.wifiguesser;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    EditText editText;
    ArrayList<String> groceries = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> arrayAdapter;
    ListView listView;
    TextView list_content;
    EditText wifiName;


    //Save the list method
    public void saveGroceriesList() {
        sharedPreferences = this.getSharedPreferences("lv.slysoft.wifiguesser", MODE_PRIVATE);
        HashSet<String> set = new HashSet(groceries);
        sharedPreferences.edit().putStringSet("Groceries", set).apply();
    }


    // Try to connect
    public void connect() {

        // Get count of listview items
        int count = listView.getAdapter().getCount();

//        listView.getAdapter().getItem();

        for(int i=0; i<count; i++) {

            listView.getAdapter().getItem(i);

        }

        // Wi-Fi related stuff
        String networkSSID = wifiName.getText().toString();
        String networkPass = "3501022718";

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";

        // For WPA
        conf.preSharedKey = "\""+ networkPass +"\"";

        WifiManager wifiManager = (WifiManager)getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Wi Fi button
        Button connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                connect();

            }
        });

        //Floating action button
        Button addNewItem = (Button) findViewById(R.id.addKeywordButton);
        addNewItem.setOnClickListener(this);

        //Edit Text
        editText = (EditText) findViewById(R.id.addKeyword);

        // wifi name
        wifiName = (EditText) findViewById(R.id.wifiName);

        //list view
        listView = (ListView) findViewById(R.id.keywordList);

        //Load on startup Groceries
        sharedPreferences = this.getSharedPreferences("lv.slysoft.wifiguesser", MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("Groceries",null);
        if (set == null) {
            groceries.add("Turēt, lai izdzēstu");
        } else {
            groceries = new ArrayList(set);
        }

        arrayAdapter = new adapter(this, groceries);
        listView.setAdapter(arrayAdapter);

        //list view click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                list_content = (TextView) view.findViewById(R.id.list_content);

                if (list_content.getCurrentTextColor() == Color.parseColor("#000000")){ //Check if item is checked or not

                    list_content.setTextColor(Color.parseColor("#a7a7a7"));
                    list_content.setPaintFlags(list_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                } else {

                    list_content.setTextColor(Color.parseColor("#000000"));
                    list_content.setPaintFlags(list_content.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

                }

            }
        });

        //List view long-click listener
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                groceries.remove(position);
                arrayAdapter.notifyDataSetChanged();
                saveGroceriesList();

                return true;
            }
        });

    }

    //Scroll list view to bottom
    private void scrollListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view..
                listView.setSelection(arrayAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addKeywordButton:

                String userInput = editText.getText().toString();

                if(userInput.equals("")) { //Check if the string is empty

                    Toast.makeText(MainActivity.this, "ievadi", Toast.LENGTH_SHORT).show();

                } else if(groceries.contains(userInput)) { //Check if the item already exists

                    Toast.makeText(MainActivity.this, "eksistē", Toast.LENGTH_SHORT).show();

                } else {
                    //Add to list
                    groceries.add(userInput);

                    //Refresh ListView
                    arrayAdapter.notifyDataSetChanged();

                    //Set blank text
                    editText.setText("");

                    //Scrolls to down
                    scrollListViewToBottom();

                    saveGroceriesList();
                }

                break;
        }
    }

}

