<script lang="ts">
    import { userDB } from '$lib/stores/authStore';
    import ProgramStats from '$lib/components/Stats/ProgramStats.svelte';
</script>

{#if $userDB.athleteData}
    <ProgramStats athlete="{$userDB.athleteData}" />
{:else}
    <div class="text-center text-lg font-semibold">
        Could not retrieve this athlete. Please try refreshing the page or
        navigating away and back.
    </div>
{/if}

<style>
</style>
