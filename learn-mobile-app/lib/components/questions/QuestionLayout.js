import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { View, StyleSheet, ScrollView } from 'react-native';
import { Button, Text, Divider } from 'react-native-elements';
import { QUESTION_DESCRIPTIONS } from './questionTypes';
import { AnswerStatus } from './AnswerStatus';

/**
 * Basic question layout component for all questions
 */
export const QuestionLayout = ({
  questionType,
  questionText,
  children,
  verifyAnswer,
  showStatus,
}) => {
  const [sucess, setSuccess] = useState(false);
  const verify = () => {
    setSuccess(verifyAnswer());
  };
  return (
    <ScrollView>
      <View style={{ marginTop: 15, marginBottom: 15 }}>
        <Text style={styles.heading}>
          {QUESTION_DESCRIPTIONS[questionType]}
        </Text>
      </View>
      <Divider style={{ height: 1 }} />
      <View style={{ marginTop: 15 }}>
        <Text style={styles.questionHeading}>Question:</Text>
        <Text style={styles.question}>{questionText}</Text>
      </View>
      <View>{children}</View>
      <Divider style={{ height: 1, marginTop: 20 }} />
      <View
        style={{
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <Button buttonStyle={styles.button} title="CHECK" onPress={verify} />
        {showStatus && <AnswerStatus success={sucess} />}
      </View>
    </ScrollView>
  );
};

QuestionLayout.propTypes = {
  /** Valid question type */
  questionType: PropTypes.string,
  /** Text of the question statement */
  questionText: PropTypes.string,
  /** Child components to be rendered */
  children: PropTypes.node,
  /** Function that gives the result whether the answer was valid or not */
  verifyAnswer: PropTypes.func,
  /** Whether to show the answer status or not */
  showStatus: PropTypes.bool,
};

const styles = StyleSheet.create({
  heading: {
    fontSize: 30,
    textAlign: 'center',
  },
  questionHeading: {
    fontWeight: 'bold',
    fontSize: 20,
    textAlign: 'center',
  },
  question: {
    fontSize: 20,
    textAlign: 'center',
    marginBottom: 20,
  },
  button: {
    marginTop: 20,
    marginBottom: 15,
    width: 250,
    borderRadius: 5,
    height: 45,
  },
});
