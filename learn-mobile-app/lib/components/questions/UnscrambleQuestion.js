import React, { useMemo, useState, useCallback } from 'react';
import { View, StyleSheet } from 'react-native';
import { Text } from 'react-native-elements';
import TouchableScale from 'react-native-touchable-scale';
import { QuestionLayout } from './QuestionLayout';
import { QUESTION_TYPES } from './questionTypes';

const WORDS = [
  { text: 'is', show: true },
  { text: 'app', show: true },
  { text: 'This', show: true },
  { text: 'an', show: true },
];

const ANSWER = 'This is an app';

const QUESTION_TEXT = 'What would you call what you are using right now?';

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

const Top = ({ top, hideTopShowBottom }) => {
  return (
    <View style={styles.centered}>
      {Object.values(top).map((word) => (
        <View key={word.text} style={styles.topWord}>
          <TouchableText word={word} onPress={hideTopShowBottom} />
        </View>
      ))}
    </View>
  );
};

const Bottom = ({ bottom, hideBottomShowTop }) => {
  return (
    <View style={styles.centered}>
      {bottom.map((word) => (
        <View key={word.text} style={styles.topWord}>
          <TouchableText showOverride word={word} onPress={hideBottomShowTop} />
        </View>
      ))}
    </View>
  );
};

const Answer = ({ bottom, hideBottomShowTop }) => {
  return (
    <View>
      <Text style={styles.bottomAnswer}>Answer:</Text>
      <Bottom bottom={bottom} hideBottomShowTop={hideBottomShowTop} />
    </View>
  );
};

const UnscrambleQuestion = () => {
  const topValues = useMemo(() => {
    const topWords = {};
    WORDS.forEach((word) => (topWords[word.text] = word));
    return topWords;
  }, []);

  const [top, setTop] = useState(topValues);
  const [bottom, setBottom] = useState([]);
  const [showStatus, setShowStatus] = useState(false);

  const verifyAnswer = useCallback(() => {
    let answer = '';
    bottom.forEach((word, index) => {
      answer = answer + word.text + (index === bottom.length - 1 ? '' : ' ');
    });
    setShowStatus(true);
    return answer === ANSWER;
  }, [bottom]);

  const hideTopShowBottom = useCallback(
    (text) => {
      let newData = { ...top };
      newData[text].show = false;

      setTop(newData);
      setBottom((existing) => {
        let newBotData = [...existing];
        newBotData.push(newData[text]);
        return newBotData;
      });
      setShowStatus(false);
    },
    [setTop, setBottom, top],
  );

  const hideBottomShowTop = useCallback(
    (text) => {
      let newData = { ...top };
      newData[text].show = true;

      setTop(newData);
      setBottom((existing) => {
        let newBotData = [...existing];
        return newBotData.filter((obj) => obj.text !== text);
      });
      setShowStatus(false);
    },
    [setTop, setBottom, top],
  );

  return (
    <QuestionLayout
      questionType={QUESTION_TYPES.UNSCRAMBLE}
      questionText={QUESTION_TEXT}
      verifyAnswer={verifyAnswer}
      showStatus={showStatus}>
      <View style={styles.parentView}>
        <Top top={top} hideTopShowBottom={hideTopShowBottom} />
        <Answer bottom={bottom} hideBottomShowTop={hideBottomShowTop} />
      </View>
    </QuestionLayout>
  );
};

const wordTextStyles = {
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
  topWord: {
    marginLeft: 5,
    marginRight: 5,
    borderRadius: 5,
  },
  bottomAnswer: {
    fontWeight: 'bold',
    paddingTop: 20,
    paddingLeft: 10,
    marginBottom: 10,
    fontSize: 20,
    textAlign: 'center',
  },
  text: {
    ...wordTextStyles,
    backgroundColor: 'rgba(110, 120, 170, 1)',
  },
  disabledText: {
    ...wordTextStyles,
    backgroundColor: 'rgba(189, 189, 189, 1)',
  },
});

export default UnscrambleQuestion;
