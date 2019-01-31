package me.nelsontran.yourrecipes.database;

public class Recipe {
    public static final String TABLE_NAME = "recipes";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RECIPE = "recipe";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String recipe;
    private String timestamp;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_RECIPE + " TEXT,"
            + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")";

    public Recipe(){

    }

    public Recipe(int id, String recipe, String timestamp) {
        this.id = id;
        this.recipe = recipe;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
