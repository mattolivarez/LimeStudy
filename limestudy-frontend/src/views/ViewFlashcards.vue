<template>
    <div class="card-wrap">
        <div class="cards container">
            <div class="toggle-edit">
                <span>Toggle Editing Flashcards </span>
                <input type="checkbox" v-model="editFlashcard" />
            </div>

            <SearchBar class="search-bar" v-on:filter-page="filterPage" />
            
            <div class="flashcard-options">
                <NewFlashcardCard class="options" />
                <TradStudy class="options" />
                <MultiChoiceStudy class="options" />
            </div>
            <FlashcardCard :flashcards="flashcards" v-for="(flashcards, index) in flashcards" :key="index" />
            
        </div>
    </div>
</template>

<script>
import FlashcardCard from "../components/FlashcardCard"
import NewFlashcardCard from "../components/NewFlashcardCard"
import TradStudy from "../components/TraditionalStudy"
import MultiChoiceStudy from "../components/MultiChoiceStudy"
import SearchBar from "../components/SearchBar"

export default {
    name: "ViewFlashcards",
    components: { 
        FlashcardCard,
        NewFlashcardCard,
        TradStudy,
        MultiChoiceStudy,
        SearchBar,
    },
    computed: {
        blogPosts() {
            return this.$store.state.blogPosts;
        },
        flashcards() {
            return this.$store.state.flashcards;
        },
        editFlashcard: {
            get() {
                return this.$store.state.editFlashcard;
            },
            set(payload) {
                this.$store.commit("toggleEditFlashcard", payload);
            }
        }
    },
    methods: {
        filterPage() {
            // Declare variables
            let searchBar, filter, flashcards, questions, answers, i, qValue, aValue; //classInfo
            searchBar = document.querySelector('.search input');
            filter = searchBar.value.toUpperCase();
            flashcards = document.querySelectorAll('.flashcards');
            //classInfo = document.querySelectorAll('.card-info');
            questions = document.querySelectorAll('.questions');
            answers = document.querySelectorAll('.answers');

            // Loop through all list items, and hide those who don't match the search query
            for (i = 0; i < this.$store.state.flashcards.length; i++) {
                //a = [i].getElementsByTagName("a")[0];
                qValue = questions[i].textContent || questions[i].innerText;
                aValue = answers[i].textContent || answers[i].innerText;
                if (qValue.toUpperCase().indexOf(filter) > -1 || aValue.toUpperCase().indexOf(filter) > -1) {
                    flashcards[i].style.display = "";
                } else {
                    flashcards[i].style.display = "none";
                }
            }

            console.log(flashcards)
            //console.log(answers)
            //console.log(this.$store.state.flashcards.length)
            //console.log()
        },
    },
    created() {
        this.$store.dispatch("getUserClassDeckFlashcards", {deckId: this.$route.params.deckId, classId: this.$route.params.classId});
    },
    beforeDestroy() {
        this.$store.commit("toggleEditFlashcard", false);
    },
}
</script>

<style lang="scss" scoped>
.cards
{
    position: relative;

    .toggle-edit
    {
        display: flex;
        align-items: center;
        position: absolute;
        top: -65px;
        left: 0;

        span
        {
            margin-right: 16px;
        }

        input[type="checkbox"]
        {
            position: relative;
            border: none;
            -webkit-appearance: none;
            background: #fff;
            outline: none;
            width: 80px;
            height: 30px;
            border-radius: 20px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        }
        input[type="checkbox"]::before
        {
            content: "";
            position: absolute;
            width: 30px;
            height: 30px;
            border-radius: 20px;
            top: 0;
            left: 0;
            background: #303030;
            transform: scale(1.1);
            transition: 750ms ease all;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
        }
        input:checked[type="checkbox"]:before
        {
            background: var(--lime);
            left: 52px;
        }
    }
}

.card-wrap
{
    .cards
    {
        grid-template-columns: repeat(3, 1fr);
        
        .flashcard-options
        {
            grid-column: 1 / 4;
            display: flex;
            .options
            {
                flex: 1;
                &:nth-child(1)
                {
                    margin-right: 10px;
                }
                &:nth-child(2)
                {
                    margin-right: 10px;
                }
            }
        }
    }
}
.search-bar
{
    align-items: center;
    position: absolute;
    top: -80px;
    right: 0;
}

</style>