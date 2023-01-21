<script lang="ts">
    import {onMount} from "svelte";
    import {authUser, userDB} from "$lib/stores/authStore";
    import {goto} from "$app/navigation";
    import {page} from '$app/stores'
    import type {LayoutData} from "./$types";
    import {User} from "$lib/classes/user";

    export let data: LayoutData

    const { user, userData } = data

    if (user && userData) {
        $userDB = User.build(userData)
        $authUser = user
    }

    onMount(async () => {
    })
</script>

<svelte:head>
    <title>Home</title>
    <meta name="description" content="View athlete and coach dashboards" />
</svelte:head>


{#if $userDB}
<section class="flex flex-col">
    <h1 class="text-3xl text-center m-10 uppercase tracking-widest text-textgray font-semibold">Welcome</h1>
    <div class="flex flex-col lg:flex-row self-center lg:justify-around w-5/12">
        {#if $userDB.coachData}
            <a class="text-gray-200 my-2 lg:my-0 bg-yellow rounded p-2 font-bold text-center text-sm lg:w-5/12 hover:bg-yellow-shade"
               href="/home/coach/{$userDB.coachData.id}">
                Coach Dashboard
            </a>
        {:else}
            <a class="text-gray-200 my-2 lg:my-0 bg-yellow rounded p-2 font-bold text-center text-sm lg:w-5/12 hover:bg-yellow-shade"
               href="/home/coach/get-started">
                Coach Dashboard
            </a>
        {/if}
        <a class="text-gray-200 my-2 lg:my-0 bg-yellow rounded p-2 font-bold text-center text-sm lg:w-5/12 hover:bg-yellow-shade"
           href="/home/athlete">
            Athlete Dashboard
        </a>
    </div>
</section>
{/if}

<style>

</style>