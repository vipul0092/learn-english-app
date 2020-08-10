import { default as fetchAndRetryIfServerBusy } from './fetchAndRetryIfServerBusy';
import { default as FetchError } from './FetchError';

/**
 * Error handler callback
 * @callback errorHandler
 * @param {Response} response - see https://developer.mozilla.org/en-US/docs/Web/API/Response
 */

/**
 * A custom fetch wrapper that enables additional behaviors based on the options passed in
 *
 * Sample usage (with default values):
 * <pre>
 * const fetch = fetchWithOptions({
 *   errorHandler: response => {
 *     throw new FetchError(response.statusText, response);
 *   },
 *   retry: {
 *     initialDelay: 1000,
 *     multiplicationFactor: 2,
 *     maxDelay: 5000,
 *     retryStatuses: [429, 502, 503]
 *   }
 * });
 * </pre>
 *
 * @param {Object} options
 * @param {errorHandler} options.errorHandler - function to call when the response is not successful (status code >= 300)
 *                                              defaults to throwing {@link FetchError}
 * @param {Object} options.retry - retry fetch request, use same retry options as {@link fetchAndRetryIfServerBusy}
 */

export default function fetchWithOptions({
  errorHandler = (response) => {
    throw new FetchError(response.statusText, response);
  },
  retry: {
    initialDelay = 1000,
    multiplicationFactor = 2,
    maxDelay = 5000,
  } = {},
} = {}) {
  return (request) => {
    const response = fetchAndRetryIfServerBusy(request, {
      initialDelay,
      multiplicationFactor,
      maxDelay,
    });
    return response.then((res) => {
      if (!res.ok) {
        errorHandler(res);
      }
      return res;
    });
  };
}
