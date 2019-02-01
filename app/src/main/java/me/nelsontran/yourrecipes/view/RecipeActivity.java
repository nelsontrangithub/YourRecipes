package me.nelsontran.yourrecipes.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.nelsontran.yourrecipes.R;

public class RecipeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        getIncomingIntent();

    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("recipe")){
            String recipeText = getIntent().getStringExtra("recipe");
            setRecipeText(recipeText);
        }
    }

    private void setRecipeText(String recipeText){
        TextView recipe = findViewById(R.id.recipe);
        recipe.setText(recipeText);
    }
}
