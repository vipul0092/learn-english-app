import React, { useMemo, useState, useCallback } from 'react';
import { View, StyleSheet } from 'react-native';
import { Text } from 'react-native-elements';
import TouchableScale from 'react-native-touchable-scale';
import { QuestionLayout } from './QuestionLayout';
import { QUESTION_TYPES } from './questionTypes';

const QUESTION_TEXT = 'Can you complete the sentence below correctly?';
const QUESTION_DESC = 'How ? you doing today?';
const ANSWER = 'are';
const CHOICES = [
  { text: 'is', show: true },
  { text: 'am', show: true },
  { text: 'are', show: true },
  { text: 'were', show: true },
];

const WordText = ({ show, text }) => (
  <Text style={show ? styles.text : styles.disabledText}>{text}</Text>
);

const TouchableText = ({ word, onPress, showOverride = false }) => {
  const show = word.show || showOverride;

  return show ? (
    <TouchableScale onPress={() => onPress(word.text)}>
      <WordText show={show} text={word.text} />
    </TouchableScale>
  ) : (
    <WordText show={show} text={word.text} />
  );
};

const SelectedAnswer = ({ selection, hideTopShowChoice }) => {
  const word = useMemo(() => ({ text: selection, show: true }), [selection]);
  return selection ? (
    <View key={`${selection}-answer`} style={styles.answerWord}>
      <TouchableText word={word} onPress={hideTopShowChoice} />
    </View>
  ) : (
    <View key="answer-selection">
      <Text style={styles.words}>______ </Text>
    </View>
  );
};

const Description = ({ words, selection, hideTopShowChoice }) => {
  return (
    <View style={styles.centered}>
      {words.map((word) =>
        word === '? ' ? (
          <SelectedAnswer
            key={word}
            selection={selection}
            hideTopShowChoice={hideTopShowChoice}
          />
        ) : (
          <View key={word}>
            <Text style={styles.words}>{word}</Text>
          </View>
        ),
      )}
    </View>
  );
};

const Choices = ({ choices, disableChoiceShowTop }) => {
  return (
    <>
      <Text style={styles.bottomChoices}>Choices:</Text>
      <View style={styles.centered}>
        {Object.values(choices).map((choice) => (
          <View key={choice.text} style={styles.topWord}>
            <TouchableText word={choice} onPress={disableChoiceShowTop} />
          </View>
        ))}
      </View>
    </>
  );
};

export const SingleChoiceQuestion = () => {
  const choiceValues = useMemo(() => {
    const choiceWords = {};
    CHOICES.forEach((choice) => (choiceWords[choice.text] = choice));
    return choiceWords;
  }, []);
  const [choices, setChoices] = useState(choiceValues);
  const [showStatus, setShowStatus] = useState(false);
  const sentenceWords = useMemo(
    () => QUESTION_DESC.split(' ').map((w) => `${w} `),
    [],
  );
  const [selection, setSelection] = useState('');

  const verifyAnswer = useCallback(() => {
    if (!selection) {
      return;
    }
    setShowStatus(true);
    return selection === ANSWER;
  }, [selection]);

  const disableChoiceShowTop = useCallback(
    (text) => {
      let newData = { ...choices };
      newData[text].show = false;
      // If there is already a selection, show it in the choices again
      if (selection) {
        newData[selection].show = true;
      }
      setSelection(text);
      setChoices(newData);
      setShowStatus(false);
    },
    [choices, setSelection, setChoices, selection],
  );

  const hideTopShowChoice = useCallback(
    (text) => {
      let newData = { ...choices };
      newData[text].show = true;

      setSelection('');
      setChoices(newData);
      setShowStatus(false);
    },
    [choices, setSelection, setChoices],
  );

  return (
    <QuestionLayout
      questionType={QUESTION_TYPES.SINGLE_CHOICE}
      questionText={QUESTION_TEXT}
      verifyAnswer={verifyAnswer}
      showStatus={showStatus}>
      <View style={styles.parentView}>
        <Description
          words={sentenceWords}
          selection={selection}
          hideTopShowChoice={hideTopShowChoice}
        />
        <Choices
          choices={choices}
          disableChoiceShowTop={disableChoiceShowTop}
        />
      </View>
    </QuestionLayout>
  );
};

const wordTextStyles = {
  fontFamily: 'UbuntuBold',
  fontSize: 20,
};

const touchableTextStyles = {
  fontFamily: 'UbuntuBold',
  fontSize: 20,
  padding: 10,
  color: 'white',
  borderRadius: 5,
};

const styles = StyleSheet.create({
  centered: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  parentView: {
    flexDirection: 'column',
    marginBottom: 15,
  },
  words: {
    ...wordTextStyles,
  },
  answerWord: {
    marginRight: 5,
    borderRadius: 5,
  },
  topWord: {
    marginLeft: 5,
    marginRight: 5,
    marginTop: 10,
    borderRadius: 5,
  },
  bottomChoices: {
    fontWeight: 'bold',
    paddingTop: 20,
    paddingLeft: 10,
    marginBottom: 10,
    fontSize: 20,
    textAlign: 'center',
  },
  text: {
    ...touchableTextStyles,
    backgroundColor: 'rgba(110, 120, 170, 1)',
  },
  disabledText: {
    ...touchableTextStyles,
    backgroundColor: 'rgba(189, 189, 189, 1)',
  },
});
