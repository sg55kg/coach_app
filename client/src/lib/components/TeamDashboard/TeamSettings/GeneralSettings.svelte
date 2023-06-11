<script lang="ts">
    import { team } from '$lib/stores/teamStore.js';
    import FaPen from 'svelte-icons/fa/FaPen.svelte';
    import RichTextEditor from '$lib/components/shared/texteditor/RichTextEditor.svelte';
    import { AthleteData } from '../../../classes/user/athlete';
    import { Team } from '../../../classes/team';

    let newLogoSrc: string = $team.teamLogo;
    let newTeamName: string = $team.name;
    let newTeamDescription: string = $team.description;
    let showNameInput: boolean = false;
    let showDescriptionInput: boolean = false;

    let athleteList: AthleteData[] = [];
    $: athleteList = $team ? $team.athletes : [];

    const saveTeam = async () => {
        const data = {
            ...$team,
            teamLogo: newLogoSrc,
            description: newTeamDescription,
            name: newTeamName.trim(),
        } as Team;
        try {
            const teamRes = await TeamService.updateTeam(data);
            $team = teamRes;
            athleteList = $team.athletes;
            showNameInput = false;
            showDescriptionInput = false;
        } catch (e) {
            console.log(e);
        }
    };
</script>

<div class="flex-grow flex-col">
    <h1 class="my-2 text-3xl font-semibold tracking-wider">General Settings</h1>
    <div class="m-auto m-2 flex flex-col items-center lg:w-3/12">
        {#if $team.teamLogo && !newLogoSrc}
            <img
                src="{$team.teamLogo}"
                alt="Team logo"
                class="h-44 w-44 rounded-full"
            />
        {:else if !$team.teamLogo && !newLogoSrc}
            <div class="text-xl font-bold">No Logo</div>
        {:else if newLogoSrc}
            <img
                src="{newLogoSrc}"
                alt="New logo"
                class="h-44 w-44 rounded-full"
            />
        {/if}
        <input
            class="my-2 w-full bg-gray-300 p-1"
            type="text"
            placeholder="{$team.teamLogo
                ? $team.teamLogo
                : 'Paste an image URL to your team logo'}"
            bind:value="{newLogoSrc}"
        />
        {#if newLogoSrc !== $team.teamLogo}
            <button
                on:click="{saveTeam}"
                class="m-1 rounded border-2 border-textblue bg-gray-100 p-1 px-5 hover:bg-gray-300"
            >
                Save
            </button>
        {/if}
    </div>
    <div class="m-2 flex flex-col items-start">
        {#if !showNameInput}
            <div class="flex flex-col justify-center text-left">
                <h4 class="self-start font-medium text-textblue">Team Name:</h4>
                <div class="flex">
                    <h4 class="text-left text-2xl">{$team.name}</h4>
                    <button
                        title="Edit team name"
                        class="mx-2 h-4 text-textblue"
                        on:click="{() => (showNameInput = !showNameInput)}"
                    >
                        <FaPen />
                    </button>
                </div>
            </div>
        {:else}
            <input
                class="w-4/12 bg-gray-300 p-1 text-textblue"
                bind:value="{newTeamName}"
                type="text"
            />
            <div class="w-4/12">
                <button
                    on:click="{() => (showNameInput = false)}"
                    class="m-1 rounded bg-gray-100 p-1 px-5 text-red-shade hover:bg-gray-300"
                >
                    Cancel
                </button>
                {#if newTeamName !== $team.name}
                    <button
                        on:click="{saveTeam}"
                        class="m-1 rounded border-2 border-textblue bg-gray-100 p-1 px-5 hover:bg-gray-300"
                    >
                        Save
                    </button>
                {/if}
            </div>
        {/if}
    </div>
    <div class="text-md m-2 flex flex-col">
        <div>
            <h4 class="font-medium text-textblue">Description:</h4>
        </div>
        {#if !showDescriptionInput}
            <div class="flex items-center justify-start">
                <RichTextEditor
                    content="{newTeamDescription}"
                    editable="{false}"
                />
                <button
                    title="Edit description"
                    class="mx-2 h-4 text-textblue"
                    on:click="{() =>
                        (showDescriptionInput = !showDescriptionInput)}"
                >
                    <FaPen />
                </button>
            </div>
        {:else}
            <RichTextEditor bind:content="{newTeamDescription}" />
            <div class="w-6/12">
                <button
                    on:click="{() => (showDescriptionInput = false)}"
                    class="m-1 rounded bg-gray-100 p-1 px-5 text-red-shade hover:bg-gray-300"
                >
                    Cancel
                </button>
                {#if newTeamDescription !== $team.description}
                    <button
                        on:click="{saveTeam}"
                        class="m-1 rounded border-2 border-textblue bg-gray-100 p-1 px-5 hover:bg-gray-300"
                    >
                        Save
                    </button>
                {/if}
            </div>
        {/if}
    </div>
</div>

<style></style>
