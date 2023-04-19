<script lang="ts">
    import {getContext, onDestroy, onMount, setContext} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import {goto} from "$app/navigation";
    import {Program, type ProgramDTO} from "$lib/classes/program";
    import AthleteExpandedDay from "$lib/components/AthleteProgram/AthleteExpandedDay.svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {athleteProgramContext} from "../../../lib/contexts/athleteProgramContext.js";
    import {Day} from "$lib/classes/program/day";
    import MdClose from 'svelte-icons/md/MdClose.svelte'

    setContext('athlete-program', athleteProgramContext)

    const { getCurrentProgram, getCurrentDay, getAthleteProgramError, getAthleteProgramLoading, getAthleteProgramSuccess } = athleteProgramContext
    const currentProgram = getCurrentProgram()
    const currentDay = getCurrentDay()
    const error = getAthleteProgramError()
    const loading = getAthleteProgramLoading()
    const success = getAthleteProgramSuccess()

    const fetchCurrentProgram = async () => {
        if (!$userDB?.athleteData?.currentProgram) {
            return
        }
        try {
            // const res = await fetch(`/api/athlete/program/${$userDB.athleteData.currentProgram.id}`)
            // const programData: ProgramDTO = await res.json()
            // console.log('program', programData)
            // const program = Program.build(programData)
            // program.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
            // const today = dayjs()
            // const day = program.days.find(d => dayjs(d.date).isSame(today, 'days'))
            //
            // if (day && !day.isRestDay && day.exercises.length > 0) {
            //     day.exercises.sort((a, b) => a.order - b.order)
            //     $currentDay = day
            // }
            $currentProgram = $userDB.athleteData.currentProgram
        } catch (e) {
            console.log(e)
        }
    }


    onMount(async () => {
        if ($userDB && (!$userDB?.athleteData )) {
            await goto(`/home/athlete/get-started/${$userDB.id}`)
        }
    })

    onDestroy(() => {
        $currentDay = new Day()
        $currentProgram = new Program()
    })

</script>

<div class="flex flex-col">
    <div class="sm:text-center md:text-center lg:text-start m-2 flex justify-center lg:justify-start">
        <h1 class="font-bold text-xl w-fit">Welcome {$userDB?.username ? $userDB.username : ''}</h1>
    </div>
    <div class="lg:m-4">
        {#await fetchCurrentProgram()}
            <div class="w-screen p-4 flex items-center justify-center">
                <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            </div>
        {:then e}
            {#if $currentDay?.id && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
                <AthleteExpandedDay />
            {:else if $currentDay && $currentDay?.isRestDay}
                <div class="flex flex-col">
                    Rest Day
                    <a class="text-link font-medium text-md" href="/home/athlete/program">View Full Program</a>
                </div>
            {:else}
                <div class="py-4 text-center md:text-left flex flex-col">
                    No programming available for today
                    <a class="text-link font-medium text-md" href="/home/athlete/program">View Full Program</a>
                </div>
            {/if}
        {:catch e}
            <p>Error: could not retrieve your program</p>
        {/await}
    </div>
    <div class="m-4 flex flex-col">
        {#if $userDB?.athleteData?.team}
            <h4 class="text-2xl text-center">{$userDB?.athleteData?.team?.name ? $userDB.athleteData.team.name : 'Your Team'}</h4>
            <p class="text-center m-2">
                As we continue to add features to the site, more information about your team will become available here like team-wide messages from your coach
            </p>
        {:else}
            <a class="text-lg text-center md:text-left underline text-link" href="/home/athlete/teams">Join a team</a>
        {/if}
    </div>
</div>

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