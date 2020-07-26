import React, { useContext, useState, useMemo } from 'react';
import { getUserInfo, saveUserInfo } from '../localStorage/userInfo';
const AuthContext = React.createContext();

export const useSetupUserData = () => {
  const [isLoggedIn, setLoginFlag] = useState(false);
  const [userName, setUserName] = useState('');

  const fetchInitialData = async () => {
    let userInfo;
    try {
      userInfo = (await getUserInfo()) || {};
    } catch (e) {
      // Restoring token failed
    }
    if (userInfo.isLoggedIn) {
      setLoginFlag(userInfo.isLoggedIn);
    }
    if (userInfo.userName) {
      setUserName(userInfo.userName);
    }
  };

  const contextValue = useMemo(
    () => ({
      isSignedIn: isLoggedIn,
      userName: userName,
      signIn: async (user) => {
        const successful = await saveUserInfo({
          userName: user,
          isLoggedIn: true,
        });
        if (successful) {
          setLoginFlag(true);
          setUserName(user);
        }
      },
      signOut: async () => {
        const successful = await saveUserInfo({
          userName,
          isLoggedIn: false,
        });
        if (successful) {
          setLoginFlag(false);
        }
      },
    }),
    [userName, isLoggedIn],
  );

  return {
    fetchInitialData,
    contextValue,
  };
};

export const useAuth = () => {
  return useContext(AuthContext);
};

export default AuthContext;
