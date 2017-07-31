import React from 'react';
import { StyleSheet, Text, View ,WebView} from 'react-native';

export default class App extends React.Component {
  render() {
    return (
      <WebView
          source={{uri: 'http://egov-micro-dev.egovernments.org/react-pgr-web/#/'}}
        />
    );
  }
}

// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#fff',
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
// });

// <View style={styles.container}>
//   <Text>Open up App.js to start working on your app!</Text>
// </View>
