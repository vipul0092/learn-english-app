import React, { useState } from 'react';
import { View, Text, StyleSheet, Button } from 'react-native';
import { useUserContext } from '../../../context/UserContext';
import LoadingOverlay from '../../overlays/LoadingOverlay';

const Intermediate = () => {
  const { userName, signOut } = useUserContext();
  const [showOverlay, setOverlayFlag] = useState(false);
  const logOut = () => {
    setOverlayFlag(true);
    signOut().then(() => setOverlayFlag(false));
  };
  return (
    <View style={styles.container}>
      <Text>{`Hi ${userName}!, you are in the intermediate section`}</Text>
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

export default Intermediate;
