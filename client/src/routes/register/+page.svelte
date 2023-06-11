<script lang="ts">
    let username: string = '';
    let email: string = '';
    let password: string = '';
    let passwordConfirm: string = '';

    const handleSubmit = async () => {
        try {
        } catch (e) {}
    };
</script>

<form
    method="post"
    class="m-auto flex flex-col bg-gray-400 p-4 px-5 shadow-2xl"
>
    <div class="text-center text-lg font-bold">
        <h1>Get Started</h1>
    </div>
    <div class="m-2 flex flex-col">
        <label>Name</label>
        <input
            name="username"
            class="bg-gray-100 p-1 font-semibold text-textblue"
            type="text"
        />
    </div>
    <div class="m-2 flex flex-col">
        <label>Email</label>
        <input
            name="email"
            class="bg-gray-100 p-1 font-semibold text-textblue"
            type="text"
        />
    </div>
    <div class="m-2 flex flex-col">
        <label>Password</label>
        <input
            name="password"
            class="bg-gray-100 p-1 font-semibold text-textblue"
            type="text"
        />
    </div>
    <div class="m-2 flex flex-col">
        <label>Confirm Password</label>
        <input
            name="password-confirm"
            class="bg-gray-100 p-1 font-semibold text-textblue"
            type="text"
        />
    </div>
    <div class="my-3 flex justify-center">
        <button
            class="rounded bg-yellow p-2 font-semibold text-black hover:bg-yellow-shade"
        >
            Register
        </button>
    </div>
</form>

<style>
</style>
