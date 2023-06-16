<script lang="ts">
    import {getContext, onMount} from "svelte";
    import ReadOnlyExerciseRow from "$lib/components/WriteProgram/views/ReadOnlyExerciseRow.svelte";
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import {userDB} from "../../../stores/authStore";

    const { getProgram, getSelectedDayIdx, getSelectedDay, addDay } = getContext('program');
    let program = getProgram();
    let selectedDayIdx = getSelectedDayIdx();
    let selectedDay = getSelectedDay();

    onMount(() => {
        const selectedDayCard = document.getElementById(`day-${$selectedDayIdx}`);
        if (selectedDayCard) {
            selectedDayCard.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    });

    let athleteName: string = '';
    $: athleteName = $program.athleteId ? $userDB!.coachData!.athletes.find(a => a.id === $program.athleteId).name : 'Template';
</script>

<div class="flex flex-col w-4/12 h-full overflow-y-hidden bg-gray-300 pb-16">
    <div class="flex flex-col text-textgray p-2 font-medium text-yellow-lt">
        <p>{athleteName}</p>
        <p>{$program.name}</p>
        {#if $program.athleteId}
            <p>{$program.startDate.format('MMM DD \'YY')} - {$program.endDate.format('MMM DD \'YY')}</p>
        {/if}
    </div>
    <div class="flex flex-col flex-grow overflow-y-auto h-full">
        {#each $program.days as day, idx}
            <div id="day-{idx}"
                 class="relative flex flex-col p-6 pt-10 {$selectedDayIdx === idx ? 'bg-gray-100' : 'bg-gray-200'} hover:cursor-pointer"
                 on:click={() => { $selectedDayIdx = idx; $selectedDay = $program.days[idx] }}
            >
                <span class="text-lg font-bold absolute left-0 top-0 p-2 {$selectedDayIdx === idx ? 'text-yellow' : 'text-link'}">{idx + 1}</span>
                {#each day.exercises as exercise, i}
                    <ReadOnlyExerciseRow exercise="{exercise}" showName="{true}" />
                {/each}
                {#if !day.exercises.length && !day.isRestDay}
                    <p class="">No Exercises</p>
                {:else if day.isRestDay}
                    <p class="text-center text-medium font-semibold text-textblue">Rest Day</p>
                {/if}
            </div>
        {/each}
        <button
                class="my-2 h-6 w-6 text-yellow self-center"
                on:click="{addDay}"
        >
            <FaPlus />
        </button>
    </div>
</div>

<style></style>