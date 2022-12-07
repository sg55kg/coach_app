<script lang="ts">
    import {auth0Client, user} from "../stores/authStore";
    import UserService from "../service/userService";

    console.log($user)
    const logout = async () => {
        await UserService.logout($auth0Client)
    }

</script>

<header class="mb-4 p-2 bg-yellow-300 flex justify-between align-middle">
    <div class="flex items-center">
        <h1 class="font-bold">Coachable</h1>
        <p>&nbsp | &nbsp</p>
        <a href="/home/coach">My Teams</a>
    </div>
    <div class="flex">
        <button on:click={logout}>
            Logout
        </button>
        <img src={$user.picture} alt="profile">
    </div>
</header>

<style>
    img {
        border-radius: 50%;
        height: 50px;
    }
</style>