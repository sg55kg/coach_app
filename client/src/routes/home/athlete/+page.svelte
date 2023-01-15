<script lang="ts">
    import {onMount} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import dayjs from "dayjs";
    import IncompleteExercise from "$lib/components/CurrentProgram/IncompleteExercise.svelte";
    import {currentDay} from "$lib/stores/athleteProgramStore";
    import {goto} from "$app/navigation";


    onMount(async () => {
        if (!$userDB?.athleteData || $userDB.athleteData.records.length < 1) {
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


        {#if $currentDay && !$currentDay?.isRestDay && $currentDay?.exercises?.length > 0}
            <div class="lg:m-4 flex flex-col justify-center lg:p-5 sm:p-2 md:p-2">
                {#each $currentDay.exercises as exercise, index (index)}
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
        <div class="sm: text-center lg:text-start">
            <a class="text-link underline" href="/home/athlete/program">View Full Program</a>
        </div>

    </div>
    <hr class="hidden md:flex m-2 mx-24">
    <div class="m-4 flex flex-col">
        {#if $userDB?.athleteData?.team}
            <h4 class="text-2xl text-center">{$userDB?.athleteData?.team?.name}</h4>
            <a class="text-lg underline text-link text-center" href="/home/athlete/team">View</a>
            <a class="text-lg underline text-link text-center" href="/home/athlete/teams">Browse Teams</a>
        {:else}
            <a class="text-lg text-center md:text-left underline text-link" href="/home/athlete/teams">Join a team</a>
        {/if}
    </div>
</div>


<style>

</style>