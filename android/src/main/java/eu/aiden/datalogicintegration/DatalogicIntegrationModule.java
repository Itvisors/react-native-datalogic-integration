package eu.aiden.datalogicintegration;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

public class DatalogicIntegrationModule extends ReactContextBaseJavaModule {

  private static final String NAME = "DatalogicIntegration";
  private static final String EVENT_BARCODE_VALUE_SCANNED = "BarcodeValueScanned";

  private final BarcodeManager barcodeManager = new BarcodeManager();
  private ReadListener readListener = null;
  private ReactApplicationContext reactContext = null;

  public DatalogicIntegrationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("EVENT_BARCODE_VALUE_SCANNED", EVENT_BARCODE_VALUE_SCANNED);
    return constants;
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }

  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void startReadListener(@NonNull Promise promise) {
    Log.d(NAME, "startReadListener: Start");

    // From here on, we want to be notified with exceptions in case of errors.
    ErrorManager.enableExceptions(true);

    if (readListener != null) {
      barcodeManager.removeReadListener(readListener);
      readListener = null;
      Log.d(NAME, "startReadListener: Current listener stopped");
    }

    try {

      // Create an anonymous class.
      readListener = new ReadListener() {

        // Implement the callback method.
        @Override
        public void onRead(DecodeResult decodeResult) {
          // Change the displayed text to the current received result.
          String barcodeText = decodeResult.getText();
          Log.d(NAME, "Scanned: " + barcodeText);
          WritableMap params = Arguments.createMap();
          params.putString("scannedValue", barcodeText);
          sendEvent(reactContext, EVENT_BARCODE_VALUE_SCANNED, params);
        }

      };

      // Remember to add it, as a listener.
      barcodeManager.addReadListener(readListener);

    } catch (DecodeException e) {
      Log.e(NAME, "Error while trying to bind a listener to BarcodeManager", e);
    }

    Log.d(NAME, "startReadListener: Listener started");

    promise.resolve(null);
  }

  @ReactMethod
  public void stopReadListener(@NonNull Promise promise) {
    Log.d(NAME, "stopReadListener: Start");

    if (readListener != null) {
      barcodeManager.removeReadListener(readListener);
      readListener = null;
      Log.d(NAME, "stopReadListener: Listener stopped");

    } else {
      Log.d(NAME, "stopReadListener: Listener not active");

    }


    promise.resolve(null);

  }

}
