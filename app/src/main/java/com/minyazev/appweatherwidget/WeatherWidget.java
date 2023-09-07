package com.minyazev.appweatherwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class WeatherWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId:appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId);

        }
    }
    static void  updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){

        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        openIntent.setData(Uri.parse("https://openweathermap.org/city/551487"));
        PendingIntent openPendingIntent = PendingIntent.getActivity(context, 0, openIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.btnOpenWeather, openPendingIntent);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WeatherWorker.class).build();
        WorkManager.getInstance(context).enqueue(workRequest);

        appWidgetManager.updateAppWidget(appWidgetId,views);

    }
}
