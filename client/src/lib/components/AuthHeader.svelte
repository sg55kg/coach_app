<script lang="ts">
    import {auth0Client, user} from "../stores/authStore";
    import UserService from "../service/userService";

    console.log($user)
    const logout = async () => {
        await UserService.logout($auth0Client)
    }

</script>

<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle">
    <div class="flex items-center align-baseline">
        <h1 class="font-semibold tracking-widest uppercase text-2xl ml-3 text-yellow-lt">Coachable</h1>
        <p>&nbsp | &nbsp</p>
        <a href="/home/coach">My Teams</a>
    </div>
    <div class="flex">
        <button on:click={logout}>
            Logout
        </button>
        <img src={$user.picture} alt="profile" class="ml-4 mr-2">
    </div>
</header>

<style>
    img {
        border-radius: 50%;
        height: 45px;
    }
</style>