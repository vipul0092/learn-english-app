import React, { useState } from 'react';
import { View, Text, StyleSheet, Button } from 'react-native';
import { useAuth } from '../../../context/AuthContext';
import LoadingOverlay from '../../overlays/LoadingOverlay';

const Beginner = () => {
  const { userName, signOut } = useAuth();
  const [showOverlay, setOverlayFlag] = useState(false);
  const logOut = () => {
    setOverlayFlag(true);
    signOut().then(() => setOverlayFlag(false));
  };
  return (
    <View style={styles.container}>
      <Text>{`Hi ${userName}!, you are in the beginner section`}</Text>
      <Button title="Log Out" onPress={logOut} />
      <LoadingOverlay
        isVisible={showOverlay}
        text="Logging you out, come back soon!"
      />
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
