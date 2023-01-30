<template>
    <div class="form-wrap">
        <form class="register">
            <p class="login-register">
                Already have an account?
                <router-link class="router-link" :to="{ name: 'Login' }">Login</router-link>
            </p>
            <h2>Create your Limestudy Account</h2>
            <div class="inputs">
                <div class="input">
                    <input type="text" placeholder="First Name" v-model="firstName" required>
                    <user class="icon" />
                </div>
                <div class="input">
                    <input type="text" placeholder="Last Name" v-model="lastName" required>
                    <user class="icon" />
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
import Email from "../assets/Icons/envelope-regular.svg"
import Password from "../assets/Icons/lock-alt-solid.svg"
import axios from "axios"

export default {
    name: "Register",
    components: { 
        Email, 
        Password, 
    },
    data() {
        return {
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            error: null,
            errorMessage: "",
        };
    },
    methods: {
        /*async register() {
            if (this.email !== "" && this.password !== "" && this.firstName !== "" && this.lastName !== "" && this.username !== "")
            {
                this.error = false;
                this.errorMessage = "";
                const firebaseAuth = await firebase.auth();
                const createUser = await firebaseAuth.createUserWithEmailAndPassword(this.email, this.password);
                const result = await createUser;
                const dataBase = db.collection("users").doc(result.user.uid);
                await dataBase.set({
                    firstName: this.firstName,
                    lastName: this.lastName,
                    username: this.username,
                    email: this.email
                });
                this.$router.push({ name: 'Home' });
                return;
            }
            this.error = true;
            this.errorMessage = "Please fill out all the fields!";
            return;
        },*/
        async register(){
            /*fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                body: JSON.stringify(this.bodyData),
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                    'Content-Type': 'application/json'
                }
            })
            .then(res => res.json())
            .then(res => console.log(res))
            .catch(error => console.error(error[0].message));*/
            /*axios.post('/api/users/login', {
                email: "david@testemail.com",
                password: "test",
                headers: {
                    //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                    'Content-Type': 'application/json'
                },
            })
            .then(res => console.log(res))
            .catch(error => console.log(error.response.data.message));*/
            if (this.email !== "" && this.password !== "" && this.firstName !== "" && this.lastName !== "" && this.username !== "")
            {
                await axios({
                    method: 'POST',
                    url: 'http://localhost:8085/auth/register',
                    headers: {
                        //'Access-Control-Allow-Origin': 'http://localhost:8080/api/users/login',
                        'Content-Type': 'application/json'
                    },
                    data: {
                        firstName: this.firstName,
                        lastName: this.lastName,
                        email: this.email,
                        password: this.password
                    }
                })
                .then((response) => {
                    this.error = false;
                    this.errorMessage = "";
                    console.log(response);
                })
                .catch((err) => {
                    console.error(err)
                    this.error = true;
                    this.errorMessage = JSON.stringify(err.response.data.message);
                    console.log(err.response.data.message);
                });
                this.$store.dispatch("loginUser", {email: this.email, password: this.password});
                this.$router.push({ name: 'ViewClasses' });
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