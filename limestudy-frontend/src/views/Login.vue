<template>
    <div class="form-wrap">
        <form class="login">
            <p class="login-register">
                Don't have an account?
                <router-link class="router-link" :to="{ name: 'Register' }">Register</router-link>
            </p>
            <h2>Login to Limestudy</h2>
            <div class="inputs">
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
            <router-link class="forgot-password" :to="{ name: 'ForgotPassword' }">Forgot your password?</router-link>
            <button @click.prevent="signIn">Sign In</button>
            <div class="angle"></div>
        </form>
        <div class="background"></div>
    </div>
</template>

<script>
import Email from "../assets/Icons/envelope-regular.svg"
import Password from "../assets/Icons/lock-alt-solid.svg"
import axios from "axios"

export default {
    name: "Login",
    components: {
        Email,
        Password,
    },
    data() {
        return {
            email: "",
            password: "",
            error: null,
            errorMessage: "",
        };
    },
    methods: {
        /*signIn() {
            firebase
            .auth()
            .signInWithEmailAndPassword(this.email, this. password)
            .then(() => {
                this.$router.push({ name: 'Home' });
                this.error = false;
                this.errorMessage = "";
                console.log(firebase.auth().currentUser.uid);
            }).catch(err => {
                this.error = true;
                this.errorMessage = err.message;
            });
        }*/
        /*async signIn() {
            if (this.email !== "" && this.password !== "")
            {
                await axios({
                    method: 'POST',
                    url: '/api/users/login',
                    headers: {
                        //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                        'Content-Type': 'application/json'
                        //'Authorization': 'Bearer ' + localStorage.getItem('user')
                    },
                    data: {
                        email: this.email,
                        password: this.password
                    }
                })
                .then((response) => {
                    console.log(response)
                    this.error = false;
                    this.errorMessage = "";
                    const token = response.data.token;
                    localStorage.setItem("user", token);
                    //const user = JSON.stringify(localStorage.getItem('user'));
                    //console.log(user);
                    this.$store.dispatch("getCurrentUser", {email: this.email, password: this.password});
                    this.$store.commit('loginSuccess', token);
                    this.$router.push({ name: 'ViewClasses' });
                    return;
                }).catch((err) => {
                    this.error = true;
                    this.errorMessage = err.response.data.message;
                    //console.log(err.response.data.message);
                    this.$store.commit('loginFailure');
                    return;
                });
            }
            else
            {
                this.error = true;
                this.errorMessage = "Please fill out all the fields!";
                return;
            }
        }*/
        async signIn() {
            if (this.email !== "" && this.password !== "")
            {
                await axios({
                    method: 'POST',
                    url: 'http://localhost:5000/auth/login',
                    withCredentials: true,
                    headers: {
                        //'Access-Control-Allow-Origin': 'http://localhost:5000',
                        'Content-Type': 'application/json'
                        //'Authorization': 'Bearer ' + localStorage.getItem('user')
                    },
                    data: {
                        email: this.email,
                        password: this.password
                    }
                })
                .then((response) => {
                    this.error = false;
                    this.errorMessage = "";
                    this.$store.commit('loginSuccess');
                    console.log(response)
                    
                }).catch((err) => {
                    this.error = true;
                    this.errorMessage = err.response.data.message;
                    //console.log(err.response.data.message);
                    //console.log("login fail")
                    this.$store.commit('loginFailure');
                })
                await axios({
                    method: 'GET',
                    url: 'http://localhost:5000/user',
                    withCredentials: true,
                    headers: {
                        //'Access-Control-Allow-Origin': 'http://localhost:5000',
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
                    }
                    this.$store.state.loggedIn = true
                    this.$store.commit("setUserDetails", data);
                    this.$store.commit('setProfileInitials');
                })
                .catch((err) => {
                    console.log(err)
                })
                await axios({
                    method: 'POST',
                    url: '/api/users/get-user-token',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: {
                        userId: this.$store.state.profileUserId,
                        firstName: this.$store.state.profileFirstName,
                        lastName: this.$store.state.profileLastName,
                        email: this.$store.state.profileEmail,
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
                this.$router.push({ name: 'ViewClasses' });
            }
            else
            {
                this.error = true;
                this.errorMessage = "Please fill out all the fields!";
                return;
            }
        },
    }
};
</script>


<style lang="scss">
.form-wrap
{
    overflow: hidden;
    display: flex;
    height: 100vh;
    justify-content: center;
    align-self: center;
    margin: 0 auto;
    width: 90%;
    @media (min-width: 900px)
    {
        width: 100%;
    }

    .login-register
    {
        margin-bottom: 32px;

        .router-link
        {
            color: #000;
        }
    }

    form
    {
        padding: 0 10px;
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        flex: 1;
        @media (min-width: 900px)
        {
            padding: 0 50px;
        }

        h2
        {
            text-align: center;
            font-size: 32px;
            color: #303030;
            margin-bottom: 40px;
            @media (min-width: 900px)
            {
                font-size: 40px;
            }
        }

        .inputs
        {
            width: 100%;
            max-width: 350px;

            .input
            {
                position: relative;
                display: flex;
                justify-content: center;
                align-items: center;
                margin-bottom: 8px;
                input
                {
                    width: 100%;
                    border: none;
                    background-color: #f2f7f6;
                    padding: 4px 4px 4px 30px;
                    height: 50px;

                    &:focus
                    {
                        outline: none;
                    }
                }

                .icon
                {
                    width: 12px;
                    position: absolute;
                    left: 6px;
                }
            }
        }

        .forgot-password
        {
            text-decoration: none;
            color: #000;
            cursor: pointer;
            font-size: 14px;
            margin: 16px 0 32px;
            border-bottom: 1px solid transparent;
            transition: .5s ease all;

            &:hover
            {
                border-color: #303030;
            }
        }

        .angle
        {
            display: none;
            position: absolute;
            background-color: #fff;
            transform: rotateZ(3deg);
            width: 60px;
            right: -30px;
            height: 101%;
            @media (min-width: 900px)
            {
                display: initial;
            }
        }
    }

    .background
    {
        display: none;
        flex: 2;
        background-size: cover;
        background-image: url("../assets/backpack-study.jpg");
        width: 100%;
        height: 100%;
        @media (min-width: 900px)
        {
            display: initial;
        }
    }
}
</style>