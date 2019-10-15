package com.si.uinam.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MyService extends Service implements DummyAsyncCallback {

    public static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        DummyAsync dummyAsync = new DummyAsync(this);
        dummyAsync.execute();
        return START_STICKY;
    }

    @Override
    public void onPreAsync() {
        Log.i(TAG, "onPreAsync: Pre Service");
    }

    @Override
    public void onPostAsync() {
        Log.i(TAG, "onPostAsync: Stop Service");
        stopSelf();
    }

    private static class DummyAsync extends AsyncTask<Void, Void, Void>{

        private WeakReference<DummyAsyncCallback> callback;

        DummyAsync(DummyAsyncCallback callback){
            this.callback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DummyAsyncCallback callback = this.callback.get();
            if(callback != null){
                callback.onPreAsync();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(TAG, "background");
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DummyAsyncCallback callback = this.callback.get();
            if(callback != null){
                callback.onPostAsync();
            }
        }
    }


}

interface DummyAsyncCallback{
    void onPreAsync();
    void onPostAsync();
}