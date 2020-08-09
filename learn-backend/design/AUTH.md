## High Level Design
* The service exposes an OAuth like token system for access to different endpoints.
* Consuming applications will have an API Key given to them and using that they can create a token and then use that token for other endpoints.
* The service uses [Dropwizard's auth system](https://www.dropwizard.io/en/latest/manual/auth.html) for doing this.


## Fresh Login Process - Students
1. Login request from the app sends the email & password
2. Fetches the student record for the email, checks the password hash
3. Creates a token with the following metadata:
    - b. until when is the token valid
    - c. student id
    - d. teacher id
4. Stores the token in the TOKENS table
5. Returns the token back with metadata

In case of any failures, 401 is thrown

## Already Logged in Process - Students
1. Already logged in students have a token, that is sent for verification
2. Fetch the token with id from the TOKENS table
3. Check whether token is still valid by checking the "until" timestamp
4. If the token is expired, delete from table and send a 401, otherwise send the metadata back.

## Other endpoints
* Each endpoint needs to supply a token, otherwise we simply 401 the request
* The token is verified as described above before any of the endpoint logic kicks in
