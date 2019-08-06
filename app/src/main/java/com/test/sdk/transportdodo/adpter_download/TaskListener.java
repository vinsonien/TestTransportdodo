package com.test.sdk.transportdodo.adpter_download;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadEntity;
import com.arialyy.aria.core.download.DownloadTask;
import com.arialyy.aria.core.inf.IHttpFileLenAdapter;
import com.test.sdk.transportdodo.R;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.test.sdk.transportdodo.adpter_download.TaskStatusManager.*;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskListener {


    private static final String TAG = "TaskListener";

    private Context context;
    Map<String, TaskAdapter.QueueViewHolder> holderMap  = new HashMap<>();
    Map<String,Observable> observableMap = new HashMap<>();//发布者
    Map<String,Observer> observerMap = new HashMap<>();//订阅者

    void bind(Context context, DownloadEntity taskHistory, TaskAdapter.QueueViewHolder holder){
        this.context = context;
        final int sizeHold = holderMap.size();
        for (int i = 0; i < sizeHold; i++) {
            String urlKey = taskHistory.getUrl();
            if (holderMap.get(urlKey) == holder) {
                holderMap.remove(urlKey);
                break;
            }
        }
        holderMap.put(taskHistory.getUrl(), holder);
        init();

    }

    private void init() {
        Aria.download(this).register();
    }

    private void initObserver(final DownloadEntity taskHistory){
        final String urlKey = taskHistory.getUrl();
        if (observerMap.containsKey(urlKey)){
            observableMap.get(urlKey).subscribe(observerMap.get(urlKey));
        }
    }

    private void initView(final DownloadEntity taskHistory){
        int percent = Aria.download(this).load(taskHistory.getUrl()).getPercent();

        holderMap.get(taskHistory.getUrl()).nameTv.setText(taskHistory.getFileName());
        holderMap.get(taskHistory.getUrl()).progressBar.setProgress(percent);
        holderMap.get(taskHistory.getUrl()).speedTv.setText(context.getString(R.string.task_speed_min));
        if (IsRunning(taskHistory)) {
            holderMap.get(taskHistory.getUrl()).actionTv.setText(context.getString(R.string.task_stop));
        }else{
            int MIN = holderMap.get(taskHistory.getUrl()).progressBar.getMin();
            int MAX = holderMap.get(taskHistory.getUrl()).progressBar.getMax();

            if (percent==MIN){
                holderMap.get(taskHistory.getUrl()).actionTv.setText(context.getString(R.string.task_start));
            }else if(percent==MAX){
                holderMap.get(taskHistory.getUrl()).actionTv.setText(context.getString(R.string.task_restart));
            }else{
                holderMap.get(taskHistory.getUrl()).actionTv.setText(context.getString(R.string.task_resume));
            }
        }

    }
    private void initListener(final DownloadEntity taskHistory){

        holderMap.get(taskHistory.getUrl()).actionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsRunning(taskHistory)) {
                    Stop(taskHistory);
                }else{
                    Start(taskHistory);
                }

            }
        });
    }

    /**
     *
     * @param taskHistory
     * @return true: onTaskStart onTaskRunning
     */
    boolean IsRunning(DownloadEntity taskHistory){
        return Aria.download(this).load(taskHistory.getUrl()).isRunning();
    }

    /**
     *
     * @param taskHistory
     * @return
     * -1	未知状态
     * 0	失败 onPre
     * 1	成功 onTaskComplete
     * 2	停止 onTaskStop
     * 3	等待 onWait onTaskFail onTaskCancel
     * 4	执行中 onTaskStart onTaskRunning
     * 5	预处理 onPre
     * 6	预处理完成
     * 7	删除任务
     */
    int GetTaskStatus(DownloadEntity taskHistory){
        return Aria.download(this).load(taskHistory.getUrl()).getTaskState();
    }

    int GetTaskStatus(String urlKey){
        return Aria.download(this).load(urlKey).getTaskState();
    }

    void Start(DownloadEntity taskHistory){

        Aria.download(this)
                .load(taskHistory.getUrl())
                //.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                //.addHeader("Accept-Encoding", "gzip, deflate")
                //.addHeader("DNT", "1")
                //.addHeader("Cookie", "BAIDUID=648E5FF020CC69E8DD6F492D1068AAA9:FG=1; BIDUPSID=648E5FF020CC69E8DD6F492D1068AAA9; PSTM=1519099573; BD_UPN=12314753; locale=zh; BDSVRTM=0")
                .useServerFileName(true)
                .setFilePath(taskHistory.getFilePath(), true)
                .setFileLenAdapter(new IHttpFileLenAdapter() {
                    @Override public long handleFileLen(Map<String, List<String>> headers) {

                        List<String> sLength = headers.get("Content-Length");
                        if (sLength == null || sLength.isEmpty()) {
                            return -1;
                        }
                        String temp = sLength.get(0);

                        return Long.parseLong(temp);
                    }
                })
                .start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void Pre(final DownloadEntity taskHistory){
        initView(taskHistory);
        initListener(taskHistory);
        initObserver(taskHistory);
    }

    /**
     * 删除任务
     *
     * @param  {@code true} 不仅删除任务数据库记录，还会删除已经删除完成的文件
     * {@code false}如果任务已经完成，只删除任务数据库记录，
     */
    void Cancel(DownloadEntity taskHistory){
        Aria.download(this).load(taskHistory.getUrl()).cancel(true);
    }

    void CancelAll(){
        Aria.download(this).removeAllTask(true);
    }

    void Stop(DownloadEntity taskHistory){
        Aria.download(this).load(taskHistory.getUrl()).stop();
    }

    void StopAll(){
        Aria.download(this).stopAllTask();
    }

    void ResumeAll(){
        Aria.download(this).resumeAllTask();
    }
    void UnRegister() {
        Aria.download(this).unRegister();
    }

    void addObervable(final DownloadTask task,final Exception e,final @TaskStatusManager.StatusMode int statusMode){
        Observable<DownloadTask> observable = Observable.create(new ObservableOnSubscribe<DownloadTask>() {
            @Override
            public void subscribe(ObservableEmitter<DownloadTask> emitter) throws Exception {
                emitter.onNext(task);
            }
        });
        observableMap.put(task.getKey(),observable);

        Observer<DownloadTask> observer = new Observer<DownloadTask>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DownloadTask task) {
                switch (statusMode){
                    case WAIT:
                        TaskStatusManager.ToWait(context,task,holderMap.get(task.getKey()));
                        break;
                    case PRE:
                        TaskStatusManager.ToPre(context,task,holderMap.get(task.getKey()));
                        break;
                    case START:
                        TaskStatusManager.ToTaskStart(context,task,holderMap.get(task.getKey()));
                        break;
                    case RUNNING:
                        TaskStatusManager.ToTaskRunning(context,task,holderMap.get(task.getKey()));
                        break;
                    case RESUME:
                        TaskStatusManager.ToTaskResume(context,task,holderMap.get(task.getKey()));
                        break;
                    case STOP:
                        TaskStatusManager.ToStop(context,task,holderMap.get(task.getKey()));
                        break;
                    case CANCEL:
                        TaskStatusManager.ToTaskCancel(context,task,holderMap.get(task.getKey()));
                        break;
                    case FAIL:
                        TaskStatusManager.ToTaskFail(context,task,e,holderMap.get(task.getKey()));
                        break;
                    case COMPLETE:
                        TaskStatusManager.ToTaskComplete(context,task,holderMap.get(task.getKey()));
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observerMap.put(task.getKey(),observer);
    }

    @Download.onWait
    void onWait(final DownloadTask task) {
        final TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, WAIT);
        }else{
            TaskStatusManager.ToWait(context,task,holder);
        }
    }

    @Download.onPre
    protected void onPre(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, PRE);
        }else{
            TaskStatusManager.ToPre(context,task,holder);
        }
    }

    @Download.onTaskStart
    void taskStart(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, START);
        }else{
            TaskStatusManager.ToTaskStart(context,task,holder);
        }
    }

    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, RUNNING);
        }else{
            TaskStatusManager.ToTaskRunning(context,task,holder);
        }
    }

    @Download.onTaskResume
    void taskResume(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, RESUME);
        }else{
            TaskStatusManager.ToTaskResume(context,task,holder);
        }
    }

    @Download.onTaskStop
    void taskStop(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, STOP);
        }else {
            TaskStatusManager.ToStop(context,task,holder);
        }
    }

    @Download.onTaskCancel
    void taskCancel(DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, CANCEL);
        }else{
            TaskStatusManager.ToTaskCancel(context,task,holder);
        }
    }

    @Download.onTaskFail
    void taskFail(final DownloadTask task, final Exception e) {
        final TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,e, FAIL);
        }else{
            TaskStatusManager.ToTaskFail(context,task,e,holder);
        }
    }

    @Download.onTaskComplete
    void taskComplete(final DownloadTask task) {
        TaskAdapter.QueueViewHolder holder = holderMap.get(task.getKey());
        if (holder==null){
            addObervable(task,null, COMPLETE);
        }else{
            TaskStatusManager.ToTaskComplete(context,task,holder);
        }
    }
}
