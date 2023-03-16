<template>
    <div class="card-wrap" v-if="showDecks">
        <div class="cards container">
            <div class="toggle-edit">
                <span>Toggle Editing Decks </span>
                <input type="checkbox" v-model="editDeck" />
            </div>

            <div class="buttons">
                <button class="preview" :class="{'button-inactive': showDecks}" @click.prevent="showOther">Decks</button>
                <button class="preview" :class="{'button-inactive': showNotes}" @click.prevent="showOther">Notes</button>
            </div>

            <SearchBar class="search-bar" v-on:filter-page="filterPage" />
            
            <DeckCard :decks="decks" v-for="(decks, index) in decks" :key="index" />
            <NewDeckCard />
        </div>
    </div>
    <div class="card-wrap" v-else-if="showNotes">
        <div class="cards container">
            <div class="toggle-edit">
                <span>Toggle Editing Notes </span>
                <input type="checkbox" v-model="editNote" />
            </div>

            <div class="buttons">
                <button class="preview" :class="{'button-inactive': showDecks}" @click.prevent="showOther">Decks</button>
                <button class="preview" :class="{'button-inactive': showNotes}" @click.prevent="showOther">Notes</button>
            </div>

            <SearchBar class="search-bar" v-on:filter-page="filterPage" />
            
            <NoteCard :notes="notes" v-for="(notes, index) in notes" :key="index" />
            <NewNoteCard />
        </div>
    </div>
</template>

<script>
import DeckCard from "../components/DeckCard"
import NewDeckCard from "../components/NewDeckCard"
import SearchBar from "../components/SearchBar"
import NewNoteCard from "../components/NewNoteCard"
import NoteCard from "../components/NoteCard"

export default {
    name: "ViewDecks",
    components: { 
        DeckCard,
        NewDeckCard,
        SearchBar,
        NewNoteCard,
        NoteCard,
    },
    data() {
        return {
            showDecks: true,
            showNotes: false
        }
    },
    computed: {
        decks() {
            return this.$store.state.decks;
        },
        notes() {
            return this.$store.state.notes;
        },
        editDeck: {
            get() {
                return this.$store.state.editDeck;
            },
            set(payload) {
                this.$store.commit("toggleEditDeck", payload);
            }
        },
        editNote: {
            get() {
                return this.$store.state.editNote;
            },
            set(payload) {
                this.$store.commit("toggleEditNote", payload);
            }
        }
    },
    methods: {
        showOther() {
            this.showDecks = !this.showDecks;
            this.showNotes = !this.showNotes;
            //console.log(this.showDecks);
            //console.log(this.showNotes);
        },
        filterPage() {
            // Declare variables
            let searchBar, filter, decks, deckNames, i, txtValue; //classInfo
            searchBar = document.querySelector('.search input');
            filter = searchBar.value.toUpperCase();
            decks = document.querySelectorAll('.card');
            //classInfo = document.querySelectorAll('.card-info');
            deckNames = document.getElementsByTagName('h4');

            // Loop through all list items, and hide those who don't match the search query
            for (i = 0; i < this.$store.state.decks.length; i++) {
                //a = [i].getElementsByTagName("a")[0];
                txtValue = deckNames[i].textContent || deckNames[i].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    decks[i].style.display = "";
                } else {
                    decks[i].style.display = "none";
                }
            }

            //console.log(classes)
            //console.log(classInfo)
            //console.log(names)
            //console.log()
        },
    },
    created() {
        this.$store.dispatch("getUserClassDecks", this.$route.params.classId);
        this.$store.dispatch("getUserClassNotes", this.$route.params.classId);
    },
    beforeDestroy() {
        this.$store.commit("toggleEditDeck", false);
        this.$store.commit("toggleEditNote", false);
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
.buttons
{
    display: flex;
    align-items: center;
    position: absolute;
    top: -93px;
    right: calc(100vw/2.5);
}
.preview
{
    margin-left: 16px;
    text-transform: initial;
}
.search-bar
{
    align-items: center;
    position: absolute;
    top: -80px;
    right: 0;
}
</style>