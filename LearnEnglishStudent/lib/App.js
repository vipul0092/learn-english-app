import React, { useState } from 'react';
import Login from './components/Login';
import { View, Text } from 'react-native';

const App = () => {
  const [isLoggedIn, setLoginFlag] = useState(false);
  return !isLoggedIn ? (
    <Login setLoginFlag={setLoginFlag} />
  ) : (
    <View>
      <Text>Logged In!</Text>
    </View>
  );
};

export default App;
