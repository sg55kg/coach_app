<script>
    import {Day} from "../../../lib/server/service/ProgramService.js";

    export let data
    const { program } = data

    const addDay = () => {
        newDay = new Day()
    }
    let showDayForm = false
    let newDay
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
{#if showDayForm}
    <div>

    </div>
{/if}
<button on:click={() => showDayForm = !showDayForm}>
    Add Day
</button>

<style>

</style>