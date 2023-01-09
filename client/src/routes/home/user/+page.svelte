<script lang="ts">

    import {User} from "$lib/classes/user";
    import {onMount} from "svelte";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";

    let user: User

    onMount(async() => {
        user = JSON.parse(JSON.stringify($userDB))
        console.log('user data', user)

    })
</script>

{#if user}
    <div class="flex flex-col m-4">
        <h1 class="font-bold text-4xl">Settings</h1>
        <br>
        <div class="flex flex-col my-2">
            <label class="text-sm">Email</label>
            <input class="w-fit bg-gray-200 p-2 text-lg" disabled type="text" bind:value={user.email}>
        </div>
        <div class="flex flex-col my-2">
            <label class="text-sm">Username</label>
            <input class="w-fit bg-gray-300 p-2 text-lg" type="text" bind:value={user.username}>
        </div>
        <div class="flex flex-col my-2">
            {#if user.athleteData?.team?.name}
                <p class="font-semibold mb-2">Currently a member of {user.athleteData?.team?.name }</p>
                <button class="p-2 px-6 self-start border-textblue border-2 rounded bg-gray-100 hover:bg-gray-300">
                    Leave
                </button>
            {:else}
                <p class="font-semibold">You are not on any teams. View a list of available teams to join <a class="text-link underline" href="/home/athlete/teams">here</a></p>
            {/if}
        </div>
        <div class="flex flex-col my-2">
            {#if user.coachData?.teams?.length > 0}
                <h4 class="text-lg font-semibold">My Teams: </h4>
                {#each user.coachData.teams as team}
                    <div class="p-2 bg-gray-200">
                        <p class="my-1">{team.name}</p>
                        <p class="my-1">{team.athletes.length} athlete{team.athletes.length === 1 ? '' : 's'}</p>
                        <div class="flex mt-2">
                            <button class="p-2 px-6 mr-2 border-textblue border-2 rounded bg-gray-100 hover:bg-gray-300">
                                Edit
                            </button>
                            <button class="p-2 px-6 ml-2 border-red text-red border-2 rounded bg-gray-100 hover:bg-gray-300 hover:border-red-shade">
                                Delete
                            </button>
                        </div>

                    </div>
                {/each}
            {:else}
                <p class="font-semibold">You are not on any teams. View a list of available teams to join <a class="text-link underline" href="/home/athlete/teams">here</a></p>
            {/if}
        </div>
        <div class="flex flex-col my-2">
            <p>Coachable member since: {user.createdAt ? user.createdAt.format('MMMM DD, YYYY') : dayjs().format('MMMM DD, YYYY')}</p>
        </div>
    </div>
{/if}

<style>

</style>