<script lang="ts">
    import { page } from '$app/stores';
    import { ProgramService } from '../../../../../../lib/service/ProgramService';
    import { AthleteProgramStats } from '../../../../../../lib/classes/program/stats';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import ProgramToolbarDesktop from '$lib/components/WriteProgram/toolbars/ProgramToolbarDesktop.svelte';
    import { isMobile } from '$lib/stores/authStore.js';
    import ProgramStats from "$lib/components/WriteProgram/views/ProgramStats.svelte";

    let weeklyStats: AthleteProgramStats[];

    const fetchWeeklyStats = async () => {
        if (weeklyStats) return;

        const programId = $page.params.id;
        try {
            const res = await ProgramService.getWeeklyProgramStats(programId);
            console.log(res);
            weeklyStats = res;
            return res;
        } catch (e) {
            console.log(e);
        }
    };
</script>

<div>
    {#if !$isMobile}
        <ProgramToolbarDesktop />
    {/if}
    {#await fetchWeeklyStats()}
        <div
            class="flex h-screen w-screen flex-col items-center justify-center"
        >
            <LoadingSpinner
                spinnerColor="fill-yellow"
                height="h-10"
                width="w-10"
            />
            <p class="font-semibold">Fetching program stats...</p>
        </div>
    {:then stats}
        <ProgramStats stats="{stats}" />
    {:catch err}
        <p>Error</p>
    {/await}
</div>

<style>
</style>
