<template>
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <Modal v-if="modalActive" :modalMessage="modalMessage" v-on:close-modal="closeModal" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <div class="editor main-editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" disabled /> <!-- @image-added="imageHandler" -->
            </div>
            <div class="editor  main-editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" disabled />
            </div>
            <div class="choices">
                <div class="choice">
                    <div class="editor" :class="{'editor-inactive': showQuestion}">
                        <vue-editor :editorOptions="editorSettingsQuestion" v-model="question1" disabled /> <!-- @image-added="imageHandler" -->
                    </div>
                    <div class="editor" :class="{'editor-inactive': showAnswer}">
                        <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer1" disabled />
                    </div>
                    <button class="select-button" @click.prevent="cardSelected1">Select</button>
                </div>
                <div class="choice">
                    <div class="editor" :class="{'editor-inactive': showQuestion}">
                        <vue-editor :editorOptions="editorSettingsQuestion" v-model="question2" disabled /> <!-- @image-added="imageHandler" -->
                    </div>
                    <div class="editor" :class="{'editor-inactive': showAnswer}">
                        <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer2" disabled />
                    </div>
                    <button class="select-button" @click.prevent="cardSelected2">Select</button>
                </div>
                <div class="choice">
                    <div class="editor" :class="{'editor-inactive': showQuestion}">
                        <vue-editor :editorOptions="editorSettingsQuestion" v-model="question3" disabled /> <!-- @image-added="imageHandler" -->
                    </div>
                    <div class="editor" :class="{'editor-inactive': showAnswer}">
                        <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer3" disabled />
                    </div>
                    <button class="select-button" @click.prevent="cardSelected3">Select</button>
                </div>
                <div class="choice">
                    <div class="editor" :class="{'editor-inactive': showQuestion}">
                        <vue-editor :editorOptions="editorSettingsQuestion" v-model="question4" disabled /> <!-- @image-added="imageHandler" -->
                    </div>
                    <div class="editor" :class="{'editor-inactive': showAnswer}">
                        <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer4" disabled />
                    </div>
                    <button class="select-button" @click.prevent="cardSelected4">Select</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Quill from "quill";
import "firebase/storage";
import BlogCoverPreview from "../components/BlogCoverPreview.vue";
import Loading from "../components/Loading";
import Modal from "../components/Modal";


window.Quill = Quill;


export default {
    name: "MultiChoiceStudy",
    components: {
        BlogCoverPreview,
        Loading,
        Modal,
    },
    data() {
        return {
            file: null,
            error: null,
            errorMessage: null,
            loading: null,
            showQuestion: true,
            showAnswer: false,
            newGame: true,
            question: "",
            answer: "",
            cardId: null,
            question1: "",
            answer1: "",
            cardId1: null,
            question2: "",
            answer2: "",
            cardId2: null,
            question3: "",
            answer3: "",
            cardId3: null,
            question4: "",
            answer4: "",
            cardId4: null,
            tempQ: "",
            tempA: "",
            tempCard: null,
            flashcards: [],
            modalMessage: "Correct!",
            modalActive: null,
            editorSettingsQuestion: {
                modules: {
                    toolbar: false
                },
            },
            editorSettingsAnswer: {
                modules: {
                    toolbar: false
                },
            },
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
        getKeyCard() {
            let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
            this.question = this.$store.state.flashcards[index].question;
            this.answer = this.$store.state.flashcards[index].answer;
            this.cardId = this.$store.state.flashcards[index].flashcardId;
            let data = {
                question: this.question,
                answer: this.answer,
                cardId: this.cardId
            }
            this.flashcards.push(data)
        },
        getRandomCard() {
            let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
            this.tempQ = this.$store.state.flashcards[index].question;
            this.tempA = this.$store.state.flashcards[index].answer;
            this.tempCard = this.$store.state.flashcards[index].flashcardId;
            let data = {
                question: this.tempQ,
                answer: this.tempA,
                cardId: this.tempCard
            }
            return data
        },
        cardSelected1() {
            if (this.answer === this.answer1)
            {
                console.log("Correct")
                this.modalActive = !this.modalActive;
            }
            else
            {
                console.log("Not Correct")
            }
        },
        cardSelected2() {
            if (this.answer === this.answer2)
            {
                console.log("Correct")
                this.modalActive = !this.modalActive;
            }
            else
            {
                console.log("Not Correct")
            }
        },
        cardSelected3() {
            if (this.answer === this.answer3)
            {
                console.log("Correct")
                this.modalActive = !this.modalActive;
            }
            else
            {
                console.log("Not Correct")
            }
        },
        cardSelected4() {
            if (this.answer === this.answer4)
            {
                console.log("Correct")
                this.modalActive = !this.modalActive;
            }
            else
            {
                console.log("Not Correct")
            }
        },
        getFourRandomCards() {
            if (!this.newGame) {
                this.getKeyCard();
            }
            for(let i = 0; i < 3; i++)
            {
                let data = this.getRandomCard()
                this.flashcards.push(data)
            }
        },
        storeCards() {
            this.question1 = this.flashcards[0].question;
            this.answer1 = this.flashcards[0].answer;
            this.cardId1 = this.flashcards[0].cardId;

            this.question2 = this.flashcards[1].question;
            this.answer2 = this.flashcards[1].answer;
            this.cardId2 = this.flashcards[1].cardId1;

            this.question3 = this.flashcards[2].question;
            this.answer3 = this.flashcards[2].answer;
            this.cardId3 = this.flashcards[2].cardId1;

            this.question4 = this.flashcards[3].question;
            this.answer4 = this.flashcards[3].answer;
            this.cardId4 = this.flashcards[3].cardId1;
        },
        shuffleCards() {
            for (let i = 3; i > 0; i--) {
            
                // Generate random number
                var j = Math.floor(Math.random() * (i + 1));
                            
                var temp = this.flashcards[i];
                this.flashcards[i] = this.flashcards[j];
                this.flashcards[j] = temp;
            }
            
            //return this.flashcards;
        },
        resetCards() {
            this.flashcards = [];
            this.getFourRandomCards();
            this.shuffleCards();
            this.storeCards();
        },
        closeModal() {
            this.modalActive = !this.modalActive;
            //this.$router.push({name: "ViewDecks", params: {classId: this.classId}});
            this.resetCards();
        },
    },
    computed: {

    },
    created() {
        //this.$store.dispatch("getUserClassDeckFlashcards", {deckId: this.$route.params.deckId, classId: this.$route.params.classId});
        let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
        this.question = this.$store.state.flashcards[index].question;
        this.answer = this.$store.state.flashcards[index].answer;
        this.cardId = this.$store.state.flashcards[index].flashcardId;
        let data = {
            question: this.question,
            answer: this.answer,
            cardId: this.cardId
        }
        this.flashcards.push(data)
        this.getFourRandomCards();
        this.shuffleCards();
        this.storeCards();
        this.newGame = false;
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
        this.newGame = true;
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
        ::-webkit-scrollbar
        {
            display: none;
        }


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
        display: flex;
        justify-content: center;

        button
        {
            margin-right: 16px;
            flex: .15;
        }
    }
}
button#good
{
    background-color: green;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#good:hover
{
    background-color: rgba(1, 50, 32, 0.7);
}
button#ok
{
    background-color: yellow;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#ok:hover
{
    background-color: rgba(155, 135, 12, 0.7);
}
button#bad
{
    background-color: red;
    transition: .5s ease-in-out all;
    align-self: center;
    font-size: 14px;
    cursor: pointer;
    border-radius: 20px;
    padding: 12px 24px;
    color: #fff;
    //background-color: #303030;
    text-decoration: none;
}
button#bad:hover
{
    background-color: rgba(139, 0, 0, 0.7);
}
.choices
{
    display: flex;
    flex-direction: row;
    margin-top: 25px;
}
.choice
{
    flex: 1;
    display: flex;
    flex-direction: column;
    &:nth-child(1)
    {
        margin-right: 10px;
    }
    &:nth-child(2)
    {
        margin-right: 10px;
    }
    &:nth-child(3)
    {
        margin-right: 10px;
    }
    .select-button
    {
        margin-top: 10px;
    }
}
.main-editor
{
    height: 30vh;
}
</style>