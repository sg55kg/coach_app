<script lang="ts">
    import { getContext, onMount } from 'svelte';
    import FaPlus from 'svelte-icons/fa/FaPlus.svelte';
    import { userDB } from '../../../stores/authStore';
    import ReadOnlyExerciseRow from "$lib/components/shared/layout/ReadOnlyExerciseRow.svelte";

    const { getProgram, getSelectedDayIdx, addDay } = getContext('program');
    let program = getProgram();
    let selectedDayIdx = getSelectedDayIdx();

    onMount(() => {
        const selectedDayCard = document.getElementById(
            `day-${$selectedDayIdx}`
        );
        if (selectedDayCard) {
            selectedDayCard.scrollIntoView({
                behavior: 'smooth',
                block: 'center',
            });
        }
    });

    let athleteName: string = '';
    $: athleteName = (() => {
        if ($program.athleteId === $userDB?.athleteData.id) {
            return $userDB?.username;
        }
        if ($program.athleteId) {
            const athlete = $userDB!.coachData!.athletes.find(
                a => a.id === $program.athleteId
            );
            if (athlete) {
                return athlete.name;
            } else {
                return 'Athlete Unavailable';
            }
        }
        return 'Template';
    })();
</script>

<div class="flex h-full w-4/12 flex-col overflow-y-hidden bg-gray-300 pb-16">
    <div class="flex flex-col p-2 font-medium text-textgray text-yellow-lt">
        <p>{athleteName}</p>
        <p>{$program.name}</p>
        {#if $program.athleteId}
            <p>
                {$program.startDate.format("MMM DD 'YY")} - {$program.endDate.format(
                    "MMM DD 'YY"
                )}
            </p>
        {/if}
    </div>
    <div class="flex h-full flex-grow flex-col overflow-y-auto">
        {#each $program.days as day, idx}
            <div
                id="day-{idx}"
                class="relative flex flex-col p-6 pt-10 {$selectedDayIdx === idx
                    ? 'bg-gray-100'
                    : 'bg-gray-200'} hover:cursor-pointer"
                on:click="{() => ($selectedDayIdx = idx)}"
            >
                <span
                    class="absolute left-0 top-0 p-2 text-lg font-bold {$selectedDayIdx ===
                    idx
                        ? 'text-yellow'
                        : 'text-link'}">{idx + 1}</span
                >
                {#each day.exercises as exercise, i}
                    <ReadOnlyExerciseRow
                        exercise="{exercise}"
                        showName="{true}"
                    />
                {/each}
                {#if !day.exercises.length && !day.isRestDay}
                    <p class="">No Exercises</p>
                {:else if day.isRestDay}
                    <p
                        class="text-medium text-center font-semibold text-textblue"
                    >
                        Rest Day
                    </p>
                {/if}
            </div>
        {/each}
        <button
            class="my-2 h-6 w-6 self-center text-yellow"
            on:click="{addDay}"
        >
            <FaPlus />
        </button>
    </div>
</div>

<style></style>
