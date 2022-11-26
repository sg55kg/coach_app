<script lang="ts">
    import {enhance} from '$app/forms'

</script>

<form method="POST" use:enhance={() => {
    return ({ result, update }) => {
        update()
    }
}}>

    <div class="bg-white border-white p-4 px-8 flex flex-col justify-items-center">
        <h1 class="font-extrabold text-3xl pb-2">Register</h1>
        <input name="username" type="text" placeholder="Username" class="register-input">
        <input name="password" type="password" placeholder="Password" class="register-input">
        <input name="first-name" type="text" placeholder="First Name" class="register-input">
        <input name="last-name" type="text" placeholder="Last Name" class="register-input">
        <input name="email" type="text" placeholder="Email Address" class="register-input">
        <button class="m-auto mt-6 p-2 bg-yellow-400 hover:bg-yellow-600">
            Sign Up
        </button>
        <small class="m-auto pt-4 font-semibold">
            Already have an account? <a class="text-teal-500" href="/login">Login</a>
        </small>
    </div>
</form>

<style>
    .register-input {
        padding: 1em;
        border-bottom: 3px solid black;
        transition: border-bottom-color .25s linear;
    }

    .register-input:focus {
        border-bottom: 3px solid deepskyblue;
        outline: none;
    }
</style>