import {
  add,
  multiply,
  startReadListener,
  stopReadListener,
} from 'react-native-datalogic-integration';
import { Text, View, StyleSheet, Button } from 'react-native';
import { useState, useEffect } from 'react';

export default function App() {
  const [result, setResult] = useState<number | undefined>();
  const [sum, setSum] = useState<number | undefined>();

  useEffect(() => {
    multiply(3, 7).then(setResult);
    add(25, 30).then(setSum);
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.textStyle}>Result: {result}</Text>
      <Text style={styles.textStyle}>Sum: {sum}</Text>
      <Button
        title="Start listener"
        onPress={() => {
          startReadListener();
        }}
      />
      <Button
        title="Stop listener"
        onPress={() => {
          stopReadListener();
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'black',
  },
  textStyle: {
    color: 'white',
  },
});
