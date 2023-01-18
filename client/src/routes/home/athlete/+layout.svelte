<script lang="ts">
    import {onDestroy} from "svelte";
    import {currentDay, currentProgram} from "$lib/stores/athleteProgramStore";
    import {Program} from "$lib/classes/program";
    import {Day} from "$lib/classes/program/day";



    onDestroy(() => {
        currentProgram.set(new Program())
        currentDay.set(new Day())
    })
</script>

<slot />

<style>

</style>