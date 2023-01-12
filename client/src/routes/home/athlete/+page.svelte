<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import {ProgramService} from "$lib/service/ProgramService";
    import IncompleteExercise from "$lib/components/CurrentProgram/IncompleteExercise.svelte";
    import {currentProgram, currentDay} from "$lib/stores/athleteProgramStore";
    import {Program} from "$lib/classes/program";
    import {Day} from "$lib/classes/program/day";


    onMount(async () => {
        if (!$userDB?.athleteData?.currentProgram) {
            return
        }

        try {
            const program = await ProgramService.getProgram($auth0Client!, $userDB.athleteData.currentProgram.id)
            console.log('program Response', program)
            const today = dayjs()

            let day = program.days.find(d => dayjs(d.date).isSame(today, 'days'))
            if (day) {
                currentDay.set(day)
                console.log('currentDay',currentDay)
            }
            currentProgram.set(program)
            console.log(currentProgram)
        } catch (e) {
            console.log(e)
        }

    })

    onDestroy(() => {
        currentProgram.set(new Program())
        currentDay.set(new Day())
    })
</script>

<div class="flex flex-col">
    <div class="sm:text-center md:text-center lg:text-start m-2 flex justify-center lg:justify-start">
        <h1 class="font-bold text-2xl w-fit">Welcome {$userDB?.username}</h1>
    </div>
    <div class="m-4">
        <div class="flex flex-col items-center lg:items-start">
            <h3 class="text-xl font-bold">My Program</h3>
            <h5 class="text-xl">{dayjs().format('dddd, MMMM D')}</h5>
        </div>


        {#if $currentDay && $currentDay.isRestDay === false && $currentDay.exercises.length > 0}
            <div class="m-4 flex flex-col justify-center lg:p-5 sm:p-2 md:p-2">
                {#each $currentDay.exercises as exercise, index (index)}
                    <IncompleteExercise bind:exercise={exercise} />
                {/each}
            </div>
        {:else if $currentDay && $currentDay.isRestDay}
            <div>
                Rest Day
            </div>
        {:else}
            <div>
                No programming available for today
            </div>
        {/if}
        <div class="sm: text-center lg:text-start">
            <a class="text-link underline" href="/home/athlete/program">View Full Program</a>
        </div>

    </div>
    <hr class="m-2 mx-24">
    <div class="m-4">
        <h3 class="text-xl font-bold" href="/home/athlete/team">My Team</h3>
        {#if $userDB?.athleteData?.team}
            <h4 class="text-2xl text-center">{$userDB.athleteData.team.name}</h4>
            <a class="text-lg underline text-link" href="/home/athlete/team">View</a>
        {:else}
            <a class="text-lg underline text-link" href="/home/athlete/teams">Join a team</a>
        {/if}
    </div>
</div>


<style>

</style>