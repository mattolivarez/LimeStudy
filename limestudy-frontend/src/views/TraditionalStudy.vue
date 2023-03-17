<template>
    <div class="create-post">
        <BlogCoverPreview v-show="this.$store.state.blogPhotoPreview" />
        <Loading v-show="loading" />
        <div class="container">
            <div :class="{invisible: !error}" class="err-message">
                <p><span>Error: </span>{{ this.errorMessage }}</p>
            </div>
            <!-- <div class="editor" :class="{'editor-inactive': !showQuestion}">
                <vue-editor :editorOptions="editorSettingsQuestion" v-model="question" disabled /> @image-added="imageHandler" -->
            <!--</div>
            <div class="editor" :class="{'editor-inactive': !showAnswer}">
                <vue-editor :editorOptions="editorSettingsAnswer" v-model="answer" disabled />
            </div> -->
            <div class="editor">
                <div v-if="currentFlashcard" class="card">
                    <div class="card-inner">
                        <div class="card-face card-face-front">
                            <p v-html="currentFlashcard.question"></p>
                        </div>
                        <div class="card-face card-face-back">
                            <p v-html="currentFlashcard.answer"></p>
                        </div>
                    </div>
                </div>
                <h2 v-else>Today's flashcard set completed!</h2>
            </div>
            
            <div class="blog-actions">
                <button v-if="currentFlashcard.noteId">View Relevant Note</button>
                <button v-if="!cardAnswered" @click.prevent="showLess">Show less often</button> 
                <button v-if="!cardAnswered" @click.prevent="showSame">Show at same rate</button>
                <button v-if="!cardAnswered" @click.prevent="showMore">Show more often</button>
                <button v-if="cardAnswered" @click.prevent="showNextCard">Get Next Card</button>
                <!--<router-link class="router-button" :to="{name: 'BlogPreview'}">Post Preview</router-link>-->
            </div>
        </div>
    </div>
</template>

<script>
import Quill from "quill";
import "firebase/storage";
import BlogCoverPreview from "../components/BlogCoverPreview.vue";
import Loading from "../components/Loading";
// import StudyCard from "../components/StudyCard";
import axios from 'axios';

window.Quill = Quill;

// const card = document.querySelector('.card-inner')
// card.addEventListener('click', () => {
//     card.classList.toggle('is-flipped');
// })

export default {
    name: "TraditionalStudy",
    components: {
        BlogCoverPreview,
        Loading,
        // StudyCard
    },
    data() {
        return {
            studyFlashcards: [],
            currentFlashcard: null,
            count: 0,
            showQuestion: null,
            showAnswer: null,
            loading: false,
            error: null,
            errorMessage: "",
            cardAnswered: false,
            flipped: false,
            existingSession: null,
            // editorSettingsQuestion: {
            //     modules: {
            //         toolbar: false
            //     },
            // },
            // editorSettingsAnswer: {
            //     modules: {
            //         toolbar: false
            //     },
            // },
        };
    },
    methods: {
        changeCardSide() {
            this.showQuestion = !this.showQuestion;
            this.showAnswer = !this.showAnswer;
        },
        getRandomCard() {
            let index = Math.floor(Math.random() * this.$store.state.flashcards.length);
            this.question = this.$store.state.flashcards[index].question;
            this.answer = this.$store.state.flashcards[index].answer;
        },
        getNextCard() {
            if (this.count < this.studyFlashcards.length)
            {
                this.currentFlashcard = this.studyFlashcards[this.count];
                this.count++;
            }
            else
            {
                this.currentFlashcard = null;
            }
            this.count += 1;
        },
        flipCard() {
            const card = document.querySelector('.card-inner')
            card.classList.toggle('is-flipped');
            this.flipped = true;
        },
        showNextCard() {
            this.loading = true;
            this.getNextCard();
            this.cardAnswered = false;
            this.loading = false;
        },
        async showLess() {
            this.loading = true;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.existingSession.session_correct += 1;
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 1, incorrect: 0 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            this.currentFlashcard.correct += 1;
            this.currentFlashcard.occurrence_rate_input += 1;
            await this.$store.dispatch("updateUserFlashcard", { flashcard: this.currentFlashcard });
            this.cardAnswered = true;
            this.flipCard();
            this.loading = false;
        },
        async showSame() {
            this.loading = true;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 0, incorrect: 0 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            await this.$store.dispatch("updateUserFlashcard", { flashcard: this.currentFlashcard });
            this.cardAnswered = true;
            this.flipCard();
            this.loading = false;
        },
        async showMore() {
            this.loading = true;
            await this.checkForExistingSession();
            if (this.existingSession)
            {
                // update session
                this.existingSession.session_incorrect += 1;
                this.$store.dispatch("updateUserSession", { flashcard: this.currentFlashcard, session: this.existingSession });
            }
            else
            {
                // add session
                let answer = { correct: 0, incorrect: 1 }
                this.$store.dispatch("createUserSession", { flashcard: this.currentFlashcard, answer: answer });
            }
            this.currentFlashcard.incorrect += 1;
            this.currentFlashcard.occurrence_rate_input -= 1;
            await this.$store.dispatch("updateUserFlashcard", { flashcard: this.currentFlashcard });
            this.cardAnswered = true;
            this.flipCard();
            this.loading = false;
        },
        async checkForExistingSession() {
            await axios({
                method: 'GET',
                url: `/api/classes/${this.currentFlashcard.classId}/decks/${this.currentFlashcard.deckId}/flashcards/${this.currentFlashcard.flashcardId}/sessions/check-for-session`,
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('user'),
                    'Content-Type': 'application/json'
                },
            })
            .then((response) => {
                console.log("response starts here")
                console.log(response.data)
                this.existingSession = {
                    sessionId: response.data.sessionId,
                    flashcardId: response.data.flashcardId,
                    deckId: response.data.deckId,
                    classId: response.data.classId,
                    userId: response.data.userId,
                    session_date: response.data.session_date,
                    session_correct: response.data.session_correct,
                    session_incorrect: response.data.session_incorrect
                }
                return;
            }).catch((err) => {
                console.log("error starts here")
                console.log(err);
                this.existingSession = null;
                return;
            });
        },
    },
    computed: {

    },
    async created() {
        this.loading = true;
        this.studyFlashcards = [];
        await axios({
            method: 'GET',
            url: `/api/classes/${this.$route.params.classId}/decks/${this.$route.params.deckId}/flashcards/traditional-study`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('user'),
                'Content-Type': 'application/json'
            },
        })
        .then((response) => {
            console.log("response starts here")
            console.log(response.data);
            response.data.forEach((studySetFlashcard) => {
                const newFlashcard = {
                    flashcardId: studySetFlashcard.flashcardId,
                    deckId: studySetFlashcard.deckId,
                    classId: studySetFlashcard.classId,
                    userId: studySetFlashcard.userId,
                    question: studySetFlashcard.question,
                    answer: studySetFlashcard.answer,
                    flashcard_created_on: studySetFlashcard.flashcard_created_on,
                    correct: studySetFlashcard.correct,
                    incorrect: studySetFlashcard.incorrect,
                    last_studied_on: studySetFlashcard.last_studied_on,
                    occurrence_rate: studySetFlashcard.occurrence_rate,
                    occurrence_rate_input: studySetFlashcard.occurrence_rate_input
                }
                this.studyFlashcards.push(newFlashcard);
            });
            console.log(this.studyFlashcards);
            return;
        }).catch((err) => {
            console.log("error starts here")
            console.log(err);
            return;
        });
        this.getNextCard();
        console.log(this.currentFlashcard);
        this.loading = false;
    },
    beforeDestroy() {
        this.showQuestion = true;
        this.showAnswer = false;
        if (this.flipped)
        {
            this.flipCard();
            this.flipped = false;
        }
        this.existingSession = null;
    },
};
</script>


<style lang="scss" scoped>
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
        justify-content: center;
        align-items: center;

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
.card
{
    margin: 100px auto 0;
    width: 400px;
    height: 600px;
}
.card-inner 
{
    width: 100%;
    height: 100%;
    transition: transform 1s;
    transform-style: preserve-3d;
    //cursor: pointer;
    position: relative;
}
.card-inner.is-flipped
{
    transform: rotateY(180deg);
}
.card-face
{
    position: absolute;
    width: 100%;
    height: 100%;
    backface-visibility: hidden;
    -webkit-backface-visibility: hidden;
    overflow: hidden;
    border-radius: 16px;
    box-shadow: 0px 3px 18px 3px rgba(0, 0, 0, 0.2);
    //text-transform: uppercase;
    text-align: center;
}
.card-face-front
{
    background-image: linear-gradient(to bottom right, var(--primary), var(--secondary));
    display: flex;
    align-items: center;
    justify-content: center;
}
.card-face-front p
{
    color: #000;
    font-size: 32px;
}
.card-face-back
{
    background-image: linear-gradient(to bottom right, var(--primary), var(--secondary));
    transform: rotateY(180deg);
    display: flex;
    align-items: center;
    justify-content: center;
}
.card-face-back p
{
    color: #000;
    font-size: 32px;
}
</style>