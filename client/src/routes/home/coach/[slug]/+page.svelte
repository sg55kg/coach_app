<script lang="ts">
    import CreateTeamModal from '$lib/components/modals/CreateTeamModal.svelte';
    import { userDB } from '$lib/stores/authStore';
    import TeamCard from '$lib/components/TeamCard.svelte';

    let showModal: boolean = false;

    const toggleModal = () => (showModal = !showModal);
</script>

<div class="flex w-screen flex-col items-center justify-center">
    <h1 class="my-2 text-2xl font-bold tracking-widest">My Teams</h1>
    <button
        on:click="{toggleModal}"
        class="my-3 rounded bg-yellow p-2 font-bold text-black duration-300 hover:bg-yellow-shade"
    >
        Create New Team
    </button>
    <CreateTeamModal open="{showModal}" on:closeModal="{toggleModal}" />
    {#if $userDB && $userDB?.coachData?.teams?.length > 0}
        {#each $userDB.coachData.teams as team}
            <TeamCard team="{team}" />
        {/each}
    {/if}
</div>

<style>
</style>
