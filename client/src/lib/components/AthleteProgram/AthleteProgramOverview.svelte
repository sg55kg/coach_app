<script lang="ts">
    import {getContext} from "svelte";
    import {isMobile} from "$lib/stores/authStore.js";
    import AthleteDayCard from "$lib/components/AthleteProgram/AthleteDayCard.svelte";
    import AthleteExpandedDay from "$lib/components/AthleteProgram/AthleteExpandedDay.svelte";

    const { getCurrentProgram, getCurrentDay, markDayCompleteAsWritten, getAthleteProgramLoading } = getContext('athlete-program')
    const currentProgram = getCurrentProgram()
    const currentDay = getCurrentDay()
    const loading = getAthleteProgramLoading()

    let daysContainer: HTMLDivElement
    let contextCoordinates: {x: number, y: number} = {x: -1, y: -1}
$: console.log('CURRENT DAY', $currentDay)
</script>

<div bind:this={daysContainer} class="relative w-screen flex flex-col h-[83vh] overflow-y-auto pb-32">
    <div class="grid {$isMobile ? 'grid-cols-1 gap-10' : 'grid-cols-7 gap-4'} w-full p-3" >
        {#each $currentProgram.days as day, index (day.id+index)}
            <AthleteDayCard bind:day={day}
                     idx={index}
                     container={daysContainer}
                     bind:contextCoordinates={contextCoordinates}
            />
        {/each}
    </div>
</div>
{#if $currentDay}
    <AthleteExpandedDay />
{/if}

<style></style>