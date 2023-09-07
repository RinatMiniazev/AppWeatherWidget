package com.minyazev.appweatherwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherWorker extends Worker {

    private static final String URL_STRING = "https://api.openweathermap.org/data/2.5/weather?q=Kazan&units=metric&appid=3142a54243e071c4224117439370a564";

  public WeatherWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
      super(context, workerParameters);
  }

    @NonNull
    @Override
    public Result doWork() {

      try{
          URL url = new URL(URL_STRING);
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
              InputStreamReader isr = new InputStreamReader(connection.getInputStream());
              BufferedReader reader = new BufferedReader(isr);
              StringBuilder result = new StringBuilder();
              String line;
              while ((line = reader.readLine()) != null) {
                  result.append(line);
              }
              reader.close();

              JSONObject jsonResponse = new JSONObject(result.toString());
              JSONObject mainObject = jsonResponse.getJSONObject("main");

              RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout);

              views.setTextViewText(R.id.tvTemperature, "Температура: " + mainObject.getDouble("temp") + " C");
              views.setTextViewText(R.id.tvHumidity, "Влажность: " + mainObject.getString("humidity") + " %");
              views.setTextViewText(R.id.tvPressure, "Давление: " + mainObject.getString("pressure") + " hPa");

              AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
              ComponentName componentName = new ComponentName(getApplicationContext(), WeatherWidget.class);
              appWidgetManager.updateAppWidget(componentName, views);

              Thread.sleep(200000);

              return Result.success();
          }else{
                return Result.failure();
          }
      } catch (Exception e ){
          e.printStackTrace();
          return Result.failure();
      }

    }
}
