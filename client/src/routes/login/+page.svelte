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
    <hr>
    <small class="m-auto pt-4 font-semibold">
        New here? <a class="text-teal-500 hover:text-teal-600" href="/register">Register for an account</a>
    </small>
</form>

<style>

</style>