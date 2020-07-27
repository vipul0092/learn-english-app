import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Image } from 'react-native-elements';
import { learnEnglish } from '../assets/images';

const SplashScreen = () => {
  return (
    <View style={styles.container}>
      <Image
        fadeDuration={0}
        source={learnEnglish}
        style={{
          width: 300,
          height: 300,
          borderRadius: 10,
          resizeMode: 'contain',
        }}
      />
      <Text style={styles.text}>Welcome to Learning English With Rafa!</Text>
      <Text style={styles.text}>Loading App...</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontFamily: 'UbuntuBold',
    fontSize: 18,
  },
});

export default SplashScreen;
