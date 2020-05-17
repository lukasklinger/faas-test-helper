# FaaS-Test Helper

This small helper program is meant to be used in conjunction with FaaS-Test, a framework for testing serverless functions. It will try to change the function automatically to include FaaS-Test and configure some basic tests. The helper was built primarily for AWS Lambda functions but will work for other providers as well (some manual changes required).

## How To Use
First, install *FaaS-Test* using npm in your Node.js function directory.
```bash
npm install --save-dev faas-test
```

Then call the .jar file (see releases).
```bash
java -jar faas-test-helper-v1.0.0.jar /path/to/function/ function-name.js
```
