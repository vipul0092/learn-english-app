import React from 'react';
import { View, StyleSheet } from 'react-native';
import { SingleChoiceQuestion } from '../../questions/SingleChoiceQuestion';

const Expert = () => {
  return (
    <View style={styles.container}>
      <SingleChoiceQuestion />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
});

export default Expert;
