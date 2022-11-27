<script lang="ts">
    import {program} from "./store";
    import {Day, Exercise} from "../../../lib/classes/program";

    let selectedDay: Day | null = null
    let selectedIndex: number = -1

    const addDay = () => {
        program.update(p => {
            p.days.push(new Day())
            return p
        })
        console.log($program)
    }

    const addExercise = () => {
        program.update(p => {
            p.days[selectedIndex].exercises.push(new Exercise())
            return p
        })
    }
</script>

<section>
    <div class="p-4">
        <div class="flex justify-center">
            <input type="text" placeholder="Program Name" bind:value={$program.name} class="p-1">
        </div>
        <div class="p-4 flex justify-around w-9/12 m-auto">
            <div class="flex flex-col">
                <label>Start Date</label>
                <input type="date"
                       bind:value={$program.startDate}
                       on:change={(e) => $program.startDate = new Date(e.target.value)}>
            </div>
            <div class="flex flex-col">
                <label>End Date</label>
                <input type="date"
                       bind:value={$program.endDate}
                       on:change={(e) => $program.endDate = new Date(e.target.value)}>
            </div>
        </div>
    </div>
    <hr>
    <div>
        {#each $program.days as day, index}
            <div on:click={() => { selectedDay = day; selectedIndex = index }}>
                <p>{`${day.date.toLocaleDateString("en-US")} ${day.exercises.length} exercise${day.exercises.length > 1 ? "s" : ''}`}</p>
            </div>
        {/each}
    </div>
    <div>
        <button on:click={addDay}>Add Day</button>
    </div>
    {#if selectedDay}
        {#each $program.days[selectedIndex].exercises as exercise}
            <div class="flex flex-col p-2 justify-items-center">
                <div class="flex flex-row p-2 justify-between">
                    <input type="text" placeholder="Exercise name" class="exercise-input" bind:value={exercise.name}>
                    <input type="number" placeholder="Weight" class="exercise-input" bind:value={exercise.weight}>
                    <input type="number" placeholder="Sets" class="exercise-input" bind:value={exercise.sets}>
                    <input type="number" placeholder="Reps" class="exercise-input" bind:value={exercise.repsPerSet}>
                </div>
                <textarea placeholder="Notes" class="exercise-input"></textarea>
            </div>
        {/each}
        <button on:click={addExercise}>Add exercise</button>
    {/if}
</section>

<style>
    .exercise-input {
        background: transparent;
        padding: .5em;
        font-weight: bold;
    }

    .exercise-input:focus {
        outline: none;
        background: lightgray;
    }
</style>