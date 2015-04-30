package apps.ssw555.com.itemcheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class ListsActivity extends ActionBarActivity {

    EditText addList;
    Button addButton;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> lists;
    TextView completed;
    TextView favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        addList = (EditText) findViewById(R.id.addListText);
        addButton = (Button) findViewById(R.id.add_list_Button);
        listView = (ListView) findViewById(R.id.listView);
        completed = (TextView) findViewById(R.id.Completed);
        favorites = (TextView) findViewById(R.id.Favorites);

        lists = new ArrayList<String>();

        ListsAndItems.InitializeFromInputFile(getApplicationContext());

        for (String key : ListsAndItems.getListsAndItems().keySet())
            lists.add(key);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lists);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToLists();
            }
        });

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemsIntent = new Intent(getApplicationContext(), ItemsActivity.class);
                String listName = lists.get(position);
                itemsIntent.putExtra(String.valueOf(R.string.ListName), listName);
                startActivity(itemsIntent);
            }
        });


    }

    public void onTextViewClick (View v) {
        Intent builtInListItemsIntent = new Intent(getApplicationContext(), BuiltInListsActivity.class);
        TextView view = (TextView)v;
        String listName = view.getText().toString();
        builtInListItemsIntent.putExtra(String.valueOf(R.string.BuiltInListName), listName);
        startActivity(builtInListItemsIntent);
    }


    @Override
    protected void onDestroy() {
        Log.i("On Destroy", "Application Exited");
        //saveListsAndItems();
    }

    private void saveListsAndItems() {
        try {

            PrintWriter printWriter = new PrintWriter(openFileOutput("ItemCheck.txt", Context.MODE_PRIVATE));

            HashMap<String, ArrayList<String>> listsAndItems = ListsAndItems.getListsAndItems();

            for (String list : listsAndItems.keySet()) {
                ArrayList<String> items = listsAndItems.get(list);
                String itemsSeparatedByCommas = TextUtils.join(",", items);
                printWriter.println(String.format("%s:%s", list, itemsSeparatedByCommas));
            }

        } catch (Exception e) {
            Log.i("Back Button Pressed", e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        Log.i("Back Button Pressed", "The Back button was pressed");
        saveListsAndItems();
    }

    private void addToLists() {
        String text = addList.getText().toString().trim();

        if (text.length() <= 0)
            return;

        addNewList(text);
        addList.setText("");
    }

    private void addNewList(String newList) {
        lists.add(newList);
        ListsAndItems.getListsAndItems().put(newList, new ArrayList<String>());
        adapter.notifyDataSetChanged();
    }

    private void deleteList(int selectedIndex) {
        String listName = lists.get(selectedIndex);
        lists.remove(selectedIndex);
        adapter.notifyDataSetChanged();
        ListsAndItems.getListsAndItems().remove(listName);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listView)
            return;

        menu.setHeaderTitle(R.string.Prompt);
        menu.add(R.string.Rename);
        menu.add(R.string.DeleteList);
        menu.add(R.string.EmailList);
        menu.add(R.string.Return);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;

        if (item.getTitle().equals(getResources().getString(R.string.DeleteList))) {
            deleteList(selectedIndex);
        }

        if (item.getTitle().equals(getResources().getString(R.string.Rename))) {
            renameListDialog(selectedIndex);
        }

        if (item.getTitle().equals(getResources().getString(R.string.EmailList))) {
            String listName = lists.get(selectedIndex);
            sendEmail(listName, ListsAndItems.getListUsingKey(listName));
        }


        return true;
    }

    private void sendEmail(String listName, ArrayList<String> listUsingKey) {
        Log.i("Send email", "");

        String[] TO = {"amrood.admin@gmail.com"};
        String[] CC = {"mcmohd@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, listName);
        String fullList = listUsingKey.toString();
        emailIntent.putExtra(Intent.EXTRA_TEXT, fullList);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Email sent...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,"There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void renameListDialog(final int selectedIndex) {
        AlertDialog.Builder alert = new AlertDialog.Builder(
                this);
        alert.setTitle("Rename");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newListName = input.getEditableText().toString();
                renameList(selectedIndex, newListName);
            }
        });

        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void renameList (int selectedIndex, String newListName) {
        String key = lists.get(selectedIndex);
        ArrayList<String> items = ListsAndItems.getListUsingKey(key);
        ListsAndItems.removeKeyValue(key);
        ListsAndItems.putListUsingKey(newListName,items);
        lists.remove(selectedIndex);
        lists.add(selectedIndex, newListName);
        adapter.notifyDataSetChanged();
    }


}
