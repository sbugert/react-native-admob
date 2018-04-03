const path = require('path');
const blacklist = require('metro-bundler/src/blacklist');
const escape = require('escape-string-regexp');

module.exports = {
  getBlacklistRE() {
    return blacklist([
      new RegExp(`^${escape(path.resolve(__dirname, 'node_modules', 'react-native-admob', 'node_modules'))}\\/.*$`),
      new RegExp(`^${escape(path.resolve(__dirname, 'node_modules', 'react-native-admob', 'Example'))}\\/.*$`),
    ]);
  },
};
