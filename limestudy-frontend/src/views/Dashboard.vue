<template>
    <div>
        <h6>Welcome back, {{ firstName }}</h6>
        <h4>Member since: {{ new Date(accountCreatedOn).toLocaleString('en-us', {dateStyle: "long"}) }}</h4>
    </div>
</template>

<script>

export default {
    name: 'Dashboard',
    computed: {
        firstName: {
            get() {
                return this.$store.state.profileFirstName;
            }
        },
        accountCreatedOn: {
            get() {
                return this.$store.state.profileAccountCreatedOn;
            }
        }
    }
}
</script>


<style lang="scss"></style>