<script lang="ts">
    import AthleteCard from '$lib/components/TeamDashboard/AthleteCard.svelte';
    import { team } from '$lib/stores/teamStore';
    import InviteUserModal from '$lib/components/modals/InviteUserModal.svelte';

    $: athleteList = $team ? $team.athletes : [];

    let showModal: boolean = false;

    const toggleInviteModal = () => {
        showModal = !showModal;
    };
</script>

<svelte:head>
    <title>{$team.name + ' Athletes'}</title>
    <meta
        name="description"
        content="Athlete dashboard for {$team?.name}. Manage your athletes"
    />
</svelte:head>

<div class="flex flex-col items-center">
    <h1 class="py-2 text-center text-3xl font-semibold text-white">
        {$team.name}
    </h1>
    <div class="flex w-screen flex-col px-3">
        {#each athleteList as athlete, idx (athlete.id)}
            <AthleteCard bind:athlete="{athlete}" />
        {/each}
        <div class="my-3 flex justify-center">
            <button
                on:click="{toggleInviteModal}"
                class="m-2 mt-4 rounded border-2 border-textblue bg-gray-100 p-2 px-6 hover:bg-gray-200"
            >
                Invite Athlete
            </button>
        </div>
        {#if showModal}
            <InviteUserModal bind:showModal="{showModal}" />
        {/if}
    </div>
</div>

<style>
</style>
