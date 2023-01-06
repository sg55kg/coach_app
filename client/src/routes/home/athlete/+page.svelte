<script lang="ts">
    import {onMount} from "svelte";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {Day} from "$lib/classes/program/day";
    import dayjs from "dayjs";
    import {ProgramService} from "$lib/service/ProgramService";

    let currentDay: Day

    onMount(async () => {
        if (!$userDB?.athleteData?.currentProgram) return

        const program = await ProgramService.getProgram($auth0Client!, $userDB.athleteData.currentProgram.id)
        console.log('program Response', program)
        const today = dayjs()

        let day = program.days.find(d => {
            return dayjs(d.date).isSame(today, 'days')
        })
        if (day) {
            currentDay = day
            console.log(currentDay)
        }
    })

</script>

<div class="flex flex-col">
    <h1 class="font-bold text-2xl">Athlete Home</h1>
<!--    <div class="flex justify-around mt-6 font-bold text-link">-->
<!--        <a href="/home/athlete/program">My Program</a>-->
<!--        <a href="/home/athlete/team">My Team</a>-->
<!--        <a href="/home/athlete/teams">Teams</a>-->
<!--    </div>-->
    <div class="m-4">
        <h3 class="text-xl font-bold">My Program</h3>
        <h5 class="text-lg">Today:</h5>

        {#if currentDay && !currentDay.isRestDay && !currentDay.exercises.length > 0}
            <div>
                {#each currentDay.exercises as exercise, index (index)}
                    <p>{exercise.name + ": " + exercise.weight + "lbs " + exercise.sets + "x" + exercise.repsPerSet}</p>
                {/each}
            </div>
        {:else if currentDay && currentDay.isRestDay}
            <div>
                Rest Day
            </div>
        {:else}
            <div>
                No programming available for today
            </div>
        {/if}
        <a class="text-link underline" href="/home/athlete/program">View Full Program</a>
    </div>
    <hr class="m-2 mx-24">
    <div class="m-4">
        <h3 class="text-xl font-bold" href="/home/athlete/team">My Team</h3>
        {#if $userDB?.athleteData?.team}
            <a class="text-lg underline text-link" href="/home/athlete/team">View</a>
        {:else}
            <a class="text-lg underline text-link" href="/home/athlete/teams">Join a team</a>
        {/if}
    </div>
</div>


<style>

</style>