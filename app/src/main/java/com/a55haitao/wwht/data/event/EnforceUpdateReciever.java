package com.a55haitao.wwht.data.event;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.a55haitao.wwht.data.net.ActivityCollector;

import java.io.File;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 更新APP
 */
public class EnforceUpdateReciever extends BroadcastReceiver {

    private String mFile;

    public EnforceUpdateReciever(Context context, String fileName) {
        this.mFile = fileName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        File file = new File(context.getExternalFilesDir(null).getAbsolutePath() + "/" + mFile);
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE) && file.exists() && file.length() > 0) {
            install(context, file.getAbsolutePath());
        }
    }

    public boolean install(Context var1, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uriForFile = FileProvider.getUriForFile(context, var1.getApplicationContext().getPackageName() + ".fileprovider", file);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            } else {
                i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            }
            var1.startActivity(i);
//            ActivityCollector.finishAll();
            return true;
        }
        return false;
    }

    public String getFileName() {
        //        return mFile.substring(mFile.lastIndexOf("/") + 1);
        return mFile;
    }
}
