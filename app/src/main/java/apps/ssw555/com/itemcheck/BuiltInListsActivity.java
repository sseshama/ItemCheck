package apps.ssw555.com.itemcheck;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class BuiltInListsActivity extends ActionBarActivity {

    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_built_in_lists);

        String listName = getIntent().getStringExtra(String.valueOf(R.string.BuiltInListName));

        if (listName.equals(getResources().getString(R.string.Favorites))) {
            items = ListsAndItems.getFavorites();
            setTitle(getResources().getString(R.string.Favorites));
        }
        else if (listName.equals(getResources().getString(R.string.Completed))) {
            items = ListsAndItems.getCompleted();
            setTitle(getResources().getString(R.string.Completed));
        }

        else
            items = new ArrayList<String>();

        listView = (ListView)findViewById(R.id.builtInLists);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
    }
}
