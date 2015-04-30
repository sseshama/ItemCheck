package apps.ssw555.com.itemcheck;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class ItemsActivity extends ActionBarActivity {

    ListView itemsView;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    Button addItemButton;
    EditText addItemEditText;
    String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        addItemButton = (Button)findViewById(R.id.add_item_button);

        listName = getIntent().getStringExtra(String.valueOf(R.string.ListName));
        items = ListsAndItems.getListUsingKey(listName);

        addItemEditText = (EditText)findViewById(R.id.item_edit_Text);
        addItemEditText.clearFocus();
        itemsView = (ListView)findViewById(R.id.item_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,items);
        itemsView.setAdapter(adapter);

        registerForContextMenu(itemsView);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToItems();
            }
        });

        itemsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView check = (CheckedTextView)view;
                check.setChecked(!check.isChecked());

                if (check.isChecked()) {
                    String completedItem = items.get(position);
                    ListsAndItems.getCompleted().add(completedItem);
                }

            }
        });
    }

    private void addToItems()
    {
        String text = addItemEditText.getText().toString().trim();

        if (text.length() <= 0)
            return;

        addNewItem(text);
        addItemEditText.setText("");
    }

    private void addNewItem(String newItem) {
        ArrayList<String> listItems = ListsAndItems.getListUsingKey(listName);
        listItems.add(newItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.item_view)
            return;

        menu.setHeaderTitle(R.string.Prompt);
        menu.add(R.string.DeleteItem);
        menu.add(R.string.AddToFavorites);
        menu.add(R.string.Return);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int selectedIndex = info.position;


        if (item.getTitle().equals(getResources().getString(R.string.DeleteItem))) {
            deleteItem(selectedIndex);
        }

        if (item.getTitle().equals(getResources().getString(R.string.AddToFavorites))) {
            ListsAndItems.getFavorites().add(items.get(selectedIndex));
        }

        return true;
    }

    private void deleteItem(int selectedIndex) {
        ArrayList<String> listItems = ListsAndItems.getListUsingKey(listName);
        listItems.remove(selectedIndex);
        adapter.notifyDataSetChanged();
    }

}
