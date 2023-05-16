<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for landing or non-logged in home page
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="home">
        <BlogPost :post="welcomeScreen" />
        <BlogPost :post="aboutScreen" /> 
        <!--<div class="blog-card-wrap">
            <div class="container">
                <h3>View More Recent Blogs</h3>
                <div class="blog-cards">
                    <BlogCard :post="post" v-for="(post, index) in blogPostsCards" :key="index" /> 
                </div>
            </div>
        </div> -->
        <div v-if="!user" class="updates">
            <div class="container">
                <h2>Improve your studying! Register for your free account today!</h2>
                <router-link class="router-button" :to="{ name: 'Register' }">
                    Register for Limestudy <Arrow class="arrow arrow-light" />
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import BlogPost from '../components/BlogPost';
import Arrow from "../assets/Icons/arrow-right-light.svg";

export default {
    name: "Landing",
    components: { BlogPost, Arrow },
    data() {
        return {
            welcomeScreen: {
                title: "Welcome!",
                statement: "Limestudy is an all-encompassing study application that intends to make studying more efficient.",
                welcomeScreen: true,
                photo: "desk-study",
            },
            aboutScreen: {
                title: "About Us",
                statement: "Limestudy utilizes many different techniques to improve the efficiency of studying.",
                welcomeScreen: false,
                photo: "laptop-study"
            }
        };
    },
    computed: {
        user() {
            return this.$store.state.user;
        },
    },
    created() {
        /*if (localStorage.getItem("user"))
        {
            console.log(localStorage.getItem("user"));
            console.log(this.$store.state.loggedIn);
            this.$router.push({name: 'ViewClasses'});
        }
        console.log(this.$store.state.loggedIn);
        */
    },
};
</script>

<style lang="scss" scoped>
.blog-card-wrap
{
    h3
    {
        font-weight: 300;
        font-size: 28px;
        margin-bottom: 32px;
    }
}

.updates
{
    .container
    {
        padding: 100px 25px;
        display: flex;
        flex-direction: column;
        align-items: center;
        @media (min-width: 800px)
        {
            padding: 125px 25px;
            flex-direction: row;
        }

        .router-button
        {
            display: flex;
            font-size: 14px;
            text-decoration: none;
            @media (min-width: 800px)
            {
                margin-left: auto;
            }
        }

        h2
        {
            font-weight: 300;
            font-size: 32px;
            max-width: 425px;
            width: 100%;
            text-align: center;
            text-transform: uppercase;
            @media (min-width: 800px)
            {
                text-align: initial;
                font-size: 40px;
            }
        }
    }
}
</style>