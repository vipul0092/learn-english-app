import React from 'react';
import { View, ActivityIndicator, StyleSheet, Text } from 'react-native';
import { Overlay } from 'react-native-elements';

const LoadingOverlay = ({ isVisible, text }) => {
  return (
    <Overlay isVisible={isVisible} width="auto" height="auto">
      <View style={styles.container}>
        <ActivityIndicator size="large" color="#00ff00" />
        <Text style={styles.text}>{text}</Text>
      </View>
    </Overlay>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  text: {
    fontFamily: 'UbuntuBold',
    fontSize: 15,
  },
});

export default LoadingOverlay;
