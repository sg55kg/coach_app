<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import {getContext, onDestroy, onMount, setContext} from "svelte";
import {Program, type ProgramDTO} from "$lib/classes/program";
import dayjs, {Dayjs} from "dayjs";
import AthleteProgramOverview from "$lib/components/AthleteProgram/AthleteProgramOverview.svelte";
import {athleteProgramContext} from "../../../../lib/contexts/athleteProgramContext";
import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";

setContext('athlete-program', athleteProgramContext)

const { getCurrentProgram, getCurrentDay, markDayCompleteAsWritten, getAthleteProgramLoading } = getContext('athlete-program')
const currentProgram = getCurrentProgram()
const currentDay = getCurrentDay()
const loading = getAthleteProgramLoading()

const fetchCurrentProgram = async () => {
    if (!$userDB?.athleteData?.currentProgram) {
        return
    }
    $loading = true
    try {
        const res = await fetch(`/api/athlete/program/${$userDB.athleteData.currentProgram.id}`)
        const programData: ProgramDTO = await res.json()
        const program = Program.build(programData)
        program.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
        $currentProgram = program
    } catch (e) {
        console.log(e)
    } finally {
        $loading = false
    }
}

onMount(() => {
    if (!$currentProgram.id) {
        fetchCurrentProgram()
    }
})

onDestroy(() => {
})
</script>

<svelte:head>
    <title>Current Program</title>
    <meta name="description" content="View your current program" />
</svelte:head>

{#if $currentProgram}
    <!--{#if viewOverview}-->
    <!--    <CurrentProgramOverview bind:viewOverview={viewOverview} />-->
    <!--{:else}-->
    <!--    <CurrentProgram bind:viewOverview={viewOverview} currentProgramId={$userDB.athleteData.currentProgram.id} />-->
    <!--{/if}-->
    <AthleteProgramOverview />
{:else if $loading}
    <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
{:else}
    <div class="text-center">
        You do not have a program currently
    </div>
{/if}

<style>

</style>