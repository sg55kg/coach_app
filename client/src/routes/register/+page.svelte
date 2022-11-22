<script lang="ts">
    import {enhance} from '$app/forms'

</script>

<form method="POST" use:enhance={() => {
    return ({ result, update }) => {
        update()
    }
}}>
    <input name="username" type="text">
    <input name="password" type="password">
    <input name="first-name" type="text">
    <input name="last-name" type="text">
    <input name="email" type="text">
    <button>Sign Up</button>
</form>

<style>

</style>