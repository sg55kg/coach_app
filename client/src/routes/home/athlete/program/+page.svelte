<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import CurrentProgram from "$lib/components/CurrentProgram/CurrentProgram.svelte";
import CurrentProgramOverview from "$lib/components/CurrentProgram/CurrentProgramOverview.svelte";
import {onDestroy, onMount} from "svelte";
import {completedExercises, currentDay, currentProgram, incompleteExercises} from "$lib/stores/athleteProgramStore";
import {Program, type ProgramDTO} from "$lib/classes/program";
import dayjs, {Dayjs} from "dayjs";

let viewOverview: boolean = true

const fetchCurrentProgram = async () => {
    if (!$userDB?.athleteData?.currentProgram) {
        return
    }
    try {
        const res = await fetch(`/api/athlete/program/${$userDB.athleteData.currentProgram.id}`)
        const programData: ProgramDTO = await res.json()
        const program = Program.build(programData)
        program.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
        const today = dayjs()
        const day = program.days.find(d => dayjs(d.date).isSame(today, 'days'))
        if (day) {
            day.exercises.sort((a, b) => a.order - b.order)
            $currentDay = day
        }
        $currentProgram = program
    } catch (e) {
        console.log(e)
    }
}

onMount(() => {
    if (!$currentProgram) {
        fetchCurrentProgram()
    }
})

onDestroy(() => {
    currentProgram.set(null)
    currentDay.set(null)
    incompleteExercises.set([])
    completedExercises.set([])
})
</script>

<svelte:head>
    <title>Current Program</title>
    <meta name="description" content="View your current program" />
</svelte:head>

{#if $currentProgram}
    {#if viewOverview}
        <CurrentProgramOverview bind:viewOverview={viewOverview} />
    {:else}
        <CurrentProgram bind:viewOverview={viewOverview} currentProgramId={$userDB.athleteData.currentProgram.id} />
    {/if}
{:else}
    <div class="text-center">
        You do not have a program currently
    </div>
{/if}

<style>

</style>