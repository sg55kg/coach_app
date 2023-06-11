<script lang="ts">
    import { User } from '$lib/classes/user';
    import { onMount } from 'svelte';
    import { userDB } from '$lib/stores/authStore';
    import dayjs from 'dayjs';
    import AuthHeader from '$lib/components/AuthHeader.svelte';
    import AthleteHeader from '$lib/components/AthleteHeader.svelte';
    import Toggle from "$lib/components/shared/layout/Toggle.svelte";
    import UserService from "../../../lib/service/UserService";

    let user: User;

    const handleSaveUser = async () => {
        try {
            const userDto = {
                id: user.id,
                username: user.username,
                athleteName: null,
                preferences: user.preferences
            } as any;
            if (user.athleteData) {
                userDto.athleteName = user.username;
            }
            const updatedUser: User = await UserService.updateUserData(userDto);
            userDB.set(updatedUser);
            user = User.createFrom($userDB);
        } catch (e) {
            console.log(e);
        }
    };

    const toggleUserPreferenceWeight = () => {
        if (user.preferences.weight === 'lb') {
            user.preferences.weight = 'kg';
        } else {
            user.preferences.weight = 'lb';
        }
    };

    const toggleUserPreferenceHomePage = () => {
        if (user.preferences.defaultHomePage === 'athlete') {
            user.preferences.defaultHomePage = 'coach';
        } else {
            user.preferences.defaultHomePage = 'athlete';
        }
    };

    onMount(async () => {
        user = User.createFrom(JSON.parse(JSON.stringify($userDB)));
    });
</script>

<svelte:head>
    <title>Settings</title>
    <meta
        name="description"
        content="Update your profile and manage your teams"
    />
</svelte:head>

{#if user}
    {#if $userDB.coachData.teams?.length > 0}
        <AuthHeader />
    {:else}
        <AthleteHeader />
    {/if}
    <h1 class="text-4xl font-bold m-4">Settings</h1>
    <div class="m-4 grid grid-cols-2">
        <div class="col-span-1">
            <h2 class="text-xl text-textblue">General Settings</h2>
            <div class="my-2 flex flex-col">
                <label class="text-sm">Email</label>
                <input
                        class="w-fit bg-gray-200 p-2 text-lg"
                        disabled
                        type="text"
                        bind:value="{user.email}"
                />
            </div>
            <div class="my-2 flex flex-col">
                <label class="text-sm">Username</label>
                <div>
                    <input
                            class="w-fit bg-gray-300 p-2 text-lg"
                            type="text"
                            bind:value="{user.username}"
                    />
                    {#if user.username !== $userDB.username}
                        <button
                                on:click="{handleSaveUser}"
                                class="mr-2 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300"
                        >Save</button
                        >
                    {/if}
                </div>
            </div>
            <div class="my-2 flex flex-col">
                <p>
                    Coachable member since: {$userDB.createdAt
                    ? $userDB.createdAt.format('MMMM DD, YYYY')
                    : dayjs().format('MMMM DD, YYYY')}
                </p>
            </div>
        </div>
        <div class="col-span-1 flex flex-col">
            <h2 class="text-xl text-textblue">Preferences</h2>
            <div class="p-2">
                <div class="my-2">
                    <p>Weight</p>
                    <div class="flex items-center">
                        <Toggle checked="{user.preferences.weight === 'kg'}" onChange="{toggleUserPreferenceWeight}" />
                        <p class="mx-2 text-yellow">{user.preferences.weight}</p>
                    </div>
                </div>
               <div class="my-2">
                   <p>Default Home Page</p>
                   <div class="flex items-center">
                       <Toggle checked="{user.preferences.defaultHomePage === 'athlete'}" onChange="{toggleUserPreferenceHomePage}" />
                       <p class="mx-2 text-yellow">{user.preferences.defaultHomePage === 'athlete' ? 'Athlete Home Page' : 'Coach Home Page'}</p>
                   </div>
               </div>
                {#if user.preferences.weight !== $userDB.preferences.weight || user.preferences.defaultHomePage !== $userDB.preferences.defaultHomePage}
                    <button on:click={handleSaveUser} class="mr-2 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300">
                        Save
                    </button>
                {/if}
            </div>
        </div>
        <br />
        <div class="col-span-2 my-2 flex flex-col">
            {#if user.athleteData?.team?.name}
                <p class="mb-2 font-semibold">
                    Currently a member of {user.athleteData?.team?.name}
                </p>
                <button
                    class="self-start rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300"
                >
                    Leave
                </button>
            {:else}
                <p class="font-semibold">
                    You are not on any teams. View a list of available teams to
                    join <a
                        class="text-link underline"
                        href="/home/athlete/teams">here</a
                    >
                </p>
            {/if}
        </div>
        <br />
        <div class="my-2 flex flex-col col-span-2">
            {#if user.coachData?.teams?.length > 0}
                <h4 class="text-xl text-textblue">My Teams:</h4>
                {#each user.coachData.teams as team}
                    <div class="bg-gray-200 p-2">
                        <p class="my-1">{team.name}</p>
                        <p class="my-1">
                            {team.numAthletes} athlete{team.numAthletes === 1
                                ? ''
                                : 's'}
                        </p>
                        <div class="mt-2 flex">
                            <button
                                class="mr-2 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300"
                            >
                                Edit
                            </button>
                            <button
                                class="ml-2 rounded border-2 border-red bg-gray-100 p-2 px-6 text-red hover:border-red-shade hover:bg-gray-300"
                            >
                                Delete
                            </button>
                        </div>
                    </div>
                {/each}
            {:else}
                <p class="font-semibold">
                    You are not on any teams. View a list of available teams to
                    join <a
                        class="text-link underline"
                        href="/home/athlete/teams">here</a
                    >
                </p>
            {/if}
        </div>

    </div>
{/if}

<style>
</style>
