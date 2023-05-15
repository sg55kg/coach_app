<script lang="ts">
    import {Team} from "$lib/classes/team";
    import {TeamService} from "$lib/service/TeamService";
    import FaPen from 'svelte-icons/fa/FaPen.svelte'
    import {team} from "$lib/stores/teamStore";

    let newLogoSrc: string = $team.teamLogo
    let newTeamName: string = $team.name
    let newTeamDescription: string = $team.description

    let showNameInput: boolean = false
    let showDescriptionInput: boolean = false

    $: athleteList = $team ? $team.athletes : []

    let loadingTeam: boolean = true

    const saveTeam = async () => {
        const data = {
            ...$team,
            teamLogo: newLogoSrc,
            description: newTeamDescription,
            name: newTeamName.trim()
        } as Team
        try {
            const teamRes = await TeamService.updateTeam(data)
            $team = teamRes
            athleteList = $team.athletes
            showNameInput = false
            showDescriptionInput = false
        } catch (e) {
            console.log(e)
        }
    }
</script>

<div class="w-11/12 flex flex-col p-3">
    <div class="w-11/12 lg:w-3/12 m-auto flex flex-col items-center m-2">
        {#if $team.teamLogo && !newLogoSrc}
            <img src={$team.teamLogo} alt="Team logo">
        {:else if !$team.teamLogo && !newLogoSrc}
            <div class="text-xl font-bold">No Logo</div>
        {:else if newLogoSrc}
            <img src={newLogoSrc} alt="New logo">
        {/if}
        <input class="my-2 p-1 bg-gray-300 w-full" type="text" placeholder={$team.teamLogo ? $team.teamLogo : 'Paste an image URL'} bind:value={newLogoSrc}>
        {#if newLogoSrc !== $team.teamLogo}
            <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                Save
            </button>
        {/if}
    </div>
    <div class="flex flex-col items-start m-2">
        {#if !showNameInput}
            <div class="flex flex-col items-center">
                <h4 class="font-semibold text-xl">Team Name:</h4>
                <div class="flex">
                    <h4>{$team.name}</h4>
                    <button title="Edit team name" class="h-4 mx-2 text-textblue" on:click={() => showNameInput = !showNameInput}>
                        <FaPen />
                    </button>
                </div>
            </div>
        {:else}
            <input class="w-4/12 bg-gray-300 p-1 text-textblue" bind:value={newTeamName} type="text">
            <div class="w-4/12">
                <button on:click={() => showNameInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                    Cancel
                </button>
                {#if newTeamName !== $team.name}
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
                <p><i>{$team.description}</i></p>
                <button title="Edit description" class="h-4 mx-2 text-textblue" on:click={() => showDescriptionInput = !showDescriptionInput}>
                    <FaPen />
                </button>
            </div>
        {:else}
            <textarea class="w-6/12 bg-gray-300 text-textblue" bind:value={newTeamDescription}></textarea>
            <div class="w-6/12">
                <button on:click={() => showDescriptionInput = false} class="p-1 px-5 m-1 rounded bg-gray-100 hover:bg-gray-300 text-red-shade">
                    Cancel
                </button>
                {#if newTeamDescription !== $team.description}
                    <button on:click={saveTeam} class="p-1 px-5 border-textblue border-2 m-1 rounded bg-gray-100 hover:bg-gray-300">
                        Save
                    </button>
                {/if}
            </div>
        {/if}
    </div>
<!--    <div>-->
<!--        <button class="p-2 px-6 border-textblue border-2 rounded bg-gray-100 hover:bg-gray-300 m-2 mt-4">-->
<!--            Invite Athlete-->
<!--        </button>-->
<!--    </div>-->
    <hr>
    <div>
        <h3>Payment Information</h3>
        <button class="text-link font-semibold">
            Connect with Stripe to enable payments
        </button>
    </div>
</div>

<style>

</style>