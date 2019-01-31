package me.nelsontran.yourrecipes.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.nelsontran.yourrecipes.R;
import me.nelsontran.yourrecipes.database.Recipe;

public class RecipeAdaptor extends RecyclerView.Adapter<RecipeAdaptor.MyViewHolder> {
    private Context context;
    private List<Recipe> recipeList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView recipe;
        public TextView dot;
        public TextView timestamp;

        public MyViewHolder(View view){
            super(view);
            recipe = view.findViewById(R.id.recipe);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
        }

    }

    public RecipeAdaptor(Context context, List<Recipe> recipeList){
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Recipe recipe = recipeList.get(i);

        myViewHolder.recipe.setText(recipe.getRecipe());
        myViewHolder.dot.setText(Html.fromHtml("&#8226;"));
        myViewHolder.timestamp.setText(formatDate(recipe.getTimestamp()));
    }

    private String formatDate(String timestamp) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
        Date date = null;
        try {
            date = fmt.parse(timestamp);
            return fmtOut.format(date); //returning String

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
