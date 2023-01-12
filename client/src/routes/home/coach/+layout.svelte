<script lang="ts">
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {displayPrograms} from "$lib/stores/programStore";
    import {onMount} from "svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {DisplayProgram} from "$lib/classes/program";
    import {programSuccess, programError} from "$lib/stores/writeProgramStore.js";
    import UserService from "$lib/service/userService";
    import {goto} from "$app/navigation";


    onMount(async () => {
        if (!$auth0Client || !$userDB) {
            const client = await UserService.initializeAuth0Client()
            auth0Client.set(client)
        }
        programError.set('')

        try {
            const programs = await ProgramService.getCoachPrograms($auth0Client, $userDB.coachData!.id!)
            displayPrograms.set(programs.map(p => DisplayProgram.build(p)))
        } catch (e) {
            programError.set('We\'re sorry, but we could not fetch your programs at this time')
        }
    })
</script>

<slot />
{#if $programError}
    <div class="fixed bottom-0 p-2 m-2 border-red border-2 text-red rounded">
        <p>{$programError}</p>
    </div>
{/if}
{#if $programSuccess}
    <div class="fixed bottom-0 p-2 m-2 border-green border-2 text-green rounded">
        <p>{$programSuccess}</p>
    </div>
{/if}

<style>

</style>