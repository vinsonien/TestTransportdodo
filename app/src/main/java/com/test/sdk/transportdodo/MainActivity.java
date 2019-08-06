package com.test.sdk.transportdodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import com.arialyy.aria.core.download.DownloadEntity;
import com.test.sdk.transportdodo.adpter_download.TaskController;

import com.test.sdk.transportdodo.ui.TaskListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void IntentHelp(Class clazz){
        startActivity(new Intent(MainActivity.this,clazz));
    }


    public void creattask(View view){
        creatalltask();
    }

    public void totasklist(View view){
        IntentHelp(TaskListActivity.class);
    }

    private void creatalltask() {

        List<DownloadEntity> taskList = new ArrayList<>();
        DownloadEntity taskHistory1 = new DownloadEntity();
        String urlPath1 = "https://cdn.llscdn.com/yy/files/xs8qmxn8-lls-LLS-5.8-800-20171207-111607.apk";
        String filePath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory1.setUrl(urlPath1);
        taskHistory1.setFilePath(filePath1 + "test_1.apk");
        taskHistory1.setFileName("test_1.apk");
        taskList.add(taskHistory1);

        DownloadEntity taskHistory2 = new DownloadEntity();
        String urlPath2 = "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk";
        String filePath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory2.setUrl(urlPath2);
        taskHistory2.setFilePath(filePath2 + "test_2.apk");
        taskHistory2.setFileName("test_2.apk");
        taskList.add(taskHistory2);

        DownloadEntity taskHistory3 = new DownloadEntity();
        String urlPath3 = "https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk";
        String filePath3 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory3.setUrl(urlPath3);
        taskHistory3.setFilePath(filePath3 + "test_3.apk");
        taskHistory3.setFileName("test_3.apk");
        taskList.add(taskHistory3);

        DownloadEntity taskHistory4 = new DownloadEntity();
        String urlPath4 = "https://dldir1.qq.com/foxmail/work_weixin/WXWork_2.4.5.213.dmg";
        String filePath4 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory4.setUrl(urlPath4);
        taskHistory4.setFilePath(filePath4 + "test_4.apk");
        taskHistory4.setFileName("test_4.apk");
        taskList.add(taskHistory4);


        DownloadEntity taskHistory5 = new DownloadEntity();
        String urlPath5 = "https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.2.0.dmg";
        String filePath5 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory5.setUrl(urlPath5);
        taskHistory5.setFilePath(filePath5 + "test_5.apk");
        taskHistory5.setFileName("test_5.apk");
        taskList.add(taskHistory5);

        DownloadEntity taskHistory6 = new DownloadEntity();
        String urlPath6 = "https://zhstatic.zhihu.com/pkg/store/zhihu";
        String filePath6 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory6.setUrl(urlPath6);
        taskHistory6.setFilePath(filePath6 + "test_6.apk");
        taskHistory6.setFileName("test_6.apk");
        taskList.add(taskHistory6);

        DownloadEntity taskHistory7 = new DownloadEntity();
        String urlPath7 = "http://d1.music.126.net/dmusic/CloudMusic_official_4.3.2.468990.apk";
        String filePath7 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory7.setUrl(urlPath7);
        taskHistory7.setFilePath(filePath7 + "test_7.apk");
        taskHistory7.setFileName("test_7.apk");
        taskList.add(taskHistory7);

        DownloadEntity taskHistory8 = new DownloadEntity();
        String urlPath8 = "http://d1.music.126.net/dmusic/NeteaseMusic_1.5.9_622_officialsite.dmg";
        String filePath8 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory8.setUrl(urlPath8);
        taskHistory8.setFilePath(filePath8 + "test_8.apk");
        taskHistory8.setFileName("test_8.apk");
        taskList.add(taskHistory8);

        DownloadEntity taskHistory9 = new DownloadEntity();
        String urlPath9 = "http://dldir1.qq.com/weixin/Windows/WeChatSetup.exe";
        String filePath9 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        taskHistory9.setUrl(urlPath9);
        taskHistory9.setFilePath(filePath9 + "test_9.apk");
        taskHistory9.setFileName("test_9.apk");
        taskList.add(taskHistory9);

        final TaskController controller = new TaskController(this,taskList);
        controller.allStart();
    }
}
