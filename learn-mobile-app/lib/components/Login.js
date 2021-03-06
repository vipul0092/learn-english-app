import React, { useState, useRef, forwardRef, useCallback } from 'react';
import {
  Dimensions,
  Keyboard,
  ScrollView,
  StyleSheet,
  TouchableWithoutFeedback,
  View,
} from 'react-native';
import { Button, Input, Icon, Text } from 'react-native-elements';
import { useUserContext } from '../context/UserContext';
import LoadingOverlay from './overlays/LoadingOverlay';
import { Snackbar } from 'react-native-paper';

const SCREEN_WIDTH = Dimensions.get('window').width;
const SCREEN_HEIGHT = Dimensions.get('window').height;
const MIN_PASSWORD_LENGTH = 6;

const defaultState = {
  username: '',
  usernameValid: true,
  password: '',
  passwordValid: true,
  isLoading: false,
};

const LoginFailedSnackBar = ({ loginFailed, setLoginFailed }) => {
  return (
    <Snackbar
      visible={loginFailed}
      duration={3000}
      onDismiss={() => setLoginFailed(false)}>
      Login Failed! Please Try Again.
    </Snackbar>
  );
};

const Login = () => {
  const usernameInput = useRef(null);
  const passwordInput = useRef(null);
  const { signIn } = useUserContext();

  const [loginForm, setLoginFormState] = useState(defaultState);
  const [showOverlay, setOverlayFlag] = useState(false);
  const [loginFailed, setLoginFailed] = useState(false);

  const setState = useCallback(
    (field, value) =>
      setLoginFormState((currentState) => ({
        ...currentState,
        [field]: value,
      })),
    [setLoginFormState],
  );

  const validateUsername = useCallback(() => {
    const { username } = loginForm;
    const usernameValid = username.length > 0;
    setState('usernameValid', usernameValid);
    usernameValid || usernameInput.current.shake();
    return usernameValid;
  }, [loginForm, setState]);

  const validatePassword = useCallback(() => {
    const { password } = loginForm;
    const passwordValid = password.length >= MIN_PASSWORD_LENGTH;
    setState('passwordValid', passwordValid);
    passwordValid || passwordInput.current.shake();
    return passwordValid;
  }, [loginForm, setState]);

  const login = useCallback(() => {
    const isUserNameValid = validateUsername();
    const isPasswordValid = validatePassword();
    if (isUserNameValid && isPasswordValid) {
      setOverlayFlag(true);
      setLoginFailed(false);
      signIn(loginForm.username, loginForm.password).then((isSuccessful) => {
        setLoginFailed(!isSuccessful);
        setOverlayFlag(false);
      });
    }
  }, [
    loginForm,
    setOverlayFlag,
    setLoginFailed,
    signIn,
    validatePassword,
    validateUsername,
  ]);

  return (
    <TouchableWithoutFeedback onPress={Keyboard.dismiss} accessible={false}>
      <ScrollView
        keyboardShouldPersistTaps="handled"
        contentContainerStyle={styles.container}>
        <View style={{ width: '80%', alignItems: 'center' }}>
          <Text h4 h4Style={styles.header}>
            Learning English with Rafa
          </Text>
          <FormInput
            ref={usernameInput}
            icon="user"
            value={loginForm.username}
            onChangeText={(username) => setState('username', username)}
            placeholder="Enter your username..."
            returnKeyType="next"
            errorMessage={
              loginForm.usernameValid ? null : "Your username can't be blank"
            }
            onSubmitEditing={() => {
              validateUsername() && passwordInput.current.focus();
            }}
          />
          <FormInput
            ref={passwordInput}
            icon="lock"
            value={loginForm.password}
            onChangeText={(password) => setState('password', password)}
            placeholder="Enter your password..."
            secureTextEntry
            returnKeyType="next"
            errorMessage={
              loginForm.passwordValid
                ? null
                : `Please enter at least ${MIN_PASSWORD_LENGTH} characters`
            }
            onSubmitEditing={login}
          />
          <Button
            loading={loginForm.isLoading}
            title="LOG IN"
            containerStyle={{ flex: -1 }}
            buttonStyle={styles.signUpButton}
            titleStyle={styles.signUpButtonText}
            onPress={login}
            icon={
              <Icon
                type="material-community"
                name="login"
                color="white"
                size={18}
              />
            }
            disabled={loginForm.isLoading}
          />
          <LoadingOverlay
            isVisible={showOverlay}
            text="Please wait while we log you in..."
          />
        </View>
        <LoginFailedSnackBar
          loginFailed={loginFailed}
          setLoginFailed={setLoginFailed}
        />
      </ScrollView>
    </TouchableWithoutFeedback>
  );
};

const FormInput = forwardRef(({ icon, ...otherProps }, ref) => (
  <Input
    {...otherProps}
    ref={ref}
    inputContainerStyle={styles.inputContainer}
    leftIcon={
      <Icon name={icon} type={'simple-line-icon'} color="#7384B4" size={18} />
    }
    inputStyle={styles.inputStyle}
    autoFocus={false}
    autoCapitalize="none"
    keyboardAppearance="dark"
    errorStyle={styles.errorInputStyle}
    autoCorrect={false}
    blurOnSubmit={false}
    placeholderTextColor="#7384B4"
  />
));

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    paddingBottom: 20,
    backgroundColor: '#293046',
    width: SCREEN_WIDTH,
    height: SCREEN_HEIGHT,
    alignItems: 'center',
    justifyContent: 'space-around',
  },
  errorInputStyle: {
    marginTop: 0,
    textAlign: 'center',
    color: '#F44336',
  },
  formContainer: {
    flex: 1,
    justifyContent: 'space-around',
    alignItems: 'center',
  },
  header: {
    textAlign: 'center',
    color: 'rgba(110, 120, 170, 1)',
    marginBottom: 20,
  },
  iconContainerStyle: {
    marginRight: 10,
  },
  inputContainer: {
    paddingLeft: 8,
    borderRadius: 40,
    borderWidth: 1,
    borderColor: 'rgba(110, 120, 170, 1)',
    height: 45,
    marginVertical: 10,
  },
  inputStyle: {
    flex: 1,
    marginLeft: 10,
    color: 'white',
    fontFamily: 'UbuntuLight',
    fontSize: 16,
  },
  signUpButton: {
    width: 250,
    borderRadius: Math.round(45 / 2),
    height: 45,
  },
  signUpButtonText: {
    fontFamily: 'UbuntuBold',
    fontSize: 15,
    paddingLeft: 10,
  },
});

export default Login;
