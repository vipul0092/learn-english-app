import React, { useState, useRef, useCallback } from 'react';
import { Text, StyleSheet, View } from 'react-native';
import { Icon } from 'react-native-elements';
import Video from 'react-native-video';
import { audios } from '../../assets/audios';

const padZero = (number) => (number < 10 ? `0${number}` : `${number}`);

const VideoQuestion = ({ text, videoName, videoSource }) => {
  const [state, setState] = useState({
    rate: 1,
    volume: 1,
    muted: false,
    duration: 0.0,
    currentTime: 0.0,
    paused: true,
  });
  const video = useRef(null);

  const setProperty = useCallback(
    (object) =>
      setState((existing) => {
        return { ...existing, ...object };
      }),
    [setState],
  );

  const onLoad = (data) => {
    setProperty({ duration: data.duration });
  };

  const onProgress = (data) => {
    setProperty({ currentTime: data.currentTime });
  };

  const onEnd = () => {
    video.current.seek(0);
    setProperty({ paused: true, currentTime: 0 });
  };

  const onAudioBecomingNoisy = () => {
    setProperty({ paused: true });
  };

  const onAudioFocusChanged = (event) => {
    setProperty({ paused: !event.hasAudioFocus });
  };

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

  const flexCompleted = getCurrentTimePercentage() * 100;
  const flexRemaining = (1 - getCurrentTimePercentage()) * 100;

  return (
    <View style={styles.container}>
      <Text style={styles.text}>{text}</Text>
      <Video
        ref={video}
        audioOnly
        source={audios[videoSource]} // Can be a URL or a local file
        paused={state.paused}
        onLoad={onLoad}
        onProgress={onProgress}
        onEnd={onEnd}
        onAudioBecomingNoisy={onAudioBecomingNoisy}
        onAudioFocusChanged={onAudioFocusChanged}
        repeat={false}
      />
      <View style={{ flexDirection: 'row', paddingBottom: 10 }}>
        <View style={{ justifyContent: 'center' }}>
          <Icon
            type="material"
            name="skip-previous"
            color="black"
            size={30}
            onPress={onEnd}
          />
        </View>
        <Icon
          type="material"
          name={state.paused ? 'play-circle-filled' : 'pause-circle-filled'}
          color="black"
          size={60}
          onPress={() => setProperty({ paused: !state.paused })}
        />
        <View style={{ justifyContent: 'center' }}>
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
      <View
        style={{
          paddingLeft: 10,
          paddingRight: 10,
          flexDirection: 'row',
        }}>
        <Text style={styles.alignLeft}>{getTimeString(state.currentTime)}</Text>
        <Text style={styles.alignRight}>{getTimeString(state.duration)}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
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
