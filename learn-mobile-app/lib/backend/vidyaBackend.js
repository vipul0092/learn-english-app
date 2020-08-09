import { RequestBuilder } from '../networking';

const APP_API_KEY = 'APP_API_KEY_VALUE';
const VIDYA_BASE_URL = 'VIDYA_BASE_URL_VALUE';

const fetchDataWithOptions = async (request) => {
  try {
    const response = await fetch(request);

    if (!response.ok) {
      return null;
    }
    /**
     * Conditional check for responseBody. When the status is 204 (No Content), then
     * we would return empty object, else return the json content from response.
     */
    return response.status === 204 ? {} : await response.json();
  } catch (error) {
    // TODO: figure out how to log this error
    return null;
  }
};

export const createToken = async (email, pass) => {
  const createTokenUrl = `${VIDYA_BASE_URL}/auth/token`;
  const request = new RequestBuilder({})
    .url(createTokenUrl)
    .auth(`api_key ${APP_API_KEY}`)
    .post()
    .json({
      email,
      pass,
    })
    .build();

  return await fetchDataWithOptions(request);
};

export const verifyToken = async (token) => {
  console.log('Verifying token: ' + token);
  const verifyTokenUrl = `${VIDYA_BASE_URL}/auth/token/${token}/verify`;
  const request = new RequestBuilder({})
    .url(verifyTokenUrl)
    .auth(`api_key ${APP_API_KEY}`)
    .get()
    .build();

  const result = await fetchDataWithOptions(request);
  console.log(`Token is ${result ? 'valid' : 'invalid'}!`);
  return result;
};

export const getStudentData = async (token) => {
  const getStudentUrl = `${VIDYA_BASE_URL}/students`;
  const request = new RequestBuilder({})
    .url(getStudentUrl)
    .auth(`bearer ${token}`)
    .get()
    .build();

  return await fetchDataWithOptions(request);
};
