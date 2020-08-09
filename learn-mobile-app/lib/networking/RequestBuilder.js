import qs from 'query-string';

/**
 * Builder class for fetch api Request objects.
 * See https://developer.mozilla.org/en-US/docs/Web/API/Request/Request
 */
export default class RequestBuilder {
  constructor(requestOptions) {
    const requestHeaders = requestOptions.headers
      ? new Headers(requestOptions.headers)
      : new Headers();
    this.requestOptions = Object.assign({}, requestOptions, {
      headers: requestHeaders,
    });
  }
  /* sets credentials to allow */
  withCookies() {
    // we're doing everything via CORS now so there is no use case for 'same-origin'
    return new RequestBuilder(
      Object.assign({}, this.requestOptions, { credentials: 'include' }),
    );
  }

  withNoCache() {
    // in order to avoid caching, passing `no-cache` avoids calls from being cached in the browser
    return new RequestBuilder(
      Object.assign({}, this.requestOptions, { cache: 'no-cache' }),
    );
  }

  /* Return a builder with a url set to url */
  url(url) {
    return new RequestBuilder(Object.assign({}, this.requestOptions, { url }));
  }

  /* Return a builder with the HTTP method for request set to POST */
  post() {
    return this.method('POST');
  }

  /* Return a builder with the HTTP method for request set to GET */
  get() {
    return this.method('GET');
  }

  /* Return a builder with the HTTP method for request set to PUT */
  put() {
    return this.method('PUT');
  }

  /* Return a builder with the HTTP method for request set to DELETE */
  delete() {
    return this.method('DELETE');
  }

  /* Return a builder with the HTTP method for request set to OPTIONS */
  options() {
    return this.method('OPTIONS');
  }

  /* Return a builder with the HTTP method for request built by the builder. */
  method(method) {
    return new RequestBuilder(
      Object.assign({}, this.requestOptions, { method }),
    );
  }

  /** Return a builder with the auth token set the Autorization header **/
  auth(token) {
    return this.header('Authorization', token);
  }

  // TODO  consider adding path param function.

  /**
   * Return a builder with the query param. If the param value is an array the
   * param will appear for each value in the array.
   */
  query(param, value) {
    const { url } = this.requestOptions;
    const base = url.split('?')[0];
    const search = qs.parse(url.split('?')[1]);
    search[param] = value;
    return this.url(`${base}?${qs.stringify(search)}`);
  }

  /* Return a builder with the json string of value set as the request body */
  json(value) {
    const body = JSON.stringify(value);
    return this.header('Content-Type', 'application/json').body(body);
  }

  /* Return a builder with a set request body */
  body(value) {
    return new RequestBuilder(
      Object.assign({}, this.requestOptions, { body: value }),
    );
  }

  /* Return a builder with a header set to value */
  header(header, value) {
    const headers = this.requestOptions.headers
      ? new Headers(this.requestOptions.headers)
      : new Headers();
    headers.set(header, value);
    return new RequestBuilder(
      Object.assign({}, this.requestOptions, { headers }),
    );
  }

  /* Build a fetch Request object */
  // https://developer.mozilla.org/en-US/docs/Web/API/Request
  build(): Request {
    const { url } = this.requestOptions;
    const requestHeaders = this.requestOptions.headers
      ? new Headers(this.requestOptions.headers)
      : new Headers();
    const request = new Request(
      url,
      Object.assign({}, this.requestOptions, {
        headers: requestHeaders,
      }),
    );
    return request;
  }
}
