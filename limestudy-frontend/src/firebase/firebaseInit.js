import firebase from "firebase/app";
import "firebase/firestore";


// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyDKU6e8nV_902DqzXC20e6FcPA5MQe1RVM",
    authDomain: "fireblogs-8f64b.firebaseapp.com",
    projectId: "fireblogs-8f64b",
    storageBucket: "fireblogs-8f64b.appspot.com",
    messagingSenderId: "876183633243",
    appId: "1:876183633243:web:05873fc895a57399732e7e"
};

const firebaseApp = firebase.initializeApp(firebaseConfig);
const timeStamp = firebase.firestore.FieldValue.serverTimestamp;

export { timeStamp };
export default firebaseApp.firestore();