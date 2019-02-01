package me.nelsontran.yourrecipes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipe_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create recipe table
        db.execSQL(Recipe.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + Recipe.TABLE_NAME);
        onCreate(db);
    }

    public long insertRecipe(String recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Recipe.COLUMN_RECIPE, recipe);
        long id = db.insert(Recipe.TABLE_NAME,null,values);

        db.close();
        return id;
    }

    public Recipe getRecipe(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Recipe.TABLE_NAME,
                new String[]{Recipe.COLUMN_ID, Recipe.COLUMN_RECIPE, Recipe.COLUMN_TIMESTAMP},
                Recipe.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Recipe recipe = new Recipe(
                cursor.getInt(cursor.getColumnIndex(Recipe.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Recipe.COLUMN_RECIPE)),
                cursor.getString(cursor.getColumnIndex(Recipe.COLUMN_TIMESTAMP))
        );

        cursor.close();
        return recipe;
    }

    public List<Recipe> getAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        //Select All
        String selectQuery = "SELECT * FROM " + Recipe.TABLE_NAME + " ORDER BY " + Recipe.COLUMN_TIMESTAMP + " DESC";
        //String selectQuery = "SELECT * FROM " + Recipe.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop thru rows and add to list
        if (cursor.moveToFirst()){
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndex(Recipe.COLUMN_ID)));
                recipe.setRecipe(cursor.getString(cursor.getColumnIndex(Recipe.COLUMN_RECIPE)));
                recipe.setTimestamp(cursor.getString(cursor.getColumnIndex(Recipe.COLUMN_TIMESTAMP)));

                recipes.add(recipe);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return recipes;
    }

    public int getRecipesCount(){
        String countQuery = "SELECT * FROM " + Recipe.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Recipe.COLUMN_RECIPE, recipe.getRecipe());

        return db.update(Recipe.TABLE_NAME, values, Recipe.COLUMN_ID + " = ?",
                new String[]{String.valueOf(recipe.getId())});
    }

    public void deleteRecipe(Recipe recipe){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Recipe.TABLE_NAME, Recipe.COLUMN_ID + " = ?",
                new String[]{String.valueOf(recipe.getId())});
        db.close();
    }
}
