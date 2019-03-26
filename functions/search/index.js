//Based on listener from https://www.algolia.com/doc/guides/sending-and-managing-data/send-and-update-your-data/tutorials/firebase-algolia/#ongoing-live-sync-to-algolia

const algoliasearch = require('algoliasearch');
const dotenv = require('dotenv');
const firebase = require('firebase');

// load values from the .env file in this directory into process.env
dotenv.load();

// configure firebase
firebase.initializeApp({
  databaseURL: process.env.FIREBASE_DATABASE_URL,
});
const database = firebase.database();

// configure algolia
const algolia = algoliasearch(
  process.env.ALGOLIA_APP_ID,
  process.env.ALGOLIA_API_KEY
);
const index = algolia.initIndex(process.env.ALGOLIA_INDEX_NAME);

const contactsRef = database.ref('/books');
contactsRef.on('child_added', addOrUpdateIndexRecord);
contactsRef.on('child_changed', addOrUpdateIndexRecord);
contactsRef.on('child_removed', deleteIndexRecord);

function addOrUpdateIndexRecord(book) {
  // Get Firebase object
  const record = book.val();
  // Specify Algolia's objectID using the Firebase object key
  if (record.availableFrom && Object.keys(record.availableFrom).length>0) {
	  record.objectID = book.key;
	  // Add or update object
	  index
		.saveObject(record)
		.then(() => {
		  console.log('Firebase object indexed in Algolia', record.objectID);
		})
		.catch(error => {
		  console.error('Error when indexing book into Algolia', error);
		  process.exit(1);
		});
  }
  else{
	  deleteIndexRecord(book);
  }
}

function deleteIndexRecord(book) {
  // Get Algolia's objectID from the Firebase object key
  const objectID = book.key;
  // Remove the object from Algolia
  index
    .deleteObject(objectID)
    .then(() => {
      console.log('Firebase object deleted from Algolia', objectID);
    })
    .catch(error => {
      console.error('Error when deleting book from Algolia', error);
      process.exit(1);
    });
}
