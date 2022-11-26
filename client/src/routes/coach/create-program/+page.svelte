<script lang="ts">
    import {program} from "./store";
    import {Day} from "../../../lib/classes/program";
    import {get} from "svelte/store";

    const addDay = () => {
        program.update(p => {
            p.days.push(new Day())
            return p
        })
        console.log($program)
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
        {#each $program.days as day}
            <div>
                <p>Day</p>
            </div>
        {/each}
    </div>
    <div>
        <button on:click={addDay}>Add Day</button>
    </div>
</section>

<style>

</style>