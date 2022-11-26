<script lang="ts">
    import {enhance} from '$app/forms'

</script>

<form method="POST" use:enhance={() => {
    return ({ result, update }) => {
        update()
    }
}}>
    <div>
        <input name="username" type="text" placeholder="Username">
        <input name="password" type="password" placeholder="Password">
        <input name="first-name" type="text" placeholder="First Name">
        <input name="last-name" type="text" placeholder="Last Name">
        <input name="email" type="text" placeholder="Email Address">
        <button>Sign Up</button>
    </div>
</form>

<style>

</style>