<template>
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <div class="blog-info">
                <input type="text" placeholder="Enter Flashcard Tags..." /> <!--v-model="blogTitle"-->
                <div class="upload-file">
                    <!--<label for="blog-photo">Upload Cover Photo</label>-->
                    <!--<input type="file" ref="blogPhoto" id="blog-photo" @change="fileChange" accept=".png, .jpg, .jpeg" />-->
                    <button class="preview" :class="{'button-inactive': showQuestion}" @click="changeCardSide">Question</button>
                    <button class="preview" :class="{'button-inactive': showAnswer}" @click="changeCardSide">Answer</button>
                    <!--<span>File Chosen: {{ this.$store.state.blogPhotoName }}</span>-->
                </div>
            </div>
            <div class="editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" useCustomImageHandler @image-added="imageHandler" /> <!-- @image-added="imageHandler" -->
            </div>
            <div class="editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" useCustomImageHandler @image-added="imageHandler" />
            </div>
            <div class="blog-actions">
                <button @click="createNewFlashcard">Create</button>
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

window.Quill = Quill;
const ImageResize = require("quill-image-resize-module").default;
Quill.register("modules/imageResize", ImageResize);

export default {
    name: "CreateNewFlascard",
    components: {
        BlogCoverPreview,
        Loading,
    },
    data() {
        return {
            file: null,
            error: null,
            errorMessage: null,
            loading: null,
            showQuestion: true,
            showAnswer: false,
            question: "question",
            answer: "answer",
            editorSettingsQuestion: {
                modules: {
                    imageResize: {},
                },
            },
            editorSettingsAnswer: {
                modules: {
                    imageResize: {},
                },
            },
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
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
        async createNewFlashcard() {
            if (this.question.length !== 0 && this.answer.length !== 0)
            {
                this.loading = true;
                await axios({
                    method: 'POST',
                    url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards`,
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('user'),
                        'Content-Type': 'application/json'
                    },
                    data: {
                        question: this.question,
                        answer: this.answer,
                        flashcard_created_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                        correct: 0,
                        incorrect: 0,
                        last_studied_on: new Date(Date.now()).toLocaleString('en-us', {year: "numeric", month: "2-digit", day: "2-digit"}),
                        occurrence_rate: 0.5,
                        occurrence_rate_input: 0
                    }
                })
                .then((response) => {
                    console.log(response)
                    console.log("Flashcard created");
                })
                .catch((err) => {
                    console.log(err);
                    console.log("Flashcard not created");
                    console.log(JSON.stringify(this.question))
                    console.log(this.answer)
                })
                this.loading = false;
                this.$router.push({name: "ViewFlashcards", params: {classId: this.$route.params.classId, deckId: this.$route.params.deckId}});
                return;
            }
            this.error = true;
            this.errorMessage = "Please ensure question and answer are both filled out!";
            setTimeout(() => {
                this.error = false;
            }, 5000);
            return;
        },
    },
    computed: {

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