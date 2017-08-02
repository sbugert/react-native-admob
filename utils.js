export const createErrorFromErrorData = (errorData) => {
  const {
    message,
    ...extraErrorInfo
  } = errorData || {};
  const error = new Error(message);
  error.framesToPop = 1;
  return Object.assign(error, extraErrorInfo);
}
