package apps.ssw555.com.itemcheck;

/**
 * Created by shreyas on 4/29/15.
 */
public class Item {

    private String mItemName;
    private boolean mCompleted;

    public Item (String itemName) {
        mItemName = itemName;
    }

    public Item(String itemName, boolean completed) {
        this(itemName);
        mCompleted = completed;
    }

    public String getName() {
        if (mItemName == null)
            mItemName = "";
        return mItemName;
    }

    public void SetName (String name) {
        mItemName = name;
    }

    public boolean isCompleted () {
        return mCompleted;
    }

    public void setCompleted (boolean completed) {
        mCompleted = completed;
    }
}
