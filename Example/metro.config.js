/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */

const path = require('path');
const escape = require('escape-string-regexp');

module.exports = {
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
  resolver: {
    blacklistRE: new RegExp(
      `^${escape(
        path.resolve(__dirname, 'node_modules', 'react-native-admob'),
      ) + '\\/(node_modules|Example)'}\\/.*$`,
    ),
  },
};
