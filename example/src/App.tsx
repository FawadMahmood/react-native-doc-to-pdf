import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity, Image } from 'react-native';
import { generateThumbnail } from 'react-native-doc-to-pdf';
import DocumentPicker from 'react-native-document-picker';

export default function App() {
  const [uri, setImage] = React.useState('');
  // const [result, setResult] = React.useState<number | undefined>();
  const onPickDocument = React.useCallback(() => {
    DocumentPicker.pick({
      allowMultiSelection: false,
      copyTo: 'documentDirectory',
    }).then((res) => {
      console.log(res[0]);
      generateThumbnail(
        res[0]?.fileCopyUri as string,
        res[0]?.type as string
      ).then((res) => {
        setImage('file://' + res);
        console.log(res);
      });
    });
  }, []);

  return (
    <View style={styles.container}>
      {/* <Text>Result: {result}</Text> */}
      {uri && (
        <Image
          resizeMode="contain"
          style={{ width: 300, height: 600 }}
          source={{ uri }}
        />
      )}
      <TouchableOpacity onPress={onPickDocument} style={styles.btnPick}>
        <Text>PICK DOCUMENT</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  btnPick: {
    width: '80%',
    height: 40,
    backgroundColor: 'red',
    justifyContent: 'center',
    alignItems: 'center',
  },
});
