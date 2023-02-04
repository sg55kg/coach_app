<script lang="ts">
    import {displayPrograms} from "$lib/stores/programStore";
    import ProgramCard from "$lib/components/ProgramCard.svelte";
    import {onMount} from "svelte";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {TeamService} from "$lib/service/TeamService";
    import {Team} from "$lib/classes/team";
    import AthleteCard from "$lib/components/TeamDashboard/AthleteCard.svelte";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'
    import type {PageServerData} from "../../../../../../.svelte-kit/types/src/routes/$types";
    import {Program} from "$lib/classes/program";
    import {ProgramService} from "$lib/service/ProgramService";
    import dayjs from "dayjs";
    import CardLoadingSkeleton from "$lib/components/shared/loading/CardLoadingSkeleton.svelte";

    export let data: PageServerData

    let team: Team
    let loadingTeam: boolean = true
    let newLogoSrc: string = ''
    let newTeamName: string = ''
    let newTeamDescription: string = ''



    let showNameInput: boolean = false
    let showDescriptionInput: boolean = false
    let activeTab: 'athletes' | 'programs' | 'settings' = 'athletes'


    $: athleteList = team ? team.athletes : []

    const saveTeam = async () => {
        const data = {
            ...team,
            teamLogo: newLogoSrc,
            description: newTeamDescription,
            name: newTeamName.trim()
        }
        try {
            const teamRes = await TeamService.updateTeam(data)
            team = teamRes
            athleteList = team.athletes
            showNameInput = false
            showDescriptionInput = false
        } catch (e) {
            console.log(e)
        }
    }

    const fetchTeam = async () => {
        let pathArr = location.pathname.split('/')
        const teamId = pathArr[pathArr.length-1]
        try {
            const teamRes: Team = await TeamService.getTeam(teamId);
            team = teamRes
            return teamRes
        } catch (e) {
            console.log(e)
        }
    }

</script>

<svelte:head>
    <title>{team?.name ? team.name : 'loading'}</title>
    <meta name="description" content="Dashboard for {team?.name}. Manage your athletes and write new programs" />
</svelte:head>

<a href="/home/coach/{$userDB?.coachData?.id}" class="font-bold mx-4">{'<-'} Back to teams</a>

{#await fetchTeam()}
    <div class="flex flex-col">
        <div class="justify-self-center rounded bg-gray-400 mx-4 lg:mx-20 flex justify-between items-center p-3">
            <h1 class="font-bold text-3xl text-white">&nbsp;</h1>
        </div>
        <CardLoadingSkeleton />
        <CardLoadingSkeleton />
        <CardLoadingSkeleton />
    </div>
{:then teamRes}
    {#if team}
        <div class="flex flex-col items-center">
            <div class="rounded bg-gray-400 w-11/12 flex justify-between items-center p-3">
                <div class="flex items-center">
                    <h1 class="font-bold text-3xl text-white">
                        {team.name}
                    </h1>
                </div>
                <div>
                    <button on:click={() => activeTab = 'athletes'} class="mx-2 p-2" class:selected={activeTab === 'athletes'}>
                        Athletes
                    </button>
                    <button on:click={() => activeTab = 'programs'} class="mx-2 p-2" class:selected={activeTab === 'programs'}>
                        Programs
                    </button>
                    <button on:click={() => activeTab = 'settings'} class="mx-2 p-2" class:selected={activeTab === 'settings'}>
                        Settings
                    </button>
                </div>
            </div>
            {#if activeTab === 'athletes'}
                <div class="rounded bg-gray-200 w-11/12 flex flex-col p-3">
                    {#each athleteList as athlete, idx (athlete.id)}
                        <div>
                            <AthleteCard bind:team={team} bind:athlete={athlete} />
                        </div>
                    {/each}
                    <div class="flex justify-center">
                        <a href="/home/coach/create-program">
                            <button class="mt-5 text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
                                Add New Program
                            </button>
                        </a>
                    </div>
                </div>
            {:else if activeTab === 'programs'}
                <div class="rounded bg-gray-200 w-11/12 flex flex-col p-3">
                    <div>
                        <h2 class="text-center text-xl font-bold">All Programs</h2>
                        <select class="bg-gray-300 text-textblue p-1">
                            <option disabled selected>Filter By...</option>
                        </select>
                        <button class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                            Search
                        </button>
                    </div>
                    <a class="self-center" href="/home/coach/create-program">
                        <button class="mt-5 text-black bg-yellow p-2 rounded font-bold hover:bg-yellow-shade duration-300">
                            Add New Program
                        </button>
                    </a>
                    {#each $userDB?.coachData?.programs as program}
                        <div class="m-2">
                            <div>{program.name}</div>
                            <div>{dayjs(program.startDate).format('ddd MMM D')} - {dayjs(program.endDate).format('ddd MMM D')}</div>
                        </div>
                    {/each}
                </div>
            {:else}
                <div class="rounded bg-gray-200 w-11/12 flex flex-col p-3">
                    <div class="w-3/12 m-auto flex flex-col items-center m-2">
                        {#if team.teamLogo && !newLogoSrc}
                            <img src={team.teamLogo} alt="Team logo">
                        {:else if !team.teamLogo && !newLogoSrc}
                            <div class="text-xl font-bold">No Logo</div>
                        {:else if newLogoSrc}
                            <img src={newLogoSrc} alt="New logo">
                        {/if}
                        <input class="my-2 p-1 bg-gray-300 w-full" type="text" placeholder={team.teamLogo ? team.teamLogo : 'Paste an image URL'} bind:value={newLogoSrc}>
                        {#if newLogoSrc !== team.teamLogo}
                            <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                                Save
                            </button>
                        {/if}
                    </div>
                    <div class="flex flex-col items-start m-2">
                        {#if !showNameInput}
                            <div class="flex items-center">
                                <h4 class="font-semibold text-xl">Team Name: {team.name}</h4>
                                <div title="Edit team name" class="h-4 mx-2 text-textblue" on:click={() => showNameInput = !showNameInput}>
                                    <FaPen />
                                </div>
                            </div>
                        {:else}
                            <input class="w-4/12 bg-gray-300 p-1 text-textblue" bind:value={newTeamName} type="text">
                            <div class="w-4/12">
                                <button on:click={() => showNameInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                                    Cancel
                                </button>
                                {#if newTeamName !== team.name}
                                    <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                                        Save
                                    </button>
                                {/if}
                            </div>
                        {/if}
                    </div>
                    <div class="flex flex-col text-md m-2">
                        <div>
                            <h5 class="font-semibold text-xl">Description:</h5>
                        </div>
                        {#if !showDescriptionInput}
                            <div class="flex justify-start items-center">
                                <p><i>{team.description}</i></p>
                                <div title="Edit description" class="h-4 mx-2 text-textblue" on:click={() => showDescriptionInput = !showDescriptionInput}>
                                    <FaPen />
                                </div>
                            </div>
                        {:else}
                            <textarea class="w-6/12 bg-gray-300 text-textblue" bind:value={newTeamDescription}></textarea>
                            <div class="w-6/12">
                                <button on:click={() => showDescriptionInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                                    Cancel
                                </button>
                                {#if newTeamDescription !== team.description}
                                    <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                                        Save
                                    </button>
                                {/if}
                            </div>
                        {/if}
                    </div>
                    <div>
                        <button class="p-2 px-6 border-textblue border-2 rounded bg-gray-100 hover:bg-gray-300 m-2 mt-4">
                            Invite Athlete
                        </button>
                    </div>
                </div>
            {/if}
        </div>
    {/if}
{:catch error}
    <div>
        We're sorry, there was an error fetching your team
    </div>
{/await}

<style>
    .selected {
        font-weight: bold;
    }
</style>