import React, { useCallback, useContext, useState, useMemo } from 'react';
import {
  getUserInfo,
  saveUserInfo,
  removeUserInfo,
} from '../localStorage/userInfo';
import {
  createToken,
  getStudentData,
  verifyToken,
} from '../backend/vidyaBackend';
const UserContext = React.createContext();

const signIn = async (email, pass, setToken, setUserData) => {
  const tokenData = await createToken(email, pass);
  const studentInfo = tokenData && (await getStudentData(tokenData.tokenId));

  const userData = studentInfo && { ...tokenData, ...studentInfo };
  const successful = userData && (await saveUserInfo(userData));
  if (successful) {
    setToken(tokenData.tokenId);
    setUserData(userData);
  }
  return !!successful;
};

export const useSetupUserData = () => {
  const [token, setToken] = useState('');
  const [userData, setUserData] = useState({});

  const fetchInitialData = useCallback(async () => {
    let userInfo;
    try {
      userInfo = (await getUserInfo()) || {};
    } catch (e) {
      // Restoring token failed
    }
    if (userInfo.tokenId) {
      const isTokenValid = await verifyToken(userInfo.tokenId);
      if (isTokenValid) {
        setToken(userInfo.tokenId);
        setUserData(userInfo);
      } else {
        await removeUserInfo();
      }
    }
  }, []);

  const contextValue = useMemo(
    () => ({
      isSignedIn: !!token,
      userName: userData.name,
      signIn: async (email, pass) =>
        await signIn(email, pass, setToken, setUserData),
      signOut: async () => {
        // TODO: We shoud be revoking the token on logging out
        const successful = await saveUserInfo({});
        if (successful) {
          setToken('');
          setUserData({});
        }
      },
    }),
    [token, userData],
  );

  return {
    fetchInitialData,
    contextValue,
  };
};

export const useUserContext = () => {
  return useContext(UserContext);
};

export default UserContext;
