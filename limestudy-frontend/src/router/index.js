import Vue from "vue";
import VueRouter from "vue-router";
import firebase from "firebase/app";
import "firebase/auth";

// general
import Landing from "../views/Landing.vue";
import About from "../views/About.vue";

// auth
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import ForgotPassword from "../views/ForgotPassword.vue";

// other user pages
import Dashboard from "../views/Dashboard.vue";
import Calendar from "../views/Calendar.vue";
import ViewStatistics from "../views/ViewStatistics.vue";

// classes
import ViewClasses from "../views/ViewClasses.vue"; // read classes
import CreateNewClass from "../views/CreateNewClass.vue"; // create classes 
import UpdateClass from "../views/UpdateClass.vue"; // update class
// delete class is done inside ClassCard.vue component

// decks
import ViewDecks from "../views/ViewDecks.vue"; // read decks
import CreateNewDeck from "../views/CreateNewDeck.vue"; // create deck
import UpdateDeck from "../views/UpdateDeck.vue"; // update deck
// delete deck

// flashcards
import ViewFlashcards from "../views/ViewFlashcards.vue"; // read flashcards
import CreateNewFlashcard from "../views/CreateNewFlashcard.vue"; // create flashcards
import UpdateFlashcard from "../views/UpdateFlashcard.vue" // update flashcard
// delete flashcard
import TraditionalStudy from "../views/TraditionalStudy.vue" // traditional study
import MultiChoiceStudy from "../views/MultiChoiceStudy.vue" // multiple choice study

// notes
import CreateNewNote from "../views/CreateNewNote.vue" // create note
import ReadNote from "../views/ReadNote.vue"
import ViewNotes from "../views/ViewNotes.vue"
 

import Profile from "../views/Profile.vue";
import Admin from "../views/Admin.vue";
import CreatePost from "../views/CreatePost.vue";
import BlogPreview from "../views/BlogPreview.vue";
import ViewBlog from "../views/ViewBlog.vue";
import EditBlog from "../views/EditBlog.vue";




Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Landing",
    component: Landing,
    meta: {
      title: 'Landing',
      requiresAuth: false,
    }
  },
  {
    path: "/about-us",
    name: "About",
    component: About,
    meta: {
      title: 'About',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
    meta: {
      title: 'Dashboard',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/calendar",
    name: "Calendar",
    component: Calendar,
    meta: {
      title: 'Calendar',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/statistics",
    name: "ViewStatistics",
    component: ViewStatistics,
    meta: {
      title: 'Statistics',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes",
    name: "ViewClasses",
    component: ViewClasses,
    meta: {
      title: 'View Classes',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/create-new-class",
    name: "CreateNewClass",
    component: CreateNewClass,
    meta: {
      title: 'Create New Class',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/update-class",
    name: "UpdateClass",
    component: UpdateClass,
    meta: {
      title: 'Update Class',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks",
    name: "ViewDecks",
    component: ViewDecks,
    meta: {
      title: 'View Decks',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/create-new-deck",
    name: "CreateNewDeck",
    component: CreateNewDeck,
    meta: {
      title: 'Create New Deck',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/update-deck",
    name: "UpdateDeck",
    component: UpdateDeck,
    meta: {
      title: 'Update Deck',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/flashcards",
    name: "ViewFlashcards",
    component: ViewFlashcards,
    meta: {
      title: 'View Flashcards',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/flashcards/create-new-flashcard",
    name: "CreateNewFlashcard",
    component: CreateNewFlashcard,
    meta: {
      title: 'Create New Flashcard',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/flashcards/:flashcardId/update-flashcard",
    name: "UpdateFlashcard",
    component: UpdateFlashcard,
    meta: {
      title: 'Update Flashcard',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/flashcards/traditional-study",
    name: "TraditionalStudy",
    component: TraditionalStudy,
    meta: {
      title: 'Traditional Study',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/classes/:classId/decks/:deckId/flashcards/multi-choice-study",
    name: "MultiChoiceStudy",
    component: MultiChoiceStudy,
    meta: {
      title: 'Multiple Choice Study',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/notes/create-new-note",
    name: "CreateNewNote",
    component: CreateNewNote,
    meta: {
      title: 'Create New Note',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/notes/:noteId",
    name: "ReadNote",
    component: ReadNote,
    meta: {
      title: 'Study Note',
      requiresAuth: false,
    }
  },
  {
    path: "/dashboard/notes",
    name: "ViewNotes",
    component: ViewNotes,
    meta: {
      title: 'View All Notes',
      requiresAuth: false,
    }
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      title: 'Login',
      requiresAuth: false,
    }
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: {
      title: 'Register',
      requiresAuth: false,
    }
  },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword,
    meta: {
      title: 'Forgot Password',
      requiresAuth: false,
    }
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
    meta: {
      title: 'Profile',
      requiresAuth: false,
    }
  },
  {
    path: "/admin",
    name: "Admin",
    component: Admin,
    meta: {
      title: 'Admin',
      requiresAuth: false,
    }
  },
  {
    path: "/create-post",
    name: "CreatePost",
    component: CreatePost,
    meta: {
      title: 'Create Post',
      requiresAuth: false,
    }
  },
  {
    path: "/post-preview",
    name: "BlogPreview",
    component: BlogPreview,
    meta: {
      title: 'Preview Blog Post',
      requiresAuth: false,
    }
  },
  {
    path: "/view-blog/:blogid",
    name: "ViewBlog",
    component: ViewBlog,
    meta: {
      title: 'View Blog Post',
      requiresAuth: false,
    }
  },
  {
    path: "/edit-blog/:blogid",
    name: "EditBlog",
    component: EditBlog,
    meta: {
      title: 'Edit Blog Post',
      requiresAuth: true,
    }
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title} | Limestudy`;
  next();
});

router.beforeEach(async (to, from, next) => {
  let user = firebase.auth().currentUser;
  let admin = null;
  if (user) 
  {
    let token = await user.getIdTokenResult();
    admin = token.claims.admin;
  }
  if (to.matched.some((res) => res.meta.requiresAuth))
  {
    if (user) 
    {
      if (to.matched.some((res) => res.meta.requiresAdmin))
      {
        if (admin)
        {
          return next();
        }
        return next({name: "Home"});
      }
      return next();
    }
    return next({name: "Home"});
  }
  return next();
});

export default router;
