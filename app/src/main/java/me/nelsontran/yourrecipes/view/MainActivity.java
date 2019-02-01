package me.nelsontran.yourrecipes.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.nelsontran.yourrecipes.R;
import me.nelsontran.yourrecipes.database.DatabaseHelper;
import me.nelsontran.yourrecipes.database.Recipe;
import me.nelsontran.yourrecipes.utils.MyDividerItemDecoration;
import me.nelsontran.yourrecipes.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private RecipeAdaptor adaptor;
    private List<Recipe> recipeList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noRecipesView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        noRecipesView = findViewById(R.id.empty_recipes_view);

        db = new DatabaseHelper(this);

        recipeList.addAll(db.getAllRecipes());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                showRecipeDialog(false, null, -1);
            }
        });

        adaptor = new RecipeAdaptor(this, recipeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(adaptor);

        toggleEmptyRecipes();

        //long press to open alert dialog with options to edit/delete
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Recipe recipe1 = recipeList.get(position);
                String recipeText = recipe1.getRecipe();

                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra("recipe", String.valueOf(recipeText)); //passing recipe content data
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void createRecipe(String recipe) {
        long id = db.insertRecipe(recipe);
        Recipe recipe1 = db.getRecipe(id);
        if (recipe1 != null){
            recipeList.add(0, recipe1);
            adaptor.notifyDataSetChanged();
            toggleEmptyRecipes();
        }
    }

    public void deleteRecipe(int position){
        db.deleteRecipe(recipeList.get(position)); //removing from db

        recipeList.remove(position);
        adaptor.notifyItemRemoved(position);
        toggleEmptyRecipes();
    }

    private void updateRecipe(String recipe, int position){
        Recipe recipe1 = recipeList.get(position);
        recipe1.setRecipe(recipe);

        db.updateRecipe(recipe1); // updating note in db

        recipeList.set(position, recipe1);
        adaptor.notifyItemChanged(position);
        toggleEmptyRecipes();
    }

    private void showActionsDialog(final int position){
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    showRecipeDialog(true, recipeList.get(position), position);
                } else{
                    deleteRecipe(position);
                }
            }
        });
        builder.show();
    }

    private void showRecipeDialog(final boolean shouldUpdate, final Recipe recipe, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.recipe_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputRecipe = view.findViewById(R.id.recipe);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_recipe_title) : getString(R.string.lbl_edit_recipe_title));

        if (shouldUpdate && recipe != null) {
            inputRecipe.setText(recipe.getRecipe());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputRecipe.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter recipe!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating recipe
                if (shouldUpdate && recipe != null) {
                    // update recipe by it's id
                    updateRecipe(inputRecipe.getText().toString(), position);
                } else {
                    // create new recipe
                    createRecipe(inputRecipe.getText().toString());
                }
            }
        });
    }

    private void toggleEmptyRecipes(){
        if (db.getRecipesCount() > 0 && db.getAllRecipes() != null){
            noRecipesView.setVisibility(View.GONE);
        } else {
            noRecipesView.setVisibility(View.VISIBLE);
        }
    }
}
