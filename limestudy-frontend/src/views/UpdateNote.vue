<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View to update note, which contains form for specific note
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <div class="blog-info">
                <input type="text" placeholder="Enter note name..." v-model="noteName" /> <!--v-model="blogTitle"-->
                <!-- <input type="text" placeholder="Enter class id..." v-model="classId" /> -->
                <Dropdown v-model="classId" :selectedId="this.classId" />
            </div>
            <div class="editor">
                <vue-editor :editorOptions="editorSettings" v-model="noteBody" useCustomImageHandler @image-added="imageHandler" /> <!-- @image-added="imageHandler" -->
            </div>
            <div class="blog-actions">
                <button @click="updateNote">Update</button>
                <!--<router-link class="router-button" :to="{name: 'BlogPreview'}">Post Preview</router-link>-->
            </div>
        </div>
    </div>
</template>


<script>
import Quill from "quill";
import firebase from "firebase/app";
import "firebase/storage";
import BlogCoverPreview from "../components/BlogCoverPreview.vue";
import Loading from "../components/Loading";
import axios from "axios"
import Dropdown from "../components/Dropdown.vue";

window.Quill = Quill;
const ImageResize = require("quill-image-resize-module").default;
Quill.register("modules/imageResize", ImageResize);

export default {
    name: "UpdateFlashcard",
    components: {
        BlogCoverPreview,
        Loading,
        Dropdown,
    },
    data() {
        return {
            file: null,
            error: null,
            errorMessage: null,
            loading: null,
            editorSettings: {
                modules: {
                    imageResize: {},
                },
            },
            userId: null,
            classId: null,
            noteId: null,
            noteBody: "",
            noteName: "",
            noteCreatedOn: null,
            noteCreatedOnTemp: null
        };
    },
    methods: {
        imageHandler(file, Editor, cursorLocation, resetUploader) {
            const storageRef = firebase.storage().ref();
            const docRef = storageRef.child(`documents/blogPostPhoto/${file.name}`);
            docRef.put(file).on("state_changed", (snapshot) => {
                console.log(snapshot)
            }, (err) => {
                console.log(err);
            }, async () => {
                const downloadURL = await docRef.getDownloadURL();
                Editor.insertEmbed(cursorLocation, "image", downloadURL);
                resetUploader();
            });
        },
        async updateNote() {
            if (this.noteBody.length !== 0 && this.noteName.length !== 0)
            {
                this.loading = true;
                await axios({
                    method: 'PUT',
                    url: `/api/notes/${this.noteId}`,
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('user'),
                        'Content-Type': 'application/json'
                    },
                    data: {
                        note_body: this.noteBody,
                        note_name: this.noteName,
                        note_created_on: this.noteCreatedOn,
                        userId: this.userId,
                        classId: this.classId,
                        noteId: this.noteId
                    }
                })
                .then((response) => {
                    console.log(response)
                    console.log("Note updated");
                })
                .catch((err) => {
                    console.log(err);
                    console.log("Note not update");
                    //console.log(JSON.stringify(this.question))
                    //console.log(this.answer)
                })
                this.loading = false;
                //this.$router.push({name: "ViewDecks", params: {classId: this.$route.params.classId}});
                this.$router.push({name: "Landing"});
                return;
            }
            this.error = true;
            this.errorMessage = "Please ensure name and body of note are both filled out!";
            setTimeout(() => {
                this.error = false;
            }, 5000);
            return;
        },
    },
    computed: {

    },
    async created() {
        await axios({
            method: 'GET',
            url: `/api/notes/${this.$route.params.noteId}`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log(response)
            this.userId = response.data.userId;
            this.classId = response.data.classId;
            this.noteId = response.data.noteId;
            this.noteCreatedOn = response.data.note_created_on;
            this.noteName = response.data.note_name;
            this.noteBody = response.data.note_body;
        })
        .catch((err) => {
            console.log(err)
        });
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
    },
};
</script>


<style lang="scss">
.create-post
{
    position: relative;
    height: 100%;

    button
    {
        margin-top: 0;
    }

    .router-button
    {
        text-decoration: none;
        color: #fff;
    }

    label,
    button,
    .router-button
    {
        transition: .5s ease-in-out all;
        align-self: center;
        font-size: 14px;
        cursor: pointer;
        border-radius: 20px;
        padding: 12px 24px;
        color: #fff;
        background-color: #303030;
        text-decoration: none;

        &:hover
        {
            background-color: rgba(48, 48, 48, 0.7);
        }
    }

    .container
    {
        position: relative;
        height: 100%;
        padding: 10px 25px 60px;
    }

    // error styling
    .invisible
    {
        opacity: 0 !important;
    }

    .err-message
    {
        width: 100%;
        padding: 12px;
        border-radius: 8px;
        color: #fff;
        margin-bottom: 10px;
        background-color: #303030;
        opacity: 1;
        transition: .5s ease all;

        p
        {
            font-size: 14px;
        }

        span
        {
            font-weight: 600;
        }
    }

    .blog-info
    {
        display: flex;
        margin-bottom: 32px;

        input:nth-child(1)
        {
            min-width: 300px;
        }

        input
        {
            transition: .5s ease-in-out all;
            padding: 10px 4px;
            border: none;
            border-bottom: 1px solid #303030;

            &:focus
            {
                outline: none;
                box-shadow: 0 1px 0 0 #303030;
            }
        }

        .upload-file
        {
            flex: 1;
            margin-left: 16px;
            position: relative;
            display: flex;

            input
            {
                display: none;
            }

            .preview
            {
                margin-left: 16px;
                text-transform: initial;
            }

            span
            {
                font-size: 12px;
                margin-left: 16px;
                align-self: center;
            }
        }
    }

    .editor
    {
        height: 60vh;
        display: flex;
        flex-direction: column;


        .quillWrapper
        {
            position: relative;
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .ql-container
        {
            display: flex;
            flex-direction: column;
            height: 100%;
            overflow: scroll;
        }

        .ql-editor
        {
            padding: 20px 16px 30px;
        }
    }

    .blog-actions
    {
        margin-top: 32px;

        button
        {
            margin-right: 16px;
        }
    }
}
</style>