<script lang="ts">
    import { getContext, onMount } from 'svelte';
    import { isMobile, userDB } from '$lib/stores/authStore.js';
    import AthleteDayCard from '$lib/components/AthleteProgram/AthleteDayCard.svelte';
    import AthleteExpandedDay from '$lib/components/AthleteProgram/AthleteExpandedDay.svelte';
    import { Day } from '$lib/classes/program/day/index.js';
    import Toggle from '$lib/components/shared/layout/Toggle.svelte';
    import UserService from '../../service/UserService';
    import { ProgramService } from '../../service/ProgramService';
    import { AthleteProgramStats } from '../../classes/program/stats';

    const {
        getCurrentProgram,
        getCurrentDay,
        markDayCompleteAsWritten,
        getAthleteProgramLoading,
        getCurrentDayIdx,
    } = getContext('athlete-program');
    const currentProgram = getCurrentProgram();
    const currentDay = getCurrentDay();
    const loading = getAthleteProgramLoading();
    const currentIdx = getCurrentDayIdx();

    let daysContainer: HTMLDivElement;
    let contextCoordinates: { x: number; y: number } = { x: -1, y: -1 };
</script>

<div
    bind:this="{daysContainer}"
    class="relative flex h-[90vh] w-screen flex-col overflow-y-auto pb-32"
>
    <div
        class="grid {$isMobile
            ? 'grid-cols-1 gap-10'
            : 'grid-cols-7 gap-4'} w-full p-3"
    >
        {#each $currentProgram?.days as day, index (day.id + index)}
            <AthleteDayCard
                bind:day="{day}"
                idx="{index}"
                container="{daysContainer}"
                bind:contextCoordinates="{contextCoordinates}"
            />
        {/each}
    </div>
</div>
{#if $currentDay && $currentIdx > -1}
    <div
        class="fixed top-0 right-0 bottom-0 left-0 z-10"
        on:click="{() => {
            $currentDay = undefined;
            $currentIdx = -1;
        }}"
    ></div>
    <div
        class="fixed z-20 {$isMobile
            ? 'top-0 right-0 left-0 bottom-0'
            : 'top-20 right-0 left-0 bottom-0'}"
    >
        <AthleteExpandedDay />
    </div>
{/if}

<style></style>
