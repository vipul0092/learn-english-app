import React from 'react';
import { ScrollView, Text, StyleSheet } from 'react-native';
import { Image } from 'react-native-elements';
import { learnEnglish } from '../../assets/images';

const AppInfo = () => {
  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Image
        source={learnEnglish}
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
    </ScrollView>
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
