import AsyncStorage from '@react-native-community/async-storage';

const USERINFO_KEY = 'userDataV1';

export const getUserInfo = async () => {
  let userInfo;

  try {
    userInfo = await AsyncStorage.getItem(USERINFO_KEY);
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (e) {
    // Failed getting user info
  }
  return userInfo && JSON.parse(userInfo);
};

export const saveUserInfo = async (userInfo) => {
  try {
    await AsyncStorage.setItem(USERINFO_KEY, JSON.stringify(userInfo));
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (e) {
    // Failed saving user info
    return false;
  }
  return true;
};

export const removeUserInfo = async () => {
  try {
    await AsyncStorage.removeItem(USERINFO_KEY);
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (e) {
    // Failed deleting user info
    return false;
  }
  return true;
};
