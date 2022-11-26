<!--<script context="module">-->

<!--    -->
<!--</script>-->
<script lang="ts">
    import {enhance} from '$app/forms'
    import {user} from "../../lib/stores/authStore";
    import {redirect} from "@sveltejs/kit";

    let location = '/'

    if($user) {
        console.log('fired')
        throw redirect(302, '/')
    }
</script>

<form method="POST" use:enhance={() => {
    return async ({ result, update }) => {
        console.log(result)

        update()

    }
}}>
    <input type="text" name="username" placeholder="Username">
    <input type="password" name="password" placeholder="Password">
    <button>Login</button>
</form>

<style>

</style>