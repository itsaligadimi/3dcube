package com.agadimi.a3dcube;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModelsAdapter extends RecyclerView.Adapter<ModelsAdapter.ModelVH>
{
    public Context context;
    public OnClickListener onClickListener;
    public List<ListItem> data = new ArrayList<>();

    public ModelsAdapter(Context context)
    {
        this.context = context;
    }

    public void addItem(ListItem listItem)
    {
        int itemPos = data.size();
        data.add(listItem);
        notifyItemInserted(itemPos);
    }

    public ListItem getItemAt(int pos)
    {
        return data.get(pos);
    }

    public void setOnClickListener(OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ModelVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ModelVH(LayoutInflater.from(context).inflate(R.layout.row_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModelVH holder, int position)
    {
        holder.updateView(data.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    class ModelVH extends RecyclerView.ViewHolder
    {
        private TextView textView;

        public ModelVH(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.img);
        }

        public void updateView(ListItem listItem)
        {
            textView.setText(listItem.name);
            itemView.setOnClickListener((v) -> {
                onClickListener.onClick(listItem, getAdapterPosition());
            });
        }
    }

    public static class ListItem
    {
        private String name;
        private int object;

        public ListItem(String name, int object)
        {
            this.name = name;
            this.object = object;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getObject()
        {
            return object;
        }

        public void setObject(int object)
        {
            this.object = object;
        }
    }

    public interface OnClickListener
    {
        void onClick(ListItem listItem, int position);
    }
}
