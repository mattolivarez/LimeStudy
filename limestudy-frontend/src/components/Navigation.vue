<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    Component for Navigation, which is navigation bar across the top of the screen
    Contains template (HTML), CSS, and JavaScript
    -->
    <header>
        <nav class="container">
            <div class="branding">
                <router-link class="header" :to="{ name: 'Landing' }">
                    <Lime class="lime" /> 
                    Limestudy
                </router-link>
            </div>
            <div class="nav-links">
                <ul v-show="!mobile">
                    <router-link v-if="!loggedIn" class="link" :to="{ name: 'Landing' }">Home</router-link>
                    <router-link v-if="!loggedIn" class="link" :to="{ name: 'About' }">About</router-link>
                    <router-link v-if="loggedIn" class="link" :to="{ name: 'Dashboard' }">Dashboard</router-link>
                    <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewClasses' }">Classes</router-link>
                    <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewNotes' }">Notes</router-link>
                    <router-link v-if="loggedIn" class="link" :to="{ name: 'Calendar' }">Calendar</router-link>
                    <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewStatistics' }">Statistics</router-link>
                    <!-- <router-link v-if="loggedIn" class="link" :to="{ name: 'CreatePost' }">Create New Class</router-link> -->
                    <!-- <router-link v-if="this.$store.state.flashcardsLoaded" class="link" to="#">Study</router-link> -->
                    <router-link v-if="!loggedIn" class="link" :to="{ name: 'Login' }">Login/Register</router-link>
                </ul>
                <div @click="toggleProfileMenu" class="profile" ref="profile" v-if="loggedIn">
                    <span>{{ this.$store.state.profileInitials }}</span>
                    <div class="profile-menu" v-show="profileMenu">
                        <div class="info">
                            <p class="initials">{{ this.$store.state.profileInitials }}</p>
                            <div class="right">
                                <p>{{ this.$store.state.profileFirstName }} {{ this.$store.state.profileLastName }}</p>
                                <p>{{ this.$store.state.profileUsername }}</p>
                                <p>{{ this.$store.state.profileEmail }}</p>
                            </div>
                        </div>
                        <div class="options">
                            <div class="option">
                                <router-link class="option" :to="{name: 'Profile'}">
                                    <userIcon class="icon" />
                                    <p>Profile</p>
                                </router-link>
                            </div>
                            <div v-if="admin" class="option">
                                <router-link class="option" :to="{name: 'Admin'}">
                                    <adminIcon class="icon" />
                                    <p>Admin</p>
                                </router-link>
                            </div>
                            <div class="option" @click="signOut">
                                <signOutIcon class="icon" />
                                <p>Sign Out</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
        <menuIcon @click="toggleMobileNav" class="menu-icon" v-show="mobile" />
        <transition name="mobile-nav">
            <ul class="mobile-nav" v-show="mobileNav">
                <router-link v-if="!loggedIn" class="link" :to="{ name: 'Landing' }">Home</router-link>
                <router-link v-if="!loggedIn" class="link" :to="{ name: 'About' }">About</router-link>
                <router-link v-if="loggedIn" class="link" :to="{ name: 'Dashboard' }">Dashboard</router-link>
                <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewClasses' }">Classes</router-link>
                <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewNotes' }">Notes</router-link>
                <router-link v-if="loggedIn" class="link" :to="{ name: 'Calendar' }">Calendar</router-link>
                <router-link v-if="loggedIn" class="link" :to="{ name: 'ViewStatistics' }">Statistics</router-link>
                <!-- <router-link v-if="loggedIn" class="link" :to="{ name: 'CreatePost' }">Create New Class</router-link> -->
                <!-- <router-link v-if="this.$store.state.flashcardsLoaded" class="link" to="#">Study</router-link> -->
                <router-link v-if="!loggedIn" class="link" :to="{ name: 'Login' }">Login/Register</router-link>
            </ul>
        </transition>
    </header>
</template>

<script>
import menuIcon from '../assets/Icons/bars-regular.svg';
import userIcon from '../assets/Icons/user-alt-light.svg';
import adminIcon from '../assets/Icons/user-crown-light.svg';
import signOutIcon from '../assets/Icons/sign-out-alt-regular.svg';
import Lime from '../assets/Icons/lemon-solid.svg'
//import firebase from "firebase/app"
import "firebase/auth"
import axios from "axios"

export default {
    name: 'Navigation',
    components: {
        menuIcon,
        userIcon,
        adminIcon,
        signOutIcon,
        Lime,
    },
    async beforeMount() {
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
            this.$store.state.loggedIn = true
            this.$store.commit("setUserDetails", data);
            this.$store.commit('setProfileInitials');
        })
        .catch((err) => {
            console.log(err)
        })
        // if (this.$store.state.loggedIn)
        // {
        //     this.$router.push({name: "ViewClasses"})
        // }
    },
    data() {
        return {
            mobile: null,
            mobileNav: null,
            windowWidth: null,
            profileMenu: null,
        }
    },
    created() {
        window.addEventListener('resize',this.checkScreen);
        this.checkScreen();
    },
    methods: {
        checkScreen() 
        {
            this.windowWidth = window.innerWidth;
            if (this.windowWidth <= 750)
            {
                this.mobile = true;
                return;
            }
            this.mobile = false;
            this.mobileNav = false;
            return;
        },
        toggleMobileNav()
        {
            this.mobileNav = !this.mobileNav;
        },
        toggleProfileMenu(e)
        {
            if (e.target === this.$refs.profile)
            {
                this.profileMenu = !this.profileMenu;
            }   
        },
        signOut() 
        {
            localStorage.removeItem('user');
            this.$store.dispatch("logoutUserSession");
            this.$router.push({name: "Landing"});
        },
    },
    computed: {
        user() {
            return this.$store.state.user;
        },
        admin() {
            return this.$store.state.profileAdmin;
        },
        loggedIn() {
            return this.$store.state.loggedIn;
        }
    },
};
</script>

<style lang="scss" scoped>
header
{
    background-color: #ffff;
    padding: 0 25px;
    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.6);
    z-index: 99;

    .link
    {
        font-weight: 500;
        padding: 0 8px;
        transition: .3s color ease;

        &:hover
        {
            color: var(--lime);
        }
    }
    nav
    {
        display: flex;
        padding: 25px 0;

        .branding
        {
            display: flex;
            align-items: center;

            .header
            {
                font-weight: 600;
                font-size: 24px;
                color: black;
                text-decoration: none;
            }
        }

        .nav-links
        {
            position: relative;
            display: flex;
            flex: 1;
            align-items: center;
            justify-content: flex-end;

            ul
            {
                margin-right: 32px;

                .link
                {
                    margin-right: 32px;
                }

                .link:last-child
                {
                    margin-right: 0;
                }
            }

            .profile
            {
                position: relative;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 40px;
                height: 40px;
                border-radius: 50%;
                color: #fff;
                background-color: #303030;

                span
                {
                    pointer-events: none;
                }

                .profile-menu
                {
                    position: absolute;
                    top: 60px;
                    right: 0;
                    width: 250px;
                    background-color: #303030;
                    box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.6);

                    .info
                    {
                        display: flex;
                        align-items: center;
                        padding: 15px;
                        border-bottom: 1px solid #fff;

                        .initials
                        {
                            position: initial;
                            width: 40px;
                            height: 40px;
                            background-color: #fff;
                            color: #303030;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            border-radius: 50%;
                        }

                        .right
                        {
                            flex: 1;
                            margin-left: 12px; /* changed from 24px */

                            p:nth-child(1)
                            {
                                font-size: 14px;
                            }

                            p:nth-child(2),
                            p:nth-child(3)
                            {
                                font-size: 12px;
                            }

                        }
                    }

                    .options
                    {
                        padding: 15px;

                        .option
                        {
                            text-decoration: none;
                            color: #fff;
                            display: flex;
                            align-items: center;
                            margin-bottom: 12px;

                            .icon
                            {
                                width: 18px;
                                height: auto;
                            }

                            p
                            {
                                font-size: 14px;
                                margin-left: 12px;
                            }

                            &:last-child
                            {
                                margin-bottom: 0px;
                            }
                        }
                    }
                }
            }
        }
    }
    .menu-icon
    {
        cursor: pointer;
        position: absolute;
        top: 32px;
        right: 25px;
        height: 25px;
        width: auto;
    }

    .mobile-nav
    {
        padding: 20px;
        width: 70%;
        max-width: 250px;
        display: flex; /* or flexbox */
        flex-direction: column;
        position: fixed;
        height: 100%;
        background-color: #303030;
        top: 0;
        left: 0;

        .link
        {
            padding: 15px 0;
            color: #fff;
        }
    }

    .mobile-nav-enter-active,
    .mobile-nav-leave-active
    {
        transition: all 1s ease;
    }
    .mobile-nav-enter
    {
        transform: translateX(-250px);
    }
    .mobile-nav-enter-to
    {
        transform: translateX(0);
    }
    .mobile-nav-leave-to
    {
        transform: translateX(-250px);
    }
}
.header
{
    display: flex;

    .lime
    {
        width: 30px;
        max-width: 25px;
        margin-right: 10px;
        fill: var(--lime);
    }
}
</style>