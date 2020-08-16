import React from 'react';
import { View, StyleSheet } from 'react-native';
import { VideoQuestion } from '../../questions';

const Beginner = () => {
  return (
    <View style={styles.container}>
      <VideoQuestion
        text="Whats the first line of the song?"
        videoSource="shine"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
  },
});

export default Beginner;
