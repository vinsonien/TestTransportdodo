package com.test.sdk.transportdodo.adpter_download;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import com.arialyy.aria.core.download.DownloadTask;
import com.test.sdk.transportdodo.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TaskStatusManager {


    public static final int WAIT = 1;//等待
    public static final int PRE = 2;//准备
    public static final int START = 3;//开始
    public static final int RUNNING = 4;//运行
    public static final int RESUME = 5;//继续
    public static final int STOP = 6;//暂停
    public static final int CANCEL = 7;//取消
    public static final int FAIL= 8;//失败
    public static final int COMPLETE = 9;//完成

    @IntDef({WAIT, PRE,START,RUNNING,RESUME,STOP,CANCEL,FAIL,COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusMode {
    }

    public static void ToWait(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_stop));
        holder.statusTv.setText("onWait");
    }

    public static void ToPre(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_stop));
        holder.statusTv.setText("onPre");
    }

    public static void ToTaskStart(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_stop));
        holder.statusTv.setText("onTaskStart");
    }

    public static void ToTaskRunning(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        long len = task.getFileSize();
        holder.actionTv.setText(context.getText(R.string.task_stop));
        holder.statusTv.setText("onTaskRunning");
        holder.progressBar.setProgress(task.getPercent());
        holder.speedTv.setText(task.getConvertSpeed());
    }

    public static void ToTaskResume(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_stop));
        holder.statusTv.setText("onTaskResume");
    }

    public static void ToStop(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_resume));
        holder.statusTv.setText("onTaskStop");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void ToTaskCancel(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.progressBar.setProgress(holder.progressBar.getMin());
        holder.actionTv.setText(context.getText(R.string.task_restart));
        holder.statusTv.setText("onTaskCancel");
    }

    public static void ToTaskFail(Context context, DownloadTask task, Exception e, TaskAdapter.QueueViewHolder holder){
        holder.actionTv.setText(context.getText(R.string.task_resume));
        holder.statusTv.setText("onTaskFail====" + e);
    }

    public static void ToTaskComplete(Context context, DownloadTask task, TaskAdapter.QueueViewHolder holder){
        holder.speedTv.setText(context.getString(R.string.task_speed_min));
        holder.actionTv.setText(context.getText(R.string.task_restart));
        holder.statusTv.setText("onTaskComplete");
        holder.progressBar.setProgress(holder.progressBar.getMax());
    }

}
