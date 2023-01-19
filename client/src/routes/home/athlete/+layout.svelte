<script lang="ts">
    import {onDestroy} from "svelte";
    import {currentDay, currentProgram} from "$lib/stores/athleteProgramStore";
    import {Program} from "$lib/classes/program";
    import {Day} from "$lib/classes/program/day";
    import {userDB} from "$lib/stores/authStore";

    onDestroy(() => {
        currentProgram.set(new Program())
        currentDay.set(new Day())
    })
</script>

<svelte:head>
    <title>{'Welcome ' + $userDB?.athleteData.name}</title>
    <meta name="description" content="Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable." />
</svelte:head>

<slot />

<style>

</style>