package edu.mit.media.funf.wifiscanner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.mit.media.funf.FunfManager;
import edu.mit.media.funf.json.IJsonObject;
import edu.mit.media.funf.pipeline.BasicPipeline;
import edu.mit.media.funf.probe.Probe.DataListener;
import edu.mit.media.funf.probe.builtin.AccelerometerSensorProbe;
import edu.mit.media.funf.probe.builtin.ActivityProbe;
import edu.mit.media.funf.probe.builtin.AndroidInfoProbe;
import edu.mit.media.funf.probe.builtin.ApplicationsProbe;
import edu.mit.media.funf.probe.builtin.BatteryProbe;
import edu.mit.media.funf.probe.builtin.BluetoothProbe;
import edu.mit.media.funf.probe.builtin.BrowserBookmarksProbe;
import edu.mit.media.funf.probe.builtin.BrowserSearchesProbe;
import edu.mit.media.funf.probe.builtin.CallLogProbe;
import edu.mit.media.funf.probe.builtin.ContactProbe;
import edu.mit.media.funf.probe.builtin.GravitySensorProbe;
import edu.mit.media.funf.probe.builtin.GyroscopeSensorProbe;
import edu.mit.media.funf.probe.builtin.HardwareInfoProbe;
import edu.mit.media.funf.probe.builtin.LightSensorProbe;
import edu.mit.media.funf.probe.builtin.MagneticFieldSensorProbe;
import edu.mit.media.funf.probe.builtin.OrientationSensorProbe;
import edu.mit.media.funf.probe.builtin.PressureSensorProbe;
import edu.mit.media.funf.probe.builtin.ProximitySensorProbe;
import edu.mit.media.funf.probe.builtin.RotationVectorSensorProbe;
import edu.mit.media.funf.probe.builtin.RunningApplicationsProbe;
import edu.mit.media.funf.probe.builtin.ScreenProbe;
import edu.mit.media.funf.probe.builtin.SimpleLocationProbe;
import edu.mit.media.funf.probe.builtin.TelephonyProbe;
import edu.mit.media.funf.probe.builtin.TemperatureSensorProbe;
import edu.mit.media.funf.probe.builtin.TimeOffsetProbe;
import edu.mit.media.funf.probe.builtin.WifiProbe;
import edu.mit.media.funf.storage.NameValueDatabaseHelper;

public class MainActivity extends Activity implements DataListener {

  public static final String PIPELINE_NAME = "default";
  private FunfManager funfManager;
  private BasicPipeline pipeline;
  private SimpleLocationProbe locationProbe;
  private BluetoothProbe bluetoothProbe;
  private WifiProbe wifiProbe;
//  private CellProbe cellProbe;
  private ContactProbe contactProbe;
  private CallLogProbe callLogProbe;
//  private SMSProbe sMSProbe;
  private AccelerometerSensorProbe accelerometerSensorProbe;
  private GravitySensorProbe gravitySensorProbe;
//  private LinearAccelerationProbe linearAccelerationProbe;
  private GyroscopeSensorProbe gyroscopeSensorProbe;
  private OrientationSensorProbe orientationSensorProbe;
  private RotationVectorSensorProbe rotationVectorSensorProbe;
  private ActivityProbe activityProbe;
  private LightSensorProbe lightSensorProbe;
  private ProximitySensorProbe proximitySensorProbe;
  private MagneticFieldSensorProbe magneticFieldSensorProbe;
  private PressureSensorProbe pressureSensorProbe;
  private TemperatureSensorProbe temperatureSensorProbe;
  private AndroidInfoProbe androidInfoProbe;
  private BatteryProbe batteryProbe;
  private HardwareInfoProbe hardwareInfoProbe;
  private TimeOffsetProbe timeOffsetProbe;
  private TelephonyProbe telephonyProbe;
  private RunningApplicationsProbe runningApplicationsProbe;
  private ApplicationsProbe applicationsProbe;
  private ScreenProbe screenProbe;
  private BrowserBookmarksProbe browserBookmarksProbe;
  private BrowserSearchesProbe browserSearchesProbe;
//  private VideosProbe videosProbe;
//  private AudioFilesProbe audioFilesProbe;
//  private ImagesProbe imagesProbe;
  private CheckBox enabledCheckbox;
  private Button archiveButton, scanNowButton;
  private TextView dataCountView;
  private Handler handler;
  private ServiceConnection funfManagerConn = new ServiceConnection() {    
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      funfManager = ((FunfManager.LocalBinder)service).getManager();
      
      Gson gson = funfManager.getGson();
      // Start
      locationProbe = gson.fromJson(new JsonObject(), SimpleLocationProbe.class);
      bluetoothProbe = gson.fromJson(new JsonObject(), BluetoothProbe.class);
      wifiProbe = gson.fromJson(new JsonObject(), WifiProbe.class);
//      cellProbe = gson.fromJson(new JsonObject(), CellProbe.class);
      contactProbe = gson.fromJson(new JsonObject(), ContactProbe.class);
      callLogProbe = gson.fromJson(new JsonObject(), CallLogProbe.class);
//      sMSProbe = gson.fromJson(new JsonObject(), SMSProbe.class);
      accelerometerSensorProbe = gson.fromJson(new JsonObject(), AccelerometerSensorProbe.class);
      gravitySensorProbe = gson.fromJson(new JsonObject(), GravitySensorProbe.class);
//      linearAccelerationProbe = gson.fromJson(new JsonObject(), LinearAccelerationProbe.class);
      gyroscopeSensorProbe = gson.fromJson(new JsonObject(), GyroscopeSensorProbe.class);
      orientationSensorProbe = gson.fromJson(new JsonObject(), OrientationSensorProbe.class);
      rotationVectorSensorProbe = gson.fromJson(new JsonObject(), RotationVectorSensorProbe.class);
      activityProbe = gson.fromJson(new JsonObject(), ActivityProbe.class);
      lightSensorProbe = gson.fromJson(new JsonObject(), LightSensorProbe.class);
      proximitySensorProbe = gson.fromJson(new JsonObject(), ProximitySensorProbe.class);
      magneticFieldSensorProbe = gson.fromJson(new JsonObject(), MagneticFieldSensorProbe.class);
      pressureSensorProbe = gson.fromJson(new JsonObject(), PressureSensorProbe.class);
      temperatureSensorProbe = gson.fromJson(new JsonObject(), TemperatureSensorProbe.class);
      androidInfoProbe = gson.fromJson(new JsonObject(), AndroidInfoProbe.class);
      batteryProbe = gson.fromJson(new JsonObject(), BatteryProbe.class);
      hardwareInfoProbe = gson.fromJson(new JsonObject(), HardwareInfoProbe.class);
      timeOffsetProbe = gson.fromJson(new JsonObject(), TimeOffsetProbe.class);
      telephonyProbe = gson.fromJson(new JsonObject(), TelephonyProbe.class);
      runningApplicationsProbe = gson.fromJson(new JsonObject(), RunningApplicationsProbe.class);
      applicationsProbe = gson.fromJson(new JsonObject(), ApplicationsProbe.class);
      screenProbe = gson.fromJson(new JsonObject(), ScreenProbe.class);
      browserBookmarksProbe = gson.fromJson(new JsonObject(), BrowserBookmarksProbe.class);
      browserSearchesProbe = gson.fromJson(new JsonObject(), BrowserSearchesProbe.class);
//      videosProbe = gson.fromJson(new JsonObject(), VideosProbe.class);
//      audioFilesProbe = gson.fromJson(new JsonObject(), AudioFilesProbe.class);
//      imagesProbe = gson.fromJson(new JsonObject(), ImagesProbe.class);
      // gson.fromJson(new JsonObject(), .class);

      // Start Pipeline
      pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME);
      // Register with Pipeline
      locationProbe.registerPassiveListener(MainActivity.this);
      bluetoothProbe.registerPassiveListener(MainActivity.this);
      wifiProbe.registerPassiveListener(MainActivity.this);
//      cellProbe.registerPassiveListener(MainActivity.this);
      contactProbe.registerPassiveListener(MainActivity.this);
      callLogProbe.registerPassiveListener(MainActivity.this);
//      sMSProbe.registerPassiveListener(MainActivity.this);
      accelerometerSensorProbe.registerPassiveListener(MainActivity.this);
      gravitySensorProbe.registerPassiveListener(MainActivity.this);
//      linearAccelerationProbe.registerPassiveListener(MainActivity.this);
      gyroscopeSensorProbe.registerPassiveListener(MainActivity.this);
      orientationSensorProbe.registerPassiveListener(MainActivity.this);
      rotationVectorSensorProbe.registerPassiveListener(MainActivity.this);
      activityProbe.registerPassiveListener(MainActivity.this);
      lightSensorProbe.registerPassiveListener(MainActivity.this);
      proximitySensorProbe.registerPassiveListener(MainActivity.this);
      magneticFieldSensorProbe.registerPassiveListener(MainActivity.this);
      pressureSensorProbe.registerPassiveListener(MainActivity.this);
      temperatureSensorProbe.registerPassiveListener(MainActivity.this);
      androidInfoProbe.registerPassiveListener(MainActivity.this);
      batteryProbe.registerPassiveListener(MainActivity.this);
      hardwareInfoProbe.registerPassiveListener(MainActivity.this);
      timeOffsetProbe.registerPassiveListener(MainActivity.this);
      telephonyProbe.registerPassiveListener(MainActivity.this);
      runningApplicationsProbe.registerPassiveListener(MainActivity.this);
      applicationsProbe.registerPassiveListener(MainActivity.this);
      screenProbe.registerPassiveListener(MainActivity.this);
      browserBookmarksProbe.registerPassiveListener(MainActivity.this);
      browserSearchesProbe.registerPassiveListener(MainActivity.this);
//      videosProbe.registerPassiveListener(MainActivity.this);
//      audioFilesProbe.registerPassiveListener(MainActivity.this);
//      imagesProbe.registerPassiveListener(MainActivity.this);
      // .registerPassiveListener(MainActivity.this);
      // End
      
      // This checkbox enables or disables the pipeline
      enabledCheckbox.setChecked(pipeline.isEnabled());
      enabledCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          if (funfManager != null) {
            if (isChecked) {
              funfManager.enablePipeline(PIPELINE_NAME);
              pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME);
            } else {
              funfManager.disablePipeline(PIPELINE_NAME);
            }
          }
        }
      });

      // Set UI ready to use, by enabling buttons
      updateScanCount();
      enabledCheckbox.setEnabled(true);
      archiveButton.setEnabled(true);
      scanNowButton.setEnabled(true);
    }
    
    @Override
    public void onServiceDisconnected(ComponentName name) {
      funfManager = null;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // Displays the count of rows in the data
    dataCountView = (TextView) findViewById(R.id.dataCountText);
    
    // Used to make interface changes on main thread
    handler = new Handler();
    
    enabledCheckbox = (CheckBox) findViewById(R.id.enabledCheckbox);
    enabledCheckbox.setEnabled(false);

    // Runs an archive if pipeline is enabled
    archiveButton = (Button) findViewById(R.id.archiveButton);
    archiveButton.setEnabled(false);
    archiveButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (pipeline.isEnabled()) {
          pipeline.onRun(BasicPipeline.ACTION_ARCHIVE, null);
          
          // Wait 1 second for archive to finish, then refresh the UI
          // (Note: this is kind of a hack since archiving is seamless and there are no messages when it occurs)
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              Toast.makeText(getBaseContext(), "Archived!", Toast.LENGTH_SHORT).show();
              updateScanCount();
            }
          }, 1000L);
        } else {
          Toast.makeText(getBaseContext(), "Pipeline is not enabled.", Toast.LENGTH_SHORT).show();
        }
      }
    });
    
    

    // Forces the pipeline to scan now
    scanNowButton = (Button) findViewById(R.id.scanNowButton);
    scanNowButton.setEnabled(false);
    scanNowButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (pipeline.isEnabled()) {
          // Manually register the pipeline
          locationProbe.registerListener(pipeline);
          bluetoothProbe.registerListener(pipeline);
          wifiProbe.registerListener(pipeline);
//          cellProbe.registerListener(pipeline);
          contactProbe.registerListener(pipeline);
          callLogProbe.registerListener(pipeline);
//          sMSProbe.registerListener(pipeline);
          accelerometerSensorProbe.registerListener(pipeline);
          gravitySensorProbe.registerListener(pipeline);
//          linearAccelerationProbe.registerListener(pipeline);
          gyroscopeSensorProbe.registerListener(pipeline);
          orientationSensorProbe.registerListener(pipeline);
          rotationVectorSensorProbe.registerListener(pipeline);
          activityProbe.registerListener(pipeline);
          lightSensorProbe.registerListener(pipeline);
          proximitySensorProbe.registerListener(pipeline);
          magneticFieldSensorProbe.registerListener(pipeline);
          pressureSensorProbe.registerListener(pipeline);
          temperatureSensorProbe.registerListener(pipeline);
          androidInfoProbe.registerListener(pipeline);
          batteryProbe.registerListener(pipeline);
          hardwareInfoProbe.registerListener(pipeline);
          timeOffsetProbe.registerListener(pipeline);
          telephonyProbe.registerListener(pipeline);
          runningApplicationsProbe.registerListener(pipeline);
          applicationsProbe.registerListener(pipeline);
          screenProbe.registerListener(pipeline);
          browserBookmarksProbe.registerListener(pipeline);
          browserSearchesProbe.registerListener(pipeline);
//          videosProbe.registerListener(pipeline);
//          audioFilesProbe.registerListener(pipeline);
//          imagesProbe.registerListener(pipeline);
        } else {
          Toast.makeText(getBaseContext(), "Pipeline is not enabled.", Toast.LENGTH_SHORT).show();
        }
      }
    });
    
    // Bind to the service, to create the connection with FunfManager
    bindService(new Intent(this, FunfManager.class), funfManagerConn, BIND_AUTO_CREATE);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbindService(funfManagerConn);
  }


  private static final String TOTAL_COUNT_SQL = "select count(*) from " + NameValueDatabaseHelper.DATA_TABLE.name;
  /**
   * Queries the database of the pipeline to determine how many rows of data we have recorded so far.
   */
  private void updateScanCount() {
    // Query the pipeline db for the count of rows in the data table
    SQLiteDatabase db = pipeline.getDb();
    Cursor mcursor = db.rawQuery(TOTAL_COUNT_SQL, null);
    mcursor.moveToFirst();
    final int count = mcursor.getInt(0);
    // Update interface on main thread
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        dataCountView.setText("Data Count: " + count);
      }
    });
  }

  @Override
  public void onDataReceived(IJsonObject probeConfig, IJsonObject data) {
    // Not doing anything with the data
    // As an exercise, you could display this to the screen 
    // (Remember to make UI changes on the main thread)
  }

  @Override
  public void onDataCompleted(IJsonObject probeConfig, JsonElement checkpoint) {
    updateScanCount();
    // Re-register to keep listening after probe completes.
    locationProbe.registerPassiveListener(this);
    bluetoothProbe.registerPassiveListener(this);
    wifiProbe.registerPassiveListener(this);
//    cellProbe.registerPassiveListener(this);
    contactProbe.registerPassiveListener(this);
    callLogProbe.registerPassiveListener(this);
//    sMSProbe.registerPassiveListener(this);
    accelerometerSensorProbe.registerPassiveListener(this);
    gravitySensorProbe.registerPassiveListener(this);
//    linearAccelerationProbe.registerPassiveListener(this);
    gyroscopeSensorProbe.registerPassiveListener(this);
    orientationSensorProbe.registerPassiveListener(this);
    rotationVectorSensorProbe.registerPassiveListener(this);
    activityProbe.registerPassiveListener(this);
    lightSensorProbe.registerPassiveListener(this);
    proximitySensorProbe.registerPassiveListener(this);
    magneticFieldSensorProbe.registerPassiveListener(this);
    pressureSensorProbe.registerPassiveListener(this);
    temperatureSensorProbe.registerPassiveListener(this);
    androidInfoProbe.registerPassiveListener(this);
    batteryProbe.registerPassiveListener(this);
    hardwareInfoProbe.registerPassiveListener(this);
    timeOffsetProbe.registerPassiveListener(this);
    telephonyProbe.registerPassiveListener(this);
    runningApplicationsProbe.registerPassiveListener(this);
    applicationsProbe.registerPassiveListener(this);
    screenProbe.registerPassiveListener(this);
    browserBookmarksProbe.registerPassiveListener(this);
    browserSearchesProbe.registerPassiveListener(this);
//    videosProbe.registerPassiveListener(this);
//    audioFilesProbe.registerPassiveListener(this);
//    imagesProbe.registerPassiveListener(this);
    //.registerPassiveListener(this);
  }


}
