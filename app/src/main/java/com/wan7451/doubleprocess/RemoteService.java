package com.wan7451.doubleprocess;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.core.app.NotificationCompat;

public class RemoteService extends Service {

    public static final String CHANNEL_ID_STRING = "nyd002";

    private MyBind myBind;
    private MyConnection conn;

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (myBind == null) {
            myBind = new MyBind();
        }
        conn = new MyConnection();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class),
                conn, BIND_IMPORTANT);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID_STRING, "诺秒贷", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        Notification notification = new NotificationCompat.Builder(this, "")
                .setContentTitle("前台进程")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("进程保活")
                .setContentIntent(PendingIntent.getService(this, 0, intent, 0))
                .build();
        startForeground(startId, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBind;
    }

    class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(RemoteService.this, LocalService.class));
            bindService(new Intent(RemoteService.this, LocalService.class),
                    conn, BIND_IMPORTANT);
        }
    }


    static class MyBind extends IKeepAlive.Stub {

        @Override
        public String getProcessName() throws RemoteException {
            return "RemoteProcess";
        }
    }

}
