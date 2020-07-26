import React from 'react';
import { createMaterialTopTabNavigator } from '@react-navigation/material-top-tabs';
import Beginner from './Beginner';
import Intermediate from './Intermediate';
import Expert from './Expert';

const Tab = createMaterialTopTabNavigator();

const Activities = () => {
  return (
    <Tab.Navigator initialRouteName="Beginner">
      <Tab.Screen name="Beginner" component={Beginner} />
      <Tab.Screen name="Intermediate" component={Intermediate} />
      <Tab.Screen name="Expert" component={Expert} />
    </Tab.Navigator>
  );
};

export default Activities;
