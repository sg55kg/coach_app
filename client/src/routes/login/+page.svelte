<script lang="ts">
    import {enhance} from '$app/forms'
</script>

<form method="POST" use:enhance={() => {
    return ({ result, update }) => {
        console.log(result)
        //update()
    }
}}>
    <input type="text" name="username" placeholder="Username">
    <input type="password" name="password" placeholder="Password">
    <button>Login</button>
</form>

<style>

</style>