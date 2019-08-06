package com.test.sdk.transportdodo.adpter_download;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import com.arialyy.aria.core.download.DownloadEntity;


import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskController {

    private final static int STARTMSG = 1;
    private final static int STOPMSG = 2;
    private final static int CANCELMSG = 3;
    private final static int DELETEMSG = 4;
    private final static int DELAYEDTIME = 600;//aria 有连击限制 500ms内 不会立刻触发开始等操作,因此用户点击全部开始时 开发者不能瞬间开始所有任务

    private Context context;
    private List<DownloadEntity> taskList = new ArrayList<>();
    TaskListener queueTask = new TaskListener();

    public TaskController(Context context, List<DownloadEntity> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void bind(final TaskAdapter.QueueViewHolder holder, final int position) {

        queueTask.bind(context, taskList.get(position), holder);
        final DownloadEntity taskHistory = taskList.get(position);
        queueTask.Pre(taskHistory);//初始化

    }

    int size() {
        return taskList.size();
    }


    public void allCancel() {
        if (queueTask != null) {
            queueTask.CancelAll();
        }
    }

    public void allStop() {
        if (queueTask != null) {
            queueTask.StopAll();
        }
    }

    public void allResume() {
        if (queueTask != null) {
            queueTask.ResumeAll();
        }
    }

    /**
     * aria 有连击限制 500ms内 不会立刻触发开始操作,因此用户点击全部开始时 开发者不能瞬间开始所有任务
     */
    public void allStart() {
        if (queueTask != null) {
            for (int i = 0; i < taskList.size(); i++) {
                SendMessageWithTag(STARTMSG, DELAYEDTIME * i, taskList.get(i));
            }
        }
    }

    private void SendMessageWithTag(int tag, int delayedTime, DownloadEntity taskHistory) {
        Message msg = new Message();
        msg.obj = taskHistory;
        msg.what = tag;
        handler.sendMessageDelayed(msg, delayedTime);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            DownloadEntity taskHistory = (DownloadEntity) msg.obj;
            switch (msg.what) {
                case STARTMSG:
                    queueTask.Start(taskHistory);
                    break;
                case STOPMSG:
                    queueTask.Stop(taskHistory);
                    break;
                case CANCELMSG:
                    queueTask.Stop(taskHistory);
                    queueTask.Cancel(taskHistory);
                    break;
                case DELETEMSG:
                    break;
            }
        }

    };


    public void unRegister() {
        if (queueTask != null) queueTask.UnRegister();
    }

}
