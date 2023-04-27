<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import {getContext, onDestroy, onMount, setContext} from "svelte";
import {Program, type ProgramDTO} from "$lib/classes/program";
import dayjs, {Dayjs} from "dayjs";
import AthleteProgramOverview from "$lib/components/AthleteProgram/AthleteProgramOverview.svelte";
import {athleteProgramContext} from "../../../../lib/contexts/athleteProgramContext";
import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
import MdClose from 'svelte-icons/md/MdClose.svelte'

setContext('athlete-program', athleteProgramContext)

const {
    getCurrentProgram,
    getCurrentDay,
    getAthleteProgramLoading,
    getAthleteProgramError,
    getAthleteProgramSuccess,
    getAthleteId
} = getContext('athlete-program')

const currentProgram = getCurrentProgram()
const currentDay = getCurrentDay()
const loading = getAthleteProgramLoading()
const error = getAthleteProgramError()
const success = getAthleteProgramSuccess()
const athleteId = getAthleteId()

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
    $currentProgram = $userDB.athleteData.currentProgram
    if (!$currentProgram.id) {
        fetchCurrentProgram()
    }
    $athleteId = $userDB.athleteData.id
})

onDestroy(() => {
})
</script>

<svelte:head>
    <title>Current Program</title>
    <meta name="description" content="View your current program" />
</svelte:head>

{#if $currentProgram}
    <AthleteProgramOverview />
{:else}
    <div class="text-center">
        You do not have a program currently
    </div>
{/if}

{#if $loading}
    <div class="fixed z-[110] top-0 bottom-0 right-0 left-0 bg-gray-300 opacity-25">

    </div>
    <div class="fixed bottom-0 top-0 right-0 left-0 z-[115] flex items-center justify-center">
        <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
    </div>

{/if}
{#if $success}
    <div class="sticky bottom-5 left-10 z-10 text-green border-l-4 border-l-green bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
        {$success}
        <button class="h-8 w-8 hover:bg-gray-400 text-green-dark rounded-full hover:text-green p-1" on:click={() => $success = ''}>
            <MdClose />
        </button>
    </div>
{:else if $error}
    <div class="sticky bottom-5 left-10 z-10 text-red border-l-4 border-l-red-shade bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
        {$error}
        <button class="h-8 w-8 hover:bg-gray-400 rounded-full hover:text-red-shade p-1" on:click={() => $error = ''}>
            <MdClose />
        </button>
    </div>
{/if}

<style>

</style>