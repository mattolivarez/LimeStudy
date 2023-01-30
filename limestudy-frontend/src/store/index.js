import Vue from 'vue'
import Vuex from 'vuex'
//import firebase from "firebase/app"
import "firebase/auth"
import db from "../firebase/firebaseInit"
import axios from "axios"

Vue.use(Vuex)

/*let newUser = JSON.parse(localStorage.getItem('user'));
let initialState = newUser
  ? { status: { loggedIn: true }, newUser }
  : { status: { loggedIn: false }, newUser: null };*/

export default new Vuex.Store({
    state: {
        blogPosts: [],
        postLoaded: null,
        blogHTML: "",
        blogTitle: "",
        blogPhotoName: "",
        blogPhotoFileURL: null,
        blogPhotoPreview: null,

        temp: true,

        classesLoaded: null,
        decksLoaded: null,
        flashcardsLoaded: null,

        classes: [],
        editClass: null,
        decks: [],
        editDeck: null,
        flashcards: [],
        editFlashcard: null,

        user: null,
        loggedIn: false,
        profileAdmin: null,
        profileEmail: null,
        profileFirstName: null,
        profileLastName: null,
        profileUserId: null,
        profileInitials: null,
    },
    getters: {
        blogPostsFeed(state) {
            return state.blogPosts.slice(0,2);
        },
        blogPostsCards(state) {
            return state.blogPosts.slice(2,6);
        }
    },
    mutations: {
        fileNameChange(state, payload) {
            state.blogPhotoName = payload;
        },
        createFileURL(state, payload) {
            state.blogPhotoFileURL = payload;
        },
        openPhotoPreview(state) {
            state.blogPhotoPreview = !state.blogPhotoPreview;
        },
        newBlogPost(state, payload) {
            state.blogHTML = payload;
        },
        updateBlogTitle(state, payload) {
            state.blogTitle = payload;
        },


        toggleEditClass(state, payload) {
            state.editClass = payload;
        },
        toggleEditDeck(state, payload) {
            state.editDeck = payload;
        },
        toggleEditFlashcard(state, payload) {
            state.editFlashcard = payload;
        },


        setBlogState(state, payload) {
            state.blogTitle = payload.blogTitle;
            state.blogHTML = payload.blogHTML;
            state.blogPhotoFileURL = payload.blogCoverPhoto;
            state.blogPhotoName = payload.blogCoverPhotoName;
        },
        filterBlogPost(state, payload)
        {
            state.blogPosts = state.blogPosts.filter(post => post.blogID !== payload);
        },
        updateUser(state, payload) {
            state.user = payload;
        },
        setProfileInfo(state, doc) {
            state.profileUserId = doc.id;
            state.profileEmail = doc.data().email;
            state.profileFirstName = doc.data().firstName;
            state.profileLastName = doc.data().lastName;
        },
        setUserDetails(state, details) {
            state.profileEmail = details.email;
            state.profileFirstName = details.firstName;
            state.profileLastName = details.lastName;
            state.profileUserId = details.userId;
        },
        removeUserDetails(state) {
            state.profileEmail = "";
            state.profileFirstName = "";
            state.profileLastName = "";
            state.profileUserId = null;
        },
        setProfileInitials(state) {
            state.profileInitials = state.profileFirstName.match(/(\b\S)?/g).join("") + 
                                    state.profileLastName.match(/(\b\S)?/g).join("");
        },
        removeProfileInitials(state) {
            state.profileInitials = "";
        },
        setProfileAdmin(state, payload) {
            state.profileAdmin = payload;
        },
        loginSuccess(state) {
            state.loggedIn = true;
            //state.user = user;
        },
        loginFailure(state) {
            state.loggedIn = false;
            //state.user = null;
        },
        logout(state) {
            state.loggedIn = false;
            //state.user = null;
        },
        registerSuccess(state) {
            state.loggedIn = false;
        },
        registerFailure(state) {
            state.loggedIn = false;
        },
    },
    actions: {
        async getCurrentUser({ commit }, user) {
            await axios({
                method: 'POST',
                url: '/api/users/get-user',
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                    'Content-Type': 'application/json'
                },
                data: {
                    email: user.email,
                    password: user.password
                }
            })
            .then((response) => {
                //console.log(response.data);
                const details = {
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    email: response.data.email,
                    userId: response.data.userId
                }
                commit('setUserDetails', details);
                commit('setProfileInitials');
                return;
            }).catch((err) => {
                console.log(err);
                //this.$store.commit('loginFailure');
                return;
            });
        },
        async updateUserSettings({commit, state}) {
            const dataBase = await db.collection('users').doc(state.profileUserId);
            await dataBase.update({
                firstName: state.profileFirstName,
                lastName: state.profileLastName,
                username: state.profileUsername,
            });
            commit("setProfileInitials");
        },
        async getPost({state}) {
            const dataBase = await db.collection('blogPosts').orderBy('date', 'desc');
            const dbResults = await dataBase.get();
            dbResults.forEach(doc => {
                if (!state.blogPosts.some(post => post.blogID === doc.id))
                {
                    const data = {
                        blogID: doc.data().blogID,
                        blogHTML: doc.data().blogHTML,
                        blogCoverPhoto: doc.data().blogCoverPhoto,
                        blogTitle: doc.data().blogTitle,
                        blogDate: doc.data().date,
                        blogCoverPhotoName: doc.data().blogCoverPhotoName,
                    };
                    state.blogPosts.push(data);
                }
            });
            state.postLoaded = true;
        },
        async updatePost({commit, dispatch}, payload) {
            commit("filterBlogPost", payload);
            await dispatch("getPost");
        },
        async deletePost({commit}, payload) {
            const getPost = await db.collection('blogPosts').doc(payload);
            await getPost.delete();
            commit("filterBlogPost", payload);
        },
        async logoutUserSession({commit}) {
            await axios({
                method: 'POST',
                url: 'http://localhost:8085/auth/logout',
                withCredentials: true,
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log(response);
                console.log("logged out");
                commit("logout");
            })
            .catch((err) => {
                console.log(err)
            })
        },
        async loginUser({commit}, user) {
            await axios({
                method: 'POST',
                url: 'http://localhost:8085/auth/login',
                withCredentials: true,
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:5000',
                    'Content-Type': 'application/json'
                    //'Authorization': 'Bearer ' + localStorage.getItem('user')
                },
                data: {
                    email: user.email,
                    password: user.password
                }
            })
            .then((response) => {
                commit('loginSuccess');
                console.log(response)
                return;
            }).catch((err) => {
                this.error = true;
                this.errorMessage = err.response.data.message;
                //console.log(err.response.data.message);
                //console.log("login fail")
                commit('loginFailure');
                return;
            })
        },
        async registerUser(user) {
            await axios({
                method: 'POST',
                url: 'http://localhost:8085/auth/register',
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                    'Content-Type': 'application/json'
                },
                data: {
                    firstName: user.firstName,
                    lastName: user.lastName,
                    email: user.email,
                    password: user.password
                }
            })
            .then((response) => {
                console.log(response);
                //localStorage.setItem("user", JSON.stringify(response.data.token));
                /*const details = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email
                }*/
                //this.$store.commit('setUserDetails', details);
                //this.$router.push({ name: 'Home' });
                return;
            })
            .catch((err) => {
                console.error(err)
                this.error = true;
                //this.errorMessage = JSON.stringify(err.response.data.message);
                console.log(err.response.data.message);
                return;
            });
        },
        async getUserToken({state}) {
            await axios({
                method: 'POST',
                url: '/api/users/get-user-token',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    userId: state.profileUserId,
                    firstName: state.profileFirstName,
                    lastName: state.profileLastName,
                    email: state.profileEmail,
                },
            })
            .then((response) => {
                const token = response.data.token;
                localStorage.setItem("user", token);
                const user = JSON.stringify(localStorage.getItem('user'));
                console.log(user)
            })
            .catch((err) => {
                console.log(err);
                console.log("Token not set");
            })
        },
        async getUserClasses({state}) {
            state.classes = [];
            await axios({
                method: 'GET',
                url: '/api/classes',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userClass) => {
                    const newClass = {
                        classId: userClass.classId,
                        class_name: userClass.class_name,
                        class_created_on: userClass.class_created_on,
                        userId: userClass.userId
                    }
                    state.classes.push(newClass);
                });
                //console.log(state.classes);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async getUserClassDecks({state}, classId) {
            console.log(classId)
            state.decks = [];
            await axios({
                method: 'GET',
                url: `/api/classes/${classId}/decks`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userClassDeck) => {
                    const newDeck = {
                        deckId: userClassDeck.deckId,
                        classId: userClassDeck.classId,
                        userId: userClassDeck.userId,
                        deck_name: userClassDeck.deck_name,
                        deck_created_on: userClassDeck.deck_created_on,
                    }
                    state.decks.push(newDeck);
                });
                //console.log(state.classes);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async getUserClassDeckFlashcards({state}, payload) {
            console.log("classId = " + payload.classId)
            console.log("deckId = " + payload.deckId)
            state.flashcards = [];
            await axios({
                method: 'GET',
                url: `/api/classes/${payload.classId}/decks/${payload.deckId}/flashcards`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userClassDeckFlashcard) => {
                    const newFlashcard = {
                        flashcardId: userClassDeckFlashcard.flashcardId,
                        deckId: userClassDeckFlashcard.deckId,
                        classId: userClassDeckFlashcard.classId,
                        userId: userClassDeckFlashcard.userId,
                        question: userClassDeckFlashcard.question,
                        answer: userClassDeckFlashcard.answer,
                        flashcard_created_on: userClassDeckFlashcard.flashcard_created_on,
                    }
                    state.flashcards.push(newFlashcard);
                });
                //console.log(state.classes);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async createNewClass(newClass) {
            await axios({
                method: 'POST',
                url: "/api/classes",
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    className: newClass.className,
                    class_created_on: Date.now()
                }
            })
            .then((response) => {
                console.log(response)
                console.log("Class created");
                return 1;
            })
            .catch((err) => {
                console.log(err);
                console.log("Class not created");
                return 0;
            })
        },
        async deleteClass(classId) {
            console.log("Payload from store: " + classId)
            await axios({
                method: 'DELETE',
                url: `/api/classes/${classId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log(response)
                console.log("Class deleted");

            })
            .catch((err) => {
                console.log(err);
                console.log("Class not deleted");
            })

        },
    },
    modules: {
    }
})
