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
    <div>
        <div>
            <input type="text" placeholder="Program Name" bind:value={$program.name}>
        </div>
        <div>
            <label>Start Date</label>
            <input type="date"
                   bind:value={$program.startDate}
                   on:change={(e) => $program.startDate = new Date(e.target.value)}>
            <label>End Date</label>
            <input type="date"
                   bind:value={$program.endDate}
                   on:change={(e) => $program.endDate = new Date(e.target.value)}>
        </div>
    </div>
    <hr>
    <div>
        {#each $program.days as day, index}
            <div on:click={() => { selectedDay = day; selectedIndex = index }}>
                <p>{day.date.toLocaleDateString("en-US")}</p>
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
                    <input type="text" placeholder="Exercise name">
                    <input type="number" placeholder="Weight">
                    <input type="number" placeholder="Sets">
                    <input type="number" placeholder="Reps">
                </div>
                <textarea class="p-2" placeholder="Notes"></textarea>
            </div>
        {/each}
        <button on:click={addExercise}>Add exercise</button>
    {/if}
</section>

<style>

</style>