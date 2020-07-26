import AsyncStorage from '@react-native-community/async-storage';

export const getUserInfo = async () => {
  let userInfo;

  try {
    userInfo = await AsyncStorage.getItem('userInfo');
    await new Promise((resolve) => setTimeout(resolve, 1000));
  } catch (e) {
    // Failed getting user info
  }
  return userInfo && JSON.parse(userInfo);
};

export const saveUserInfo = async (userInfo) => {
  try {
    await AsyncStorage.setItem('userInfo', JSON.stringify(userInfo));
    await new Promise((resolve) => setTimeout(resolve, 1000));
  } catch (e) {
    // Failed saving user info
    return false;
  }
  return true;
};
