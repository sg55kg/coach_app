<script lang="ts">
    import {enhance} from '$app/forms'
    export let form
</script>

<form class="flex" method="POST" use:enhance={() => {
    return ({ result, update }) => {
        update()
    }
}}>

    <div class="bg-white border-white p-4 px-8 w-96 m-auto flex flex-col justify-items-center content-center drop-shadow-2xl rounded">
        <h1 class="font-extrabold text-3xl pb-2">
            Register
        </h1>
        <div class="flex justify-center flex-col m-auto">
            <input name="username"
                   type="text"
                   placeholder="Username"
                   class={!form?.errors?.username ? 'register-input' : 'input-error'}
                   autocomplete="off"
                   value={form?.data?.username ?? ''}>
            {#if form?.errors?.username}
                <small class="label-text-alt text-red-500">{form?.errors?.username[0]}</small>
            {/if}
        </div>
        <div class="flex justify-center flex-col m-auto">
            <input name="password"
                   type="password"
                   placeholder="Password"
                   class={!form?.errors?.password ? 'register-input' : 'input-error'}>
            {#if form?.errors?.password}
                <small class="label-text-alt text-red-500">{form?.errors?.password[0]}</small>
            {/if}
        </div>
        <div class="flex justify-center flex-col m-auto">
            <input name="passwordConfirm"
                   type="password"
                   placeholder="Confirm Password"
                   class={!form?.errors?.passwordConfirm ? 'register-input' : 'input-error'}>
            {#if form?.errors?.passwordConfirm}
                <small class="label-text-alt text-red-500">{form?.errors?.passwordConfirm[0]}</small>
            {/if}
        </div>
        <div class="flex justify-center flex-col m-auto">
            <input name="firstName"
                   type="text"
                   placeholder="First Name"
                   class={!form?.errors?.firstName ? 'register-input' : 'input-error'}
                   value={form?.data?.firstName ?? ''}>
            {#if form?.errors?.firstName}
                <small class="label-text-alt text-red-500">{form?.errors?.firstName[0]}</small>
            {/if}
        </div>
        <div class="flex justify-center flex-col m-auto">
            <input name="lastName"
                   type="text"
                   placeholder="Last Name"
                   class={!form?.errors?.lastName ? 'register-input' : 'input-error'}
                   value={form?.data?.lastName ?? ''}>
            {#if form?.errors?.lastName}
                <small class="label-text-alt text-red-500">{form?.errors?.lastName[0]}</small>
            {/if}
        </div>
        <div class="flex justify-center flex-col m-auto">
            <input name="email"
                   type="text"
                   placeholder="Email Address"
                   class={!form?.errors?.email ? 'register-input' : 'input-error'}
                   value={form?.data?.email ?? ''}>
            {#if form?.errors?.email}
                <small class="label-text-alt text-red-500">{form?.errors?.email[0]}</small>
            {/if}
        </div>
        <div class="mt-2 flex justify-center m-auto">
            <label class="p-1">I am a...</label>
            <select name="role" class="px-2">
                <option value="coach">Coach</option>
                <option value="athlete">Athlete</option>
            </select>
        </div>

        <button class="m-auto my-6 p-2 px-4 bg-yellow-400 hover:bg-yellow-600">
            Sign Up
        </button>
        <hr>
        <small class="m-auto pt-4 font-semibold">
            Already have an account? <a class="text-teal-500 hover:text-teal-600" href="/login">Login</a>
        </small>
    </div>
</form>

<style>
    .register-input {
        border-bottom: 3px solid gray;
        transition: border-bottom-color .25s linear;
        padding: 1em 2em .5em;
        font-size: 18px;
    }

    .register-input:focus {
        border-bottom: 3px solid deepskyblue;
        outline: none;
    }

    .input-error {
        border-bottom: 3px solid red;
        padding: 1em 2em .5em;
        font-size: 18px;
    }

    .input-error:focus {
        outline: none;
    }
</style>