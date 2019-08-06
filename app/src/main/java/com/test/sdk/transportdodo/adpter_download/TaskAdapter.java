package com.test.sdk.transportdodo.adpter_download;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.test.sdk.transportdodo.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.QueueViewHolder>
{

  TaskController queueController;

    public TaskAdapter(TaskController queueController) {
        this.queueController = queueController;
    }

    @Override
    public QueueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QueueViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_downloader, parent, false));
    }
    @Override public void onBindViewHolder(QueueViewHolder holder, int position) {
        queueController.bind(holder,position);
    }

    @Override public int getItemCount() {
        return queueController.size();
    }

    static class QueueViewHolder extends RecyclerView.ViewHolder {

        Button actionTv;
        TextView statusTv,nameTv,speedTv;
        ProgressBar progressBar;

        QueueViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            statusTv = itemView.findViewById(R.id.statusTv);
            speedTv = itemView.findViewById(R.id.speedTv);
            actionTv = itemView.findViewById(R.id.actionTv);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }



}
