import { startReadListener, stopReadListener, EVENT_BARCODE_VALUE_SCANNED } from 'react-native-datalogic-integration';
import { Text, View, StyleSheet, Button, NativeEventEmitter, NativeModules } from 'react-native';
import { useEffect, useState } from 'react';

export default function App() {
  const [listenerActive, setListenerActive] = useState<boolean>(false);
  const [scannedValue, setScannedValue] = useState<string | undefined>();

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(NativeModules.DatalogicIntegration);
    let eventListener = eventEmitter.addListener(EVENT_BARCODE_VALUE_SCANNED, (event) => {
      setScannedValue(event.scannedValue);
    });

    // Removes the listener once unmounted
    return () => {
      eventListener.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.textStyle}>Listener active: {listenerActive ? 'yes' : 'no'}</Text>
      <Text style={styles.textStyle}>Scanned value: {scannedValue}</Text>
      <View style={styles.buttonContainer}>
        <Button
          title="Start listener"
          onPress={() => {
            setScannedValue('');
            startReadListener();
            setListenerActive(true);
          }}
        />
        <Button
          title="Stop listener"
          onPress={() => {
            stopReadListener();
            setListenerActive(false);
          }}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: 50,
    paddingHorizontal: 10,
    backgroundColor: 'black',
  },
  buttonContainer: {
    marginTop: 20,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  textStyle: {
    color: 'white',
  },
});
