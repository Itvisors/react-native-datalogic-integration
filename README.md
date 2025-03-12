# react-native-datalogic-integration

React Native DataLogic Integration

We publish this package to make it easier to integrate it into our projects.
Feel free to use it but we do not provide any support outside our projects.

## Installation

```sh
npm install react-native-datalogic-integration
```

## Usage


```js
import { startReadListener, stopReadListener, EVENT_BARCODE_VALUE_SCANNED } from 'react-native-datalogic-integration';

// Start the listener
startReadListener();

// Receive scanned value
  useEffect(() => {
    const eventEmitter = new NativeEventEmitter(NativeModules.DatalogicIntegration);
    let eventListener = eventEmitter.addListener(EVENT_BARCODE_VALUE_SCANNED, (event) => {
      console.log(event.scannedValue);
    });

    // Removes the listener once unmounted
    return () => {
      eventListener.remove();
    };
  }, []);

// Stop the listener
stopReadListener();
```

Take care to stop the listener when the app is backgrounded or closed


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
