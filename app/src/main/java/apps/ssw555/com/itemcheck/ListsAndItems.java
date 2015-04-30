package apps.ssw555.com.itemcheck;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by shreyas on 4/23/15.
 */
public class ListsAndItems {

    private static HashMap<String, ArrayList<String>> listsAndItems;


    private static ArrayList<String> favorites;
    private static ArrayList<String> completed;

    public static void InitializeFromInputFile (Context context) {

        try {
            Scanner scanner = new Scanner(context.openFileInput("ItemCheck.txt"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String [] listAndItems = line.split(":");
                String list, items;
                if (listAndItems.length > 0) {
                    list = listAndItems[0];
                    items = listAndItems[1];

                    ArrayList<String> itemList = new ArrayList<String>();
                    for (String item : items.split(",")) {
                        itemList.add(item);
                    }

                    listsAndItems.put(list, itemList);
                }
            }

            scanner.close();

        } catch (Exception e) {
            Log.i("On Initialize", e.getMessage());
        }

    }

    public static HashMap<String, ArrayList<String>> getListsAndItems () {

        if (listsAndItems == null) {
            listsAndItems = new HashMap<String, ArrayList<String>>();
        }

        return listsAndItems;
    }


    public static ArrayList<String> getListUsingKey (String key) {
        if (listsAndItems == null || !listsAndItems.containsKey(key))
            return new ArrayList<String>();

        return listsAndItems.get(key);
    }

    public static void putListUsingKey (String key, ArrayList<String> list) {
       listsAndItems.put(key, list);
    }

    public static void removeKeyValue (String key) {
        listsAndItems.remove(key);
    }

    public static ArrayList<String> getFavorites() {
        if (favorites == null) {
            favorites = new ArrayList<String>();
        }
        return favorites;
    }

    public static ArrayList<String> getCompleted() {
        if (completed == null)
            completed = new ArrayList<String>();
        return completed;
    }

}
