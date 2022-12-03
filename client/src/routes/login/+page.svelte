<!--<script context="module">-->

<!--    -->
<!--</script>-->
<script lang="ts">
    import {enhance} from '$app/forms'
    import {user} from "../../lib/stores/authStore";
    import {redirect} from "@sveltejs/kit";
    import {beforeUpdate} from "svelte";

    let location = '/'
    beforeUpdate(() => {
        if($user) {
            console.log('fired')
            //throw redirect(302, )
        }
    })
</script>

<form method="POST" use:enhance={() => {
    return async ({ result, update }) => {
        console.log(result)
        if (result.data?.success) {
            user.set(result.data.success)
            window.location.replace(result.data.location)
        } else {
            console.log(result)
        }

       // update()
    }
}}>
    <input type="text" name="username" placeholder="Username">
    <input type="password" name="password" placeholder="Password">
    <button>Login</button>
    <hr>
    <small class="m-auto pt-4 font-semibold">
        New here?
    </small>
    <a class="text-teal-500 hover:text-teal-600" href="register">
        <small class="m-auto pt-4 font-semibold">Register for an account</small>
    </a>
</form>

<style>

</style>