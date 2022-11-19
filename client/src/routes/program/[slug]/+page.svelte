<script lang="ts">
    import {Day} from "../../../lib/classes/program";

    export let data
    const { program } = data
    console.log(program)

    let showDayForm = false
    let newDay: Day | null = null

    const addDay = () => {
        showDayForm = !showDayForm
        newDay = new Day()
        console.log('fired')
    }

</script>

<h1>{program.name}</h1>
{#each program.days as day}
    <div>
        <h2>{day.date.toLocaleDateString('en-US')}</h2>
        {#each day.exercises as exercise}
            <p>{exercise.name}</p>
        {/each}
    </div>
{/each}
{#if showDayForm && newDay}
    <div>
        {#each newDay.exercises as exercise}
            <div>Test</div>
        {/each}
        <div>
            <input type="text">
            <input type="number">
            <input type="number">
            <input type="number">
            <textarea></textarea>
        </div>
    </div>
{/if}
<button on:click={addDay}>
    Add Day
</button>

<style>

</style>