<script lang="ts">
    import {onMount} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import IncompleteExercise from "$lib/components/CurrentProgram/IncompleteExercise.svelte";
    import {currentDay, currentProgram} from "$lib/stores/athleteProgramStore";
    import {goto} from "$app/navigation";
    import {Program, type ProgramDTO} from "$lib/classes/program";


    const fetchCurrentProgram = async () => {
        if (!$userDB?.athleteData?.currentProgram) {
            return
        }
        try {
            const res = await fetch(`/api/athlete/program/${$userDB.athleteData.currentProgram.id}`)
            const programData: ProgramDTO = await res.json()
            const program = Program.build(programData)
            const today = dayjs()
            const day = program.days.find(d => dayjs(d.date).isSame(today, 'days'))
            if (day) {
                $currentDay = day
            }
            $currentProgram = program
        } catch (e) {
            console.log(e)
        }
    }

    onMount(async () => {
        if ($userDB && (!$userDB?.athleteData || $userDB.athleteData.records.length < 1)) {
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
            <h3 class="text-2xl font-bold tracking-wider">My Program</h3>
            <h5 class="my-3 text-xl">{dayjs().format('dddd, MMMM D')}</h5>
        </div>

        {#await fetchCurrentProgram()}
            <p>Loading your program...</p>
        {:then e}
            <div class="text-center lg:text-start">
                <a class="text-link hover:text-link-shade underline" href="/home/athlete/program">
                    View Full Program
                </a>
            </div>
            {#if $currentDay && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
                <div class="lg:m-4 flex flex-col justify-center lg:p-5 sm:p-2 md:p-2">
                    {#each $currentDay.exercises as exercise, index (exercise.id)}
                        <IncompleteExercise bind:exercise={exercise} />
                    {/each}
                </div>
            {:else if $currentDay && $currentDay?.isRestDay}
                <div>
                    Rest Day
                </div>
            {:else}
                <div class="py-4 text-center md:text-left">
                    No programming available for today
                </div>
            {/if}
            <div class="text-center lg:text-start mt-4">
                <a class="text-link hover:text-link-shade underline" href={`/home/athlete/${$userDB?.athleteData?.id}`}>
                    My Progress
                </a>
            </div>
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
<!--            <a class="text-lg underline text-link text-center" href="/home/athlete/team">View</a>-->
            <a class="text-lg underline text-link text-center" href="/home/athlete/teams">Browse Teams</a>
        {:else}
            <a class="text-lg text-center md:text-left underline text-link" href="/home/athlete/teams">Join a team</a>
        {/if}
    </div>
</div>


<style>

</style>