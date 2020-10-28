package com.agadimi.a3dcube;

import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.agadimi.a3dcube.glversion.GlSceneView;

public class MainActivity extends AppCompatActivity
{
    private GlSceneView scene;
    private RecyclerView modelsRv;

    private ModelsAdapter modelsAdapter;
    private LinearLayoutManager layoutManager;
    private SnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        scene = findViewById(R.id.mesh_viewer);
        modelsRv = findViewById(R.id.models_rv);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        modelsRv.setLayoutManager(layoutManager);

        modelsRv.addItemDecoration(new SpaceItemDecoration(16, true, true));

//        snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(modelsRv);

        modelsAdapter = new ModelsAdapter(this);
        modelsAdapter.setOnClickListener(((listItem, position) -> scene.load(listItem.getObject())));
        modelsRv.setAdapter(modelsAdapter);

        addItems();
    }

    private void addItems()
    {
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Cube", R.raw.cube));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Icosphere", R.raw.icosphere));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Torus", R.raw.torus));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Human", R.raw.al));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("ROI", R.raw.roi));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Violin case", R.raw.violin_case));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Skyscaper", R.raw.skyscraper));
        modelsAdapter.addItem(new ModelsAdapter.ListItem("Minicooper", R.raw.minicooper));

        scene.load(modelsAdapter.getItemAt(0).getObject());
    }
}
