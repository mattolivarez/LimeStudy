<template>
    <div class="card-wrap">
        <div class="cards container">
            <div class="toggle-edit">
                <span>Toggle Editing Decks </span>
                <input type="checkbox" v-model="editDeck" />
            </div>
            
            <DeckCard :decks="decks" v-for="(decks, index) in decks" :key="index" />
            <NewDeckCard />
        </div>
    </div>
</template>

<script>
import DeckCard from "../components/DeckCard"
import NewDeckCard from "../components/NewDeckCard"

export default {
    name: "ViewDecks",
    components: { 
        DeckCard,
        NewDeckCard,
    },
    computed: {
        blogPosts() {
            return this.$store.state.blogPosts;
        },
        decks() {
            return this.$store.state.decks;
        },
        editDeck: {
            get() {
                return this.$store.state.editDeck;
            },
            set(payload) {
                this.$store.commit("toggleEditDeck", payload);
            }
        }
    },
    created() {
        this.$store.dispatch("getUserClassDecks", this.$route.params.classId);
    },
    beforeDestroy() {
        this.$store.commit("toggleEditDeck", false);
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
        top: -70px;
        right: 0;

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
            background: #fff;
            left: 52px;
        }
    }
}

</style>