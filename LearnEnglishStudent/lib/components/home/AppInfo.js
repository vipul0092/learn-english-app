import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Image } from 'react-native-elements';

const AppInfo = () => {
  return (
    <View style={styles.container}>
      <Image
        source={require('../../images/learn-english.jpg')}
        style={{
          width: 300,
          height: 300,
          borderRadius: 20,
          resizeMode: 'contain',
        }}
      />
      <Text style={styles.topText}>Learn With Teacher Rafa App</Text>
      <Text style={styles.bottomText}>Content by Rafaela</Text>
      <Text style={styles.bottomText}>Made by Vipul</Text>
      <Text style={styles.bottomText}>v1.0.0</Text>
    </View>
  );
};

const textStyle = {
  fontFamily: 'UbuntuBold',
  fontSize: 20,
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  topText: {
    ...textStyle,
    paddingBottom: 10,
  },
  bottomText: {
    ...textStyle,
  },
});

export default AppInfo;
