// Based on https://github.com/firebase/functions-samples/blob/master/fcm-notifications/functions/index.js
// Licensed under Apache License, Version 2.0

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendNotification = functions.https.onCall((data) => {
  // Get the list of device notification tokens.
  const tokensPromise = admin.database().ref(`/users/${data.userId}/deviceTokens`).once('value');

  // The snapshot to the user's tokens.
  let tokensSnapshot;

  // The array containing all the user's tokens.
  let tokens;

  return Promise.all([tokensPromise]).then(results => {
    tokensSnapshot = results[0];

    // Check if there are any device tokens.
    if (!tokensSnapshot.hasChildren()) {
      return console.log('There are no notification tokens to send to.');
    }
    console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');

    // Notification details.
    const payload = {
      notification: {
        title: data.title,
        body: data.body
      }
    };

    // Listing all tokens as an array.
    tokens = Object.keys(tokensSnapshot.val());
    console.log(`Tokens: ${tokens}`);

    // Send notifications to all tokens.
    return admin.messaging().sendToDevice(tokens, payload);
  }).then((response) => {
    // For each message check if there was an error.
    const tokensToRemove = [];
    response.results.forEach((result, index) => {
      const error = result.error;
      if (error) {
        console.error('Failure sending notification to', tokens[index], error);
        // Cleanup the tokens who are not registered anymore.
        if (error.code === 'messaging/invalid-registration-token' ||
          error.code === 'messaging/registration-token-not-registered') {
          tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
        }
      }
    });
    return Promise.all(tokensToRemove);
  });
});