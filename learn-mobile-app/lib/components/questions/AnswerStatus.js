import React from 'react';
import { StyleSheet, View } from 'react-native';
import { Icon, Text } from 'react-native-elements';

/**
 * Shows the status of the answer for a question according to the passed success flag
 */
export const AnswerStatus = ({ success }) => {
  return (
    <View style={styles.container}>
      <Icon
        size={50}
        name={success ? 'check-outline' : 'close-outline'}
        type="material-community"
        color={success ? 'green' : 'red'}
      />
      <Text style={success ? styles.successText : styles.failureText}>
        {success ? 'Thats Correct! Well Done!' : 'Oops! thats incorrect.'}
      </Text>
    </View>
  );
};

const textStyle = {
  fontFamily: 'UbuntuBold',
  fontSize: 20,
  fontWeight: '600',
};

const styles = StyleSheet.create({
  successText: {
    ...textStyle,
    color: 'green',
  },
  failureText: {
    ...textStyle,
    color: 'red',
  },
  container: {
    marginBottom: 40,
  },
});
