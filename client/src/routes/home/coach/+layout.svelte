<script lang="ts">
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {displayPrograms, programError} from "$lib/stores/programStore";
    import {onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {DisplayProgram} from "$lib/classes/program";


    onMount(async () => {
        if (!$auth0Client || !$userDB) return
        //displayPrograms.set($userDB.coachData!.programs!)

        const programs = await ProgramService.getCoachPrograms($auth0Client, $userDB.coachData!.id!)
        displayPrograms.set(programs.map(p => DisplayProgram.build(p)))
    })
</script>

<slot />
{#if $programError}
    <div class="fixed bottom-0 p-2 m-2 bg-red-300 border-2 border-red-900 rounded">
        <p>{$programError}</p>
    </div>
{/if}

<style>

</style>