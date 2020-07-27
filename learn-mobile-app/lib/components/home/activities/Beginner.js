import React from 'react';
import { View, StyleSheet } from 'react-native';
import VideoQuestion from '../../questions/VideoQuestion';

const Beginner = () => {
  return (
    <View style={styles.container}>
      <VideoQuestion text="Identify the song" videoSource="shine" />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default Beginner;
