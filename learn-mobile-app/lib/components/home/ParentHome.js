import React from 'react';
import { createMaterialBottomTabNavigator } from '@react-navigation/material-bottom-tabs';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';
import Account from './Account';
import AppInfo from './AppInfo';
import Activites from './activities/Activities';

const Tab = createMaterialBottomTabNavigator();

const ParentHome = () => {
  return (
    <Tab.Navigator
      initialRouteName="Activites"
      activeColor="#f0edf6"
      inactiveColor="black"
      barStyle={{ backgroundColor: '#694fad' }}>
      <Tab.Screen
        name="Activites"
        component={Activites}
        options={{
          tabBarLabel: 'Activites',
          tabBarIcon: ({ color }) => (
            <MaterialCommunityIcons name="home" color={color} size={26} />
          ),
        }}
      />
      <Tab.Screen
        name="Account"
        component={Account}
        options={{
          tabBarLabel: 'Account',
          tabBarIcon: ({ color }) => (
            <MaterialCommunityIcons name="account" color={color} size={26} />
          ),
        }}
      />
      <Tab.Screen
        name="App Informaton"
        component={AppInfo}
        options={{
          tabBarLabel: 'App Informaton',
          tabBarIcon: ({ color }) => (
            <MaterialCommunityIcons
              name="information"
              color={color}
              size={26}
            />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export default ParentHome;
