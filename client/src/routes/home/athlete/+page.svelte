<script lang="ts">
    import {onMount, setContext} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import {goto} from "$app/navigation";
    import {Program, type ProgramDTO} from "$lib/classes/program";
    import AthleteExpandedDay from "$lib/components/AthleteProgram/AthleteExpandedDay.svelte";
    import type {Writable} from "svelte/store";
    import {Day} from "../../../lib/classes/program/day";
    import {writable} from "svelte/store";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";


    const currentDay: Writable<Day> = writable(new Day())
    const currentProgram: Writable<Program> = writable(new Program())

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

    setContext('athlete-program', {
        getCurrentProgram: () => currentProgram,
        getCurrentDay: () => currentDay
    })

    onMount(async () => {
        if ($userDB && (!$userDB?.athleteData )) {
            await goto(`/home/athlete/get-started/${$userDB.id}`)
        }
    })


</script>

<div class="flex flex-col">
    <div class="sm:text-center md:text-center lg:text-start m-2 flex justify-center lg:justify-start">
        <h1 class="font-bold text-xl w-fit">Welcome {$userDB?.username ? $userDB.username : ''}</h1>
    </div>
    <div class="lg:m-4">
        <div class="flex flex-col items-center lg:items-start">
<!--            <h3 class="text-2xl font-bold tracking-wider">My Program</h3>-->
<!--            <h5 class="my-3 text-xl">{dayjs().format('dddd, MMMM D')}</h5>-->
        </div>

        {#await fetchCurrentProgram()}
            <div class="w-screen p-4 flex items-center justify-center">
                <LoadingSpinner spinnerColor="fill-yellow" height="10" width="10" />
            </div>
        {:then e}
            {#if $currentDay.id && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
<!--                <div class="lg:m-4 flex flex-col justify-center lg:p-5 sm:p-2 md:p-2">-->
<!--                    {#each $currentDay.exercises as exercise, index (exercise.id)}-->
<!--                        {#if exercise.type === ExerciseType.EXERCISE}-->
<!--                            <IncompleteExercise bind:exercise={exercise} />-->
<!--                        {:else}-->
<!--                            <AthleteComplexExercise bind:exercise={exercise} />-->
<!--                        {/if}-->
<!--                    {/each}-->
<!--                </div>-->
                <AthleteExpandedDay />
            {:else if $currentDay && $currentDay?.isRestDay}
                <div>
                    Rest Day
                </div>
            {:else}
                <div class="py-4 text-center md:text-left">
                    No programming available for today
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


<style>

</style>