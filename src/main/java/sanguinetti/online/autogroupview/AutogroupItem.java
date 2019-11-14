package sanguinetti.online.autogroupview;

public class AutogroupItem {
    public static final int HEADER = 0;
    public static final int REGULARITEM = 1;

    private String text, subtext;
    private IAutogroupItem shownItem;
    private int type;

    public AutogroupItem(IAutogroupItem shownItem) {
        this.shownItem = shownItem;
        this.type = REGULARITEM;
    }

    public AutogroupItem(String text) {
        this.text = text;
        this.type = HEADER;
    }

    public String getText() {
        return (type == HEADER) ? text : shownItem.getDisplayName();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubtext() {
        return (type == HEADER) ? "" : shownItem.getSubtitle();
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public IAutogroupItem getShownItem() {
        return shownItem;
    }

    public void setShownItem(IAutogroupItem shownItem) {
        this.shownItem = shownItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
