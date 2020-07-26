import 'react-native-gesture-handler';
import 'react-native-paper';
import React, { useState, useEffect } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Login from './components/Login';
import SplashScreen from './components/SplashScreen';
import HomeScreen from './components/home/ParentHome';
import AuthContext, { useSetupUserData } from './context/AuthContext';

const Stack = createStackNavigator();

const App = () => {
  const [loading, setLoading] = useState(true);
  const { fetchInitialData, contextValue } = useSetupUserData();

  useEffect(() => {
    // Fetch the token from storage then navigate to our appropriate place
    const fetchData = async () => {
      await fetchInitialData();
      setLoading(false);
    };
    fetchData();
  }, [fetchInitialData]);

  return (
    <AuthContext.Provider value={contextValue}>
      <Stack.Navigator headerMode="none">
        {loading ? (
          // We haven't finished checking for the token yet
          <Stack.Screen name="Splash" component={SplashScreen} />
        ) : !contextValue.isSignedIn ? (
          // No token found, user isn't signed in
          <Stack.Screen
            name="SignIn"
            component={Login}
            options={{
              // When logging out, a pop animation feels intuitive
              animationTypeForReplace: 'push',
            }}
          />
        ) : (
          // User is signed in
          <Stack.Screen name="Home" component={HomeScreen} />
        )}
      </Stack.Navigator>
    </AuthContext.Provider>
  );
};

const AppWithNavigation = () => {
  return (
    <NavigationContainer>
      <App />
    </NavigationContainer>
  );
};

export default AppWithNavigation;
