import React from 'react';
import { StyleSheet, View } from 'react-native';
import { UnscrambleQuestion } from '../../questions';

const Intermediate = () => {
  return (
    <View style={styles.container}>
      <UnscrambleQuestion />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'white',
    borderRadius: 5,
  },
});

export default Intermediate;
