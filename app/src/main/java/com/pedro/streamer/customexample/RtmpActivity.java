/*
 * Copyright (C) 2023 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pedro.streamer.customexample;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.pedro.common.ConnectChecker;
import com.pedro.encoder.input.gl.render.filters.AndroidViewFilterRender;
import com.pedro.encoder.input.gl.render.filters.SnowFilterRender;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.library.generic.GenericCamera1;
import com.pedro.library.view.OpenGlView;
import com.pedro.streamer.MainActivity;
import com.pedro.streamer.R;
import com.pedro.streamer.data.local.SessionManager;
import com.pedro.streamer.data.remote.MyResource;
import com.pedro.streamer.data.remote.Retro;
import com.pedro.streamer.utils.PathUtils;
import com.pedro.streamer.utils.Utility;
import com.pedro.streamer.utils.constants.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;

/**
 * More documentation see:
 * {@link com.pedro.library.base.Camera1Base}
 * {@link com.pedro.library.rtmp.RtmpCamera1}
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RtmpActivity extends AppCompatActivity
    implements Button.OnClickListener, ConnectChecker, SurfaceHolder.Callback,
    View.OnTouchListener {

  private Integer[] orientations = new Integer[] { 0, 90, 180, 270 };

  private GenericCamera1 rtmpCamera1;

  private OpenGlView openGlView;
  private Button bStartStop, bRecord;
  private EditText etUrl;
  private String currentDateAndTime = "";
  private File folder;
  //options menu
  private DrawerLayout drawerLayout;
  private NavigationView navigationView;
  private ActionBarDrawerToggle actionBarDrawerToggle;
  private RadioGroup rgChannel;
  private Spinner spResolution;
  private CheckBox cbEchoCanceler, cbNoiseSuppressor;
  private EditText etVideoBitrate, etFps, etAudioBitrate, etSampleRate, etWowzaUser,
      etWowzaPassword;
  private String lastVideoBitrate,scoreCardUrl="",strLiveUrl="";
  private TextView tvBitrate;

  ImageView iv_back;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    setContentView(R.layout.activity_custom);
    folder = PathUtils.getRecordPath();
    Log.e("TAG","RtmpActivity");

    if(getSupportActionBar()!=null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);
    }

    try {
    String strScore = getIntent().getExtras().getString("scoreCardUrl");
      Log.e("TAG strScore",""+strScore);
      if(strScore!=null)
    {
      scoreCardUrl=strScore;
    }
      Log.e("TAG scoreCardUrl",""+scoreCardUrl);
    }
    catch (Exception exp){

    }
    openGlView = findViewById(R.id.surfaceView);
    openGlView.getHolder().addCallback(this);
     openGlView.setOnTouchListener(this);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      rtmpCamera1 = new GenericCamera1(openGlView, this);
    }
    prepareOptionsMenuViews();
    tvBitrate = findViewById(R.id.tv_bitrate);
    etUrl = findViewById(R.id.et_rtp_url);
    etUrl.setHint(R.string.hint_rtmp);
    bStartStop = findViewById(R.id.b_start_stop);
    bStartStop.setOnClickListener(this);
    bRecord = findViewById(R.id.b_record);
    bRecord.setOnClickListener(this);

    iv_back = findViewById(R.id.iv_back);
    iv_back.setOnClickListener(this);

    Button switchCamera = findViewById(R.id.switch_camera);
    switchCamera.setOnClickListener(this);




  }

  private void prepareOptionsMenuViews() {
    drawerLayout = findViewById(R.id.activity_custom);
    navigationView = findViewById(R.id.nv_rtp);

    navigationView.inflateMenu(R.menu.options_rtmp);
    actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.rtmp_streamer,
        R.string.rtmp_streamer) {

      public void onDrawerOpened(View drawerView) {
        actionBarDrawerToggle.syncState();
        lastVideoBitrate = etVideoBitrate.getText().toString();
      }

      public void onDrawerClosed(View view) {
        actionBarDrawerToggle.syncState();
        if (lastVideoBitrate != null && !lastVideoBitrate.equals(
            etVideoBitrate.getText().toString()) && rtmpCamera1.isStreaming()) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int bitrate = Integer.parseInt(etVideoBitrate.getText().toString()) * 1024;
            rtmpCamera1.setVideoBitrateOnFly(bitrate);
            Toast.makeText(RtmpActivity.this, "New bitrate: " + bitrate, Toast.LENGTH_SHORT).
                show();
          } else {
            Toast.makeText(RtmpActivity.this, "Bitrate on fly ignored, Required min API 19",
                Toast.LENGTH_SHORT).show();
          }
        }
      }
    };
    drawerLayout.addDrawerListener(actionBarDrawerToggle);
    //checkboxs
    cbEchoCanceler =
        (CheckBox) navigationView.getMenu().findItem(R.id.cb_echo_canceler).getActionView();
    cbNoiseSuppressor =
        (CheckBox) navigationView.getMenu().findItem(R.id.cb_noise_suppressor).getActionView();
    //radiobuttons
    RadioButton rbTcp =
        (RadioButton) navigationView.getMenu().findItem(R.id.rb_tcp).getActionView();
    rgChannel = (RadioGroup) navigationView.getMenu().findItem(R.id.channel).getActionView();
    rbTcp.setChecked(true);
    //spinners
    spResolution = (Spinner) navigationView.getMenu().findItem(R.id.sp_resolution).getActionView();

    ArrayAdapter<Integer> orientationAdapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
    orientationAdapter.addAll(orientations);

    ArrayAdapter<String> resolutionAdapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
    List<String> list = new ArrayList<>();
    for (Camera.Size size : rtmpCamera1.getResolutionsBack()) {
      list.add(size.width + "X" + size.height);
    }
    resolutionAdapter.addAll(list);
    spResolution.setAdapter(resolutionAdapter);
    //edittexts
    etVideoBitrate =
        (EditText) navigationView.getMenu().findItem(R.id.et_video_bitrate).getActionView();
    etFps = (EditText) navigationView.getMenu().findItem(R.id.et_fps).getActionView();
    etAudioBitrate =
        (EditText) navigationView.getMenu().findItem(R.id.et_audio_bitrate).getActionView();
    etSampleRate = (EditText) navigationView.getMenu().findItem(R.id.et_samplerate).getActionView();
    etVideoBitrate.setText("2500");
    etFps.setText("30");
    etAudioBitrate.setText("128");
    etSampleRate.setText("44100");
    etWowzaUser = (EditText) navigationView.getMenu().findItem(R.id.et_user).getActionView();
    etWowzaPassword =
        (EditText) navigationView.getMenu().findItem(R.id.et_password).getActionView();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    actionBarDrawerToggle.syncState();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    getMenuInflater().inflate(R.menu.gl_webview, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int itemId = item.getItemId();
    if (itemId == android.R.id.home) {
      if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
        drawerLayout.openDrawer(GravityCompat.START);
      } else {
        drawerLayout.closeDrawer(GravityCompat.START);
      }
      return true;
    } else if (itemId == R.id.microphone) {
      if (!rtmpCamera1.isAudioMuted()) {
        item.setIcon(getResources().getDrawable(R.drawable.icon_microphone_off));
        rtmpCamera1.disableAudio();
      } else {
        item.setIcon(getResources().getDrawable(R.drawable.icon_microphone));
        rtmpCamera1.enableAudio();
      }
      return true;
    }
    else if (itemId == R.id.android_view) {
      rtmpCamera1.getGlInterface().setFilter(new SnowFilterRender());
      return true;
    }

    else if (itemId == R.id.web_view) {


      if(scoreCardUrl.equals("")||scoreCardUrl.equals(null) || scoreCardUrl.equals("null"))
      {

        Utility.toast(this,"Your score card url is not valid, Go back and put a valid url");
      }
      else {

//            Activity activity = this;
        View view = LayoutInflater.from(this).inflate(R.layout.layout_web_view, null);
        WebView webView = (WebView) view.findViewById(R.id.webView);
//            progDailog = ProgressDialog.show(activity, "Loading","Please wait...", true);
//            progDailog.setCancelable(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(10);
//            webView.canZoomIn();
//            webView.canZoomOut();
        webView.setBackgroundColor(
                Color.TRANSPARENT
        );
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    Log.e("web_shouldOverrideUrlLoading",""+url);
//                   progDailog.show();
            view.loadUrl(url);

            return true;
          }

          @Override
          public void onPageFinished(WebView view, final String url) {
            Log.e("web_onpagefinished", "" + url);
//                  progDailog.dismiss();
          }
        });

        webView.loadUrl(scoreCardUrl);

        View root = findViewById(R.id.activity_custom);
        int sizeSpecWidth = View.MeasureSpec.makeMeasureSpec(root.getWidth(), View.MeasureSpec.EXACTLY);
        int sizeSpecHeight = View.MeasureSpec.makeMeasureSpec(root.getHeight(), View.MeasureSpec.EXACTLY);

        //Set view size to allow rendering
        webView.measure(sizeSpecWidth, sizeSpecHeight);
        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());

        AndroidViewFilterRender androidViewFilterRender = new AndroidViewFilterRender();
        androidViewFilterRender.setView(webView);
        rtmpCamera1.getGlInterface().setFilter(androidViewFilterRender);

        return true;
      }
    }

    return false;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.b_start_stop) {
      Log.d("TAG_R", "b_start_stop: ");
      if (!rtmpCamera1.isStreaming()) {
        bStartStop.setText(getResources().getString(R.string.stop_button));
        String user = etWowzaUser.getText().toString();
        String password = etWowzaPassword.getText().toString();
        if (!user.isEmpty() && !password.isEmpty()) {
          rtmpCamera1.getStreamClient().setAuthorization(user, password);
        }
        if (rtmpCamera1.isRecording() || prepareEncoders()) {
          rtmpCamera1.startStream(etUrl.getText().toString());
        } else {
          //If you see this all time when you start stream,
          //it is because your encoder device dont support the configuration
          //in video encoder maybe color format.
          //If you have more encoder go to VideoEncoder or AudioEncoder class,
          //change encoder and try
          Toast.makeText(this, "Error preparing stream, This device cant do it",
                  Toast.LENGTH_SHORT).show();
          bStartStop.setText(getResources().getString(R.string.start_button));
        }

       callApiWithDelay();

     //   http://192.168.1.2:8001/api/stream/stream-url
      } else {
        bStartStop.setText(getResources().getString(R.string.start_button));
        rtmpCamera1.stopStream();
      }
    } else if (id == R.id.b_record) {
      Log.d("TAG_R", "b_start_stop: ");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
        if (!rtmpCamera1.isRecording()) {
          try {
            if (!folder.exists()) {
              folder.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            currentDateAndTime = sdf.format(new Date());
            if (!rtmpCamera1.isStreaming()) {
              if (prepareEncoders()) {
                rtmpCamera1.startRecord(
                        folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                bRecord.setText(R.string.stop_record);
                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
              } else {
                Toast.makeText(this, "Error preparing stream, This device cant do it",
                        Toast.LENGTH_SHORT).show();
              }
            } else {
              rtmpCamera1.startRecord(
                      folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
              bRecord.setText(R.string.stop_record);
              Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show();
            }
          } catch (IOException e) {
            rtmpCamera1.stopRecord();
            PathUtils.updateGallery(this, folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
            bRecord.setText(R.string.start_record);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        } else {
          rtmpCamera1.stopRecord();
          PathUtils.updateGallery(this, folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
          bRecord.setText(R.string.start_record);
          Toast.makeText(this,
                  "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
                  Toast.LENGTH_SHORT).show();
          currentDateAndTime = "";
        }
      } else {
        Toast.makeText(this, "You need min JELLY_BEAN_MR2(API 18) for do it...",
                Toast.LENGTH_SHORT).show();
      }
    } else if (id == R.id.switch_camera) {
      try {
        rtmpCamera1.switchCamera();
      } catch (final CameraOpenException e) {
        Toast.makeText(RtmpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    }

    else if (id == R.id.iv_back) {

      try {
        if (rtmpCamera1 != null)
          rtmpCamera1.stopStream();
      }
      catch (Exception excep)
      {

      }

      try {
     Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
       } catch ( Exception e) {
        Toast.makeText(RtmpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void callApiWithDelay() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        callGetLiveVdoUrl(RtmpActivity.this);
      }
    },5000);
  }

  public void callGetLiveVdoUrl(Context context) {


    Retro.networkCalls(new Dialog(context),Retro.service(context).getActiveLiveVideo(), new MyResource() {
      @Override
      public void isSuccess(String res) {
        try {
          JSONObject jsonObject1 = new JSONObject(res);
          String strMsg=jsonObject1.getString("message");
          Log.e("message of liveVdo",  strMsg  );


          if (jsonObject1.getInt(AppConstants.STATUS) == 200) {
            JSONObject data = jsonObject1.getJSONObject("data");
            Log.e("TAG", "callGetLiveVdoUrl data: " + data);
                strLiveUrl=data.get("url").toString();


            /*{
    "status": 200,
    "message": "Streaming url fetched successfully",
    "data": {
        "url": "https://www.youtube.com/watch?v=rGFLWzz5Nw4"
    },
    "timestamp": "2024-03-01T14:18:54.844Z"
}*/

          }else   {
            Utility.toast(context, strMsg);
          }
        } catch (Exception e) {
          Log.e("Exception message of liveVdo",  e.toString());

                   }

      }

      @Override
      public void isError(String err, int code) {
        Utility.toast(context, err);
      }
    });



  }

  private boolean prepareEncoders() {
    Camera.Size resolution =
        rtmpCamera1.getResolutionsBack().get(spResolution.getSelectedItemPosition());
    int width = resolution.width;
    int height = resolution.height;
    return rtmpCamera1.prepareVideo(width, height, Integer.parseInt(etFps.getText().toString()),
        Integer.parseInt(etVideoBitrate.getText().toString()) * 1024,
        CameraHelper.getCameraOrientation(this)) && rtmpCamera1.prepareAudio(
        Integer.parseInt(etAudioBitrate.getText().toString()) * 1024,
        Integer.parseInt(etSampleRate.getText().toString()),
        rgChannel.getCheckedRadioButtonId() == R.id.rb_stereo, cbEchoCanceler.isChecked(),
        cbNoiseSuppressor.isChecked());
  }

  @Override
  public void onConnectionStarted(@NonNull String url) {
  }

  @Override
  public void onConnectionSuccess() {
    Toast.makeText(RtmpActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onConnectionFailed(@NonNull final String reason) {
    Toast.makeText(RtmpActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
        .show();
    rtmpCamera1.stopStream();
    bStartStop.setText(getResources().getString(R.string.start_button));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        && rtmpCamera1.isRecording()) {
      rtmpCamera1.stopRecord();
      PathUtils.updateGallery(getApplicationContext(), folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
      bRecord.setText(R.string.start_record);
      Toast.makeText(RtmpActivity.this,
          "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
          Toast.LENGTH_SHORT).show();
      currentDateAndTime = "";
    }
  }

  @Override
  public void onNewBitrate(final long bitrate) {
    tvBitrate.setText(bitrate + " bps");
  }

  @Override
  public void onDisconnect() {
    Toast.makeText(RtmpActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
        && rtmpCamera1.isRecording()) {
      rtmpCamera1.stopRecord();
      PathUtils.updateGallery(getApplicationContext(), folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
      bRecord.setText(R.string.start_record);
      Toast.makeText(RtmpActivity.this,
          "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
          Toast.LENGTH_SHORT).show();
      currentDateAndTime = "";
    }
  }

  @Override
  public void onAuthError() {
    Toast.makeText(RtmpActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onAuthSuccess() {
    Toast.makeText(RtmpActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    drawerLayout.openDrawer(GravityCompat.START);
  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    rtmpCamera1.startPreview();
    // optionally:
    //rtmpCamera1.startPreview(CameraHelper.Facing.BACK);
    //or
    //rtmpCamera1.startPreview(CameraHelper.Facing.FRONT);
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rtmpCamera1.isRecording()) {
      rtmpCamera1.stopRecord();
      PathUtils.updateGallery(this, folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
      bRecord.setText(R.string.start_record);
      Toast.makeText(this,
          "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath(),
          Toast.LENGTH_SHORT).show();
      currentDateAndTime = "";
    }
    if (rtmpCamera1.isStreaming()) {
      rtmpCamera1.stopStream();
      bStartStop.setText(getResources().getString(R.string.start_button));
    }
    rtmpCamera1.stopPreview();
  }

  @Override
  public boolean onTouch(View view, MotionEvent motionEvent) {
    int action = motionEvent.getAction();
    if (motionEvent.getPointerCount() > 1) {
      if (action == MotionEvent.ACTION_MOVE) {
        rtmpCamera1.setZoom(motionEvent);
      }
    } else if (action == MotionEvent.ACTION_DOWN) {
      rtmpCamera1.tapToFocus(view, motionEvent);
    }
    return true;
  }




}
