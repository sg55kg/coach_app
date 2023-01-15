<script lang="ts">
    import {auth0Client, isAuthenticated, userDB} from "../stores/authStore";
    import UserService from "../service/userService";

    export let ssr = false
    export let user
    const logout = async () => {
        //await UserService.logout($auth0Client!)
    }

    let showDropdown: boolean = false

</script>

<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle">
    <div class="flex items-center align-baseline">
        {#if isAuthenticated}
            <a href="/home" class="font-semibold tracking-widest uppercase text-2xl ml-1 text-yellow-lt">
                Coachable
            </a>
        {:else}
            <a href="/" class="font-semibold tracking-widest uppercase text-2xl ml-1 text-yellow-lt">
                Coachable
            </a>
        {/if}

            <p>&nbsp | &nbsp</p>
            <a href="/home/coach">Coach</a>
            <p>&nbsp | &nbsp</p>
            <a href="/home/athlete">Athlete</a>


    </div>
    <div class="relative">
        <button class="align-middle" on:click={() => { showDropdown = !showDropdown }}>
            <img src={user.picture} alt="profile" class="ml-2 mr-1">
        </button>
        {#if showDropdown}
            <div on:blur={() => showDropdown = false}
                 id="header-dropdown"
                 class="absolute right-0 w-56 flex flex-col z-40 bg-gray-300 items-center justify-center py-5 drop-shadow-2xl drop-shadow-black">
                {#if window.screen.width < 800}
                    <a class="w-full py-2 hover:bg-gray-200 text-center" href="/home/coach">Coach Dashboard</a>
                    <a class="w-full py-2 hover:bg-gray-200 text-center" href="/home/athlete">Athlete Dashboard</a>
                {/if}
                <a on:click={() => showDropdown = false} class="w-full py-2 hover:bg-gray-200 text-center" href="/home/user">Settings</a>
                <button on:click={logout} class="w-full py-2 hover:bg-gray-200">Sign Out</button>
            </div>
        {/if}
    </div>
</header>

<style>
    img {
        border-radius: 50%;
        height: 45px;
    }
</style>