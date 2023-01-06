<script lang="ts">
    import {auth0Client, user} from "../stores/authStore";
    import UserService from "../service/userService";

    const logout = async () => {
        await UserService.logout($auth0Client!)
    }

</script>

<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle">
    <div class="flex items-center align-baseline">
        <a href="/" class="font-semibold tracking-widest uppercase text-2xl ml-3 text-yellow-lt">
            Coachable
        </a>
        <p>&nbsp | &nbsp</p>
        <a href="/home/coach">Coach</a>
        <p>&nbsp | &nbsp</p>
        <a href="/home/athlete">Athlete</a>
    </div>
    <div class="flex">
        <button on:click={logout}>
            <img src={$user.picture} alt="profile" class="ml-4 mr-2">
        </button>

    </div>
</header>

<style>
    img {
        border-radius: 50%;
        height: 45px;
    }
</style>