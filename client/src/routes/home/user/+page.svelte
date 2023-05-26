<script lang="ts">
    import { User } from '$lib/classes/user';
    import { onMount } from 'svelte';
    import { userDB } from '$lib/stores/authStore';
    import dayjs from 'dayjs';
    import UserService from '$lib/service/userService';
    import AuthHeader from '$lib/components/AuthHeader.svelte';
    import AthleteHeader from '$lib/components/AthleteHeader.svelte';

    let user: User;

    const handleSaveUsername = async () => {
        try {
            const userDto = {
                id: user.id,
                username: user.username,
                athleteName: null,
            } as any;
            if (user.athleteData) {
                userDto.athleteName = user.username;
            }
            const updatedUser: User = await UserService.updateUserData(userDto);
            userDB.set(updatedUser);
            user = JSON.parse(JSON.stringify($userDB));
        } catch (e) {
            console.log(e);
        }
    };

    onMount(async () => {
        user = JSON.parse(JSON.stringify($userDB));
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
    <div class="m-4 flex flex-col">
        <h1 class="text-4xl font-bold">Settings</h1>
        <br />
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
                        on:click="{handleSaveUsername}"
                        class="mr-2 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-300"
                        >Save</button
                    >
                {/if}
            </div>
        </div>
        <div class="my-2 flex flex-col">
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
        <div class="my-2 flex flex-col">
            {#if user.coachData?.teams?.length > 0}
                <h4 class="text-lg font-semibold">My Teams:</h4>
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
        <div class="my-2 flex flex-col">
            <p>
                Coachable member since: {$userDB.createdAt
                    ? $userDB.createdAt.format('MMMM DD, YYYY')
                    : dayjs().format('MMMM DD, YYYY')}
            </p>
        </div>
    </div>
{/if}

<style>
</style>
