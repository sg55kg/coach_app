<script lang="ts">

    let username: string = ''
    let email: string = ''
    let password: string = ''
    let passwordConfirm: string = ''

    const handleSubmit = async () => {
        try {

        } catch (e) {

        }
    }
</script>

<form method="post" class="flex flex-col p-4 px-5 m-auto bg-gray-400 shadow-2xl">
    <div class="text-center font-bold text-lg">
        <h1>Get Started</h1>
    </div>
    <div class="flex flex-col m-2">
        <label>Name</label>
        <input name="username" class="bg-gray-100 p-1 text-textblue font-semibold" type="text">
    </div>
    <div class="flex flex-col m-2">
        <label>Email</label>
        <input name="email" class="bg-gray-100 p-1 text-textblue font-semibold" type="text">
    </div>
    <div class="flex flex-col m-2">
        <label>Password</label>
        <input name="password" class="bg-gray-100 p-1 text-textblue font-semibold" type="text">
    </div>
    <div class="flex flex-col m-2">
        <label>Confirm Password</label>
        <input name="password-confirm" class="bg-gray-100 p-1 text-textblue font-semibold" type="text">
    </div>
    <div class="flex justify-center my-3">
        <button class="bg-yellow hover:bg-yellow-shade text-black font-semibold p-2 rounded">
            Register
        </button>
    </div>
</form>

<style>

</style>