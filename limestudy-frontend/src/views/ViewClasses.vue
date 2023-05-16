<template>
    <!-- 
    Matthew Olivarez
    Spring 2023
    Senior Project
    Limestudy Frontend
    View to view all Classes, which contains the class card component
    Contains template (HTML), CSS, and JavaScript
    -->
    <div class="card-wrap">
        <div class="cards container">
            <div class="toggle-edit">
                <span>Toggle Editing Class </span>
                <input type="checkbox" v-model="editClass" />
            </div>

            <SearchBar class="search-bar" v-on:filter-page="filterPage" />
            

            <ClassCard :classes="classes" v-for="(classes, index) in classes" :key="index" />
            <NewClassCard />

        </div>
    </div>
</template>

<script>
import ClassCard from "../components/ClassCard"
import NewClassCard from "../components/NewClassCard"
import SearchBar from "../components/SearchBar"

export default {
    name: "ViewClasses",
    components: { 
        ClassCard,
        NewClassCard,
        SearchBar,
    },
    computed: {
        blogPosts() {
            return this.$store.state.blogPosts;
        },
        classes() {
            //console.log(this.$store.state.classes.length)
            return this.$store.state.classes;
        },
        editClass: {
            get() {
                return this.$store.state.editClass;
            },
            set(payload) {
                this.$store.commit("toggleEditClass", payload);
            }
        }
    },
    methods: {
        filterPage() {
            // Declare variables
            let searchBar, filter, classes, classNames, i, txtValue; //classInfo
            searchBar = document.querySelector('.search input');
            filter = searchBar.value.toUpperCase();
            classes = document.querySelectorAll('.card');
            //classInfo = document.querySelectorAll('.card-info');
            classNames = document.getElementsByTagName('h4');

            // Loop through all list items, and hide those who don't match the search query
            for (i = 0; i < this.$store.state.classes.length; i++) {
                //a = [i].getElementsByTagName("a")[0];
                txtValue = classNames[i].textContent || classNames[i].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    classes[i].style.display = "";
                } else {
                    classes[i].style.display = "none";
                }
            }

            //console.log(classes)
            //console.log(classInfo)
            //console.log(classNames)
            //console.log()
        }
    },
    created() {
        this.$store.dispatch("getUserClasses");
        
    },
    beforeDestroy() {
        this.$store.commit("toggleEditClass", false);
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
        left: 0px;

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
.search-bar
{
    align-items: center;
    position: absolute;
    top: -80px;
    right: 0;
}
</style>