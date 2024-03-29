/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Frontend 
Contains state of frontend application
*/

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
        notes: [],
        editNote: null,
        events: [],
        editEvent: null,

        user: null,
        loggedIn: false,
        profileAdmin: null,
        profileEmail: null,
        profileFirstName: null,
        profileLastName: null,
        profileUserId: null,
        profileInitials: null,
        profileAccountCreatedOn: null,
        profileFlashcardDelaySetting: null,
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
        toggleEditNote(state, payload) {
            state.editNote = payload;
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
            state.profileAccountCreatedOn = details.accountCreatedOn;
            state.profileFlashcardDelaySetting = details.flashcard_delay_setting;
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
                    userId: response.data.userId,
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
                return;
            })
            .catch((err) => {
                console.log(err);
                return;
            })
        },
        async loginUser({commit, dispatch}, user) {
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
                console.log(response);
                console.log('login success');
                commit('loginSuccess');
                dispatch("getUser");
                return;
            }).catch((err) => {
                console.log(err);
                //this.error = true;
                //this.errorMessage = err.response.data.message;
                //console.log(err.response.data.message);
                console.log("login fail")
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
            })
            .catch((err) => {
                console.error(err)
                this.error = true;
                //this.errorMessage = JSON.stringify(err.response.data.message);
                console.log(err.response.data.message);
            });
        },
        async getUserToken({state}) {
            console.log(state.blogHTML);
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
                    accountCreatedOn: state.profileAccountCreatedOn,
                    flashcard_delay_setting: state.profileFlashcardDelaySetting
                },
            })
            .then((response) => {
                const token = response.data.token;
                localStorage.setItem("user", token);
                const user = JSON.stringify(localStorage.getItem('user'));
                console.log(user)
                return;
            })
            .catch((err) => {
                console.log(err);
                console.log("Token not set");
                return;
            })
        },
        async getUser({commit, dispatch}) {
            await axios({
                method: 'GET',
                url: 'http://localhost:8085/user',
                withCredentials: true,
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8000/',
                    'Content-Type': 'application/json'
                    //'Authorization': 'Bearer ' + localStorage.getItem('user')
                },
            })
            .then((response) => {
                console.log(response)
                const data = {
                    userId: response.data.user_id,
                    firstName: response.data.first_name,
                    lastName: response.data.last_name,
                    email: response.data.email,
                    accountCreatedOn: response.data.account_created_on,
                    flashcard_delay_setting: response.data.flashcard_delay_setting
                }
                commit("setUserDetails", data);
                commit('setProfileInitials');
                dispatch("getUserToken");
                return;
            })
            .catch((err) => {
                console.log(err)
                return;
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
                //console.log(state.decks);
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
                console.log(response.data);
                response.data.forEach((userClassDeckFlashcard) => {
                    const newFlashcard = {
                        flashcardId: userClassDeckFlashcard.flashcardId,
                        deckId: userClassDeckFlashcard.deckId,
                        classId: userClassDeckFlashcard.classId,
                        userId: userClassDeckFlashcard.userId,
                        question: userClassDeckFlashcard.question,
                        answer: userClassDeckFlashcard.answer,
                        flashcard_created_on: userClassDeckFlashcard.flashcard_created_on,
                        correct: userClassDeckFlashcard.correct,
                        incorrect: userClassDeckFlashcard.incorrect,
                        last_studied_on: userClassDeckFlashcard.last_studied_on,
                        occurrence_rate: userClassDeckFlashcard.occurrence_rate,
                        occurrence_rate_input: userClassDeckFlashcard.occurrence_rate_input
                    }
                    state.flashcards.push(newFlashcard);
                });
                console.log(state.flashcards);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async getUserClassNotes({state}, classId) {
            //console.log(classId)
            state.notes = [];
            await axios({
                method: 'GET',
                url: `/api/notes/classes/${classId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userClassNote) => {
                    const newNote = {
                        noteId: userClassNote.noteId,
                        classId: userClassNote.classId,
                        userId: userClassNote.userId,
                        note_name: userClassNote.note_name,
                        note_body: userClassNote.note_body,
                        note_created_on: userClassNote.note_created_on,
                    }
                    state.notes.push(newNote);
                });
                console.log(state.notes);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async getAllUserNotes({state}) {
            //console.log(classId)
            state.notes = [];
            await axios({
                method: 'GET',
                url: `/api/notes`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userClassNote) => {
                    const newNote = {
                        noteId: userClassNote.noteId,
                        classId: userClassNote.classId,
                        userId: userClassNote.userId,
                        note_name: userClassNote.note_name,
                        note_body: userClassNote.note_body,
                        note_created_on: userClassNote.note_created_on,
                    }
                    state.notes.push(newNote);
                });
                console.log(state.notes);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async getAllUserEvents({state}) {
            //console.log(classId)
            state.events = [];
            await axios({
                method: 'GET',
                url: `/api/events`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                //console.log(response.data);
                response.data.forEach((userEvent) => {
                    const newEvent = {
                        eventId: userEvent.eventId,
                        userId: userEvent.userId,
                        event_date: userEvent.event_date,
                        event_description: userEvent.event_description,
                        event_created_on: userEvent.event_created_on,
                    }
                    state.events.push(newEvent);
                });
                console.log(state.events);
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async updateUserSession({state}, payload) {
            console.log(state.blogTitle)
            await axios({
                method: 'PUT',
                url: `/api/classes/${payload.session.classId}/decks/${payload.session.deckId}/flashcards/${payload.session.flashcardId}/sessions/${payload.session.sessionId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    sessionId: payload.session.sessionId,
                    flashcardId: payload.session.flashcardId,
                    deckId: payload.session.deckId,
                    classId: payload.session.classId,
                    userId: payload.session.userId,
                    session_date: payload.session.session_date,
                    session_correct: payload.session.session_correct,
                    session_incorrect: payload.session.session_incorrect
                }
            })
            .then(() => {
                console.log("response starts here")
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async createUserSession({state}, payload) {
            console.log(state.blogTitle)
            await axios({
                method: 'POST',
                url: `/api/classes/${payload.flashcard.classId}/decks/${payload.flashcard.deckId}/flashcards/${payload.flashcard.flashcardId}/sessions`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    session_date: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                    session_correct: payload.answer.correct,
                    session_incorrect: payload.answer.incorrect
                }
            })
            .then(() => {
                console.log("response starts here")
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
        async updateUserFlashcard({state}, payload) {
            console.log(state.blogTitle);
            console.log(payload.flashcard.occurrence_rate_input);
            await axios({
                method: 'PUT',
                url: `/api/classes/${payload.flashcard.classId}/decks/${payload.flashcard.deckId}/flashcards/${payload.flashcard.flashcardId}`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
                data: {
                    question: payload.flashcard.question,
                    answer: payload.flashcard.answer,
                    flashcard_created_on: payload.flashcard.flashcard_created_on,
                    userId: payload.flashcard.userId,
                    classId: payload.flashcard.classId,
                    deckId: payload.flashcard.deckId,
                    flashcardId: payload.flashcard.flashcardId,
                    correct: payload.flashcard.correct,
                    incorrect: payload.flashcard.incorrect,
                    last_studied_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                    occurrence_rate: payload.flashcard.occurrence_rate,
                    occurrence_rate_input: payload.flashcard.occurrence_rate_input,
                }
            })
            .then(() => {
                console.log("response starts here")
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                return;
            });
        },
    },
    modules: {
    }
})
