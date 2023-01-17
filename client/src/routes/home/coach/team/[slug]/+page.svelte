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

    export let data: PageServerData

    let team: Team = data.team ?? data.team
    let showNameInput: boolean = false
    $: athleteList = team ? team.athletes : []

    const saveTeam = async () => {
        if (!$auth0Client) return

        try {
            const teamRes = await TeamService.updateTeam($auth0Client, team)
            athleteList = team.athletes
            showNameInput = false
        } catch (e) {
            console.log(e)
        }
    }

</script>

<a href="/home/coach/{$userDB?.coachData?.id}" class="font-bold mx-4">{'<-'} Back to teams</a>
{#if team}
    <div class="flex flex-col items-center">
        <div class="rounded bg-gray-400 w-11/12 flex justify-start items-center p-3">
            {#if !showNameInput}
                <h1 class="font-bold text-3xl text-white">
                    {team.name}
                </h1>
                <div class="h-5 mx-2 text-textblue" on:click={() => showNameInput = !showNameInput}>
                    <FaPen />
                </div>
            {:else}
                <div class="flex flex-col">
                    <input class="font-bold text-3xl text-black" bind:value={team.name}>
                    <div class="flex flex-row justify-around">
                        <button on:click={saveTeam}>Save</button>
                        <button on:click={() => showNameInput = false}>Cancel</button>
                    </div>
                </div>
            {/if}
        </div>
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
    </div>
{/if}

<style>

</style>