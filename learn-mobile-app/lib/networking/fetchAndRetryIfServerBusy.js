const pause = (milliseconds) => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(), milliseconds);
  });
};

/**
 * Wrapper for the fetch api that takes additional options around how to deal with
 * server too busy conditions
 * @param request - The fetch request created from the RequestBuilder.
 * @param options.initialDelay - number of milliseconds to wait if the 1st call is a retry status
 * @param options.multiplicationFactor - exponential back off
 * @param options.maxDelay - longest time to delay in milliseconds before just returning a response.
 * @param options.retryStatuses - response statuses to retry, defaults to [429]
 **/
export default function fetchAndRetryIfServerBusy(request, options = {}) {
  const {
    initialDelay = 1000,
    // Keeping this misspelled option to maintain backwards compatibility.
    multiplactionFactor,
    multiplicationFactor,
    maxDelay = 64000,
    retryStatuses = [502, 503, 504, 429, 408],
  } = options;
  const exponentialBackoffFactor =
    multiplicationFactor || multiplactionFactor || 2;
  let delay = initialDelay;
  function checkAndFetch() {
    return fetch(request).then((response) => {
      if (delay > maxDelay || !retryStatuses.includes(response.status)) {
        return response;
      }

      // server was busy, retrying again in ${delay} MS
      const newResponse = pause(delay).then(checkAndFetch);
      delay = delay * exponentialBackoffFactor;
      return newResponse;
    });
  }

  return checkAndFetch().then((response) => {
    if (retryStatuses.includes(response.status)) {
      // max retries reached, giving up. ${delay} MS
    }
    return response;
  });
}
