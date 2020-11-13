import React, { useState, useMemo, useRef, useCallback } from 'react';
import { Text, StyleSheet, View } from 'react-native';
import { Icon } from 'react-native-elements';
import Video from 'react-native-video';
import { audios } from '../../assets/audios';
import { QUESTION_TYPES } from './questionTypes';
import { QuestionLayout } from './QuestionLayout';

const padZero = (number) => (number < 10 ? `0${number}` : `${number}`);
// eslint-disable-next-line prettier/prettier
const nop = () => { };

const VideoQuestion = ({ text, videoSource }) => {
  const [state, setState] = useState({
    rate: 1,
    volume: 1,
    muted: false,
    duration: 0.0,
    currentTime: 0.0,
  });
  const [paused, setPaused] = useState(true);
  const video = useRef(null);

  const setProperty = useCallback(
    (object) =>
      setState((existing) => {
        return { ...existing, ...object };
      }),
    [setState],
  );

  const onLoad = useCallback(
    (data) => {
      setProperty({ duration: data.duration });
    },
    [setProperty],
  );

  const onProgress = useCallback(
    (data) => {
      setProperty({ currentTime: data.currentTime });
    },
    [setProperty],
  );

  const onEnd = useCallback(() => {
    video.current.seek(0);
    setProperty({ currentTime: 0 });
    setPaused(true);
  }, [setProperty, setPaused]);

  const onAudioBecomingNoisy = useCallback(() => {
    setPaused(true);
  }, [setPaused]);

  const onAudioFocusChanged = useCallback(
    (event) => {
      setPaused(!event.hasAudioFocus);
    },
    [setPaused],
  );

  const getCurrentTimePercentage = useCallback(() => {
    if (state.currentTime > 0) {
      return parseFloat(state.currentTime) / parseFloat(state.duration);
    }
    return 0;
  }, [state.currentTime, state.duration]);

  const getTimeString = useCallback((duration) => {
    if (duration === 0) {
      return '00:00';
    }
    const minutes = Math.floor(duration / 60);
    const seconds = Math.ceil(duration % 60);
    return `${minutes}:${padZero(seconds)}`;
  }, []);

  const currentTimePercentage = getCurrentTimePercentage();
  const flexCompleted = useMemo(() => currentTimePercentage * 100, [
    currentTimePercentage,
  ]);
  const flexRemaining = useMemo(() => (1 - currentTimePercentage) * 100, [
    currentTimePercentage,
  ]);

  return (
    <QuestionLayout
      questionType={QUESTION_TYPES.AUDIO}
      questionText={text}
      verifyAnswer={nop}
      showStatus={false}>
      <Video
        ref={video}
        audioOnly
        source={audios[videoSource]} // Can be a URL or a local file
        paused={paused}
        onLoad={onLoad}
        onProgress={onProgress}
        onEnd={onEnd}
        onAudioBecomingNoisy={onAudioBecomingNoisy}
        onAudioFocusChanged={onAudioFocusChanged}
        repeat={false}
      />
      <View style={styles.centered}>
        <View>
          <Icon
            type="material"
            name="skip-previous"
            color="black"
            size={30}
            onPress={onEnd}
          />
        </View>
        <View>
          <Icon
            type="material"
            name={paused ? 'play-circle-filled' : 'pause-circle-filled'}
            color="black"
            size={60}
            onPress={() => setPaused(!paused)}
          />
        </View>
        <View>
          <Icon
            type="material"
            name="stop"
            color="black"
            size={30}
            onPress={onEnd}
          />
        </View>
      </View>
      <View style={styles.progress}>
        <View
          style={[styles.innerProgressCompleted, { flex: flexCompleted }]}
        />
        <View
          style={[styles.innerProgressRemaining, { flex: flexRemaining }]}
        />
      </View>
      <View style={styles.audioTimeBar}>
        <Text style={styles.alignLeft}>{getTimeString(state.currentTime)}</Text>
        <Text style={styles.alignRight}>{getTimeString(state.duration)}</Text>
      </View>
    </QuestionLayout>
  );
};

const styles = StyleSheet.create({
  centered: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  audioTimeBar: {
    paddingLeft: 10,
    paddingRight: 10,
    flexDirection: 'row',
  },
  text: {
    fontFamily: 'UbuntuBold',
    fontSize: 20,
    paddingBottom: 10,
  },
  progress: {
    paddingLeft: 10,
    paddingRight: 10,
    flexDirection: 'row',
    borderRadius: 10,
    overflow: 'hidden',
    marginTop: 10,
  },
  alignLeft: {
    display: 'flex',
    flex: 0.5,
  },
  alignRight: {
    display: 'flex',
    flex: 0.5,
    textAlign: 'right',
  },
  innerProgressCompleted: {
    height: 5,
    borderRadius: 10,
    backgroundColor: '#2C2C2C',
  },
  innerProgressRemaining: {
    height: 5,
    borderRadius: 10,
    backgroundColor: '#cccccc',
  },
});

export default VideoQuestion;
