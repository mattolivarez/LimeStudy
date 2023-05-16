<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View for registration page
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="form-wrap">
        <Loading v-show="loading" />
        <form class="register">
            <p class="login-register">
                Already have an account?
                <router-link class="router-link" :to="{ name: 'Login' }">Login</router-link>
            </p>
            <h2>Create your Limestudy Account</h2>
            <div class="inputs">
                <div class="input">
                    <input type="text" placeholder="First Name" v-model="firstName" required>
                    <User class="icon" />
                </div>
                <div class="input">
                    <input type="text" placeholder="Last Name" v-model="lastName" required>
                    <User class="icon" />
                </div>
                <div class="input">
                    <input type="text" placeholder="Email" v-model="email" required>
                    <Email class="icon" />
                </div>
                <div class="input">
                    <input type="password" placeholder="Password" v-model="password" required>
                    <Password class="icon" />
                </div>
                <div class="error" v-show="error">
                    {{ this.errorMessage }}
                </div>
            </div>
            <button @click.prevent="register">Sign Up</button>
            <div class="angle"></div>
        </form>
        <div class="background"></div>
    </div>
</template>

<script>
import Email from "../assets/Icons/envelope-regular.svg";
import Password from "../assets/Icons/lock-alt-solid.svg";
import User from "../assets/Icons/user-alt-light.svg";
import axios from "axios";
import Loading from "../components/Loading";

export default {
    name: "Register",
    components: { 
        Email, 
        Password, 
        User,
        Loading,
    },
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            error: null,
            errorMessage: "",
            loading: false,
        };
    },
    methods: {
        async register(){
            if (this.email !== "" && this.password !== "" && this.firstName !== "" && this.lastName !== "")
            {
                this.loading = true;
                await axios({
                    method: 'POST',
                    url: 'http://localhost:8085/auth/register',
                    headers: {
                        //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                        'Content-Type': 'application/json'
                    },
                    data: {
                        first_name: this.firstName,
                        last_name: this.lastName,
                        email: this.email,
                        password: this.password,
                        account_created_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                        flashcard_delay_setting: 3
                    }
                })
                .then((response) => {
                    this.error = false;
                    this.errorMessage = "";
                    console.log(response);
                    this.$store.dispatch("loginUser", {email: this.email, password: this.password});
                    //this.$store.dispatch("getUserToken");
                    this.$router.push({ name: 'Dashboard' });
                    this.loading = false;
                })
                .catch((err) => {
                    console.error(err)
                    this.error = true;
                    this.errorMessage = JSON.stringify(err.response.data.message);
                    console.log(err.response.data.message);
                });
                this.loading = false;
            }
            else
            {
                this.error = true;
                this.errorMessage = "Please fill out all the fields!";
                return;
            }
        },
    },
}
</script>


<style lang="scss" scoped>
.register
{
    h2
    {
        max-width: 350px;
    }
}
</style>