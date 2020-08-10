/**
 * A custom error class to be used with fetch
 */
export default function FetchError(message, response) {
  const instance = new Error(message);
  Object.setPrototypeOf(instance, Object.getPrototypeOf(this));
  Object.defineProperty(instance, 'response', { value: response });
  return instance;
}
FetchError.prototype = Object.create(Error.prototype, {
  constructor: {
    value: Error,
    enumerable: false,
    writable: true,
    configurable: true,
  },
});
Object.setPrototypeOf(FetchError, Error);
