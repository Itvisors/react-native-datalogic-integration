package eu.aiden.datalogicintegration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;

public class DatalogicIntegrationModule extends ReactContextBaseJavaModule {

  private static final String NAME = "DatalogicIntegration";

  private BarcodeManager barcodeManager = new BarcodeManager();
  private ReadListener readListener = null;

  public DatalogicIntegrationModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, @NonNull Promise promise) {
    promise.resolve(a * b);
  }

  @ReactMethod
  public void add(double a, double b, @NonNull Promise promise) {
    promise.resolve(a + b);
  }

  @ReactMethod
  public void startReadListener(@NonNull Promise promise) {
    Log.i(NAME, "startReadListener: Start");

    // From here on, we want to be notified with exceptions in case of errors.
    ErrorManager.enableExceptions(true);

    if (readListener != null) {
      barcodeManager.removeReadListener(readListener);
      readListener = null;
      Log.i(NAME, "startReadListener: Current listener stopped");
    }
    
    try {

      // Create an anonymous class.
      readListener = new ReadListener() {

        // Implement the callback method.
        @Override
        public void onRead(DecodeResult decodeResult) {
          // Change the displayed text to the current received result.
          String barcodeText = decodeResult.getText();
          Log.i(NAME, "Scanned: " + barcodeText);
        }

      };

      // Remember to add it, as a listener.
      barcodeManager.addReadListener(readListener);

    } catch (DecodeException e) {
      Log.e(NAME, "Error while trying to bind a listener to BarcodeManager", e);
    }

    Log.i(NAME, "startReadListener: Listener started");

    promise.resolve(null);
  }

  @ReactMethod
  public void stopReadListener(@NonNull Promise promise) {
    Log.i(NAME, "stopReadListener: Start");

    if (readListener != null) {
      barcodeManager.removeReadListener(readListener);
      readListener = null;
      Log.i(NAME, "stopReadListener: Listener stopped");

    } else {
      Log.i(NAME, "stopReadListener: Listener not active");

    }


    promise.resolve(null);

  }

}
