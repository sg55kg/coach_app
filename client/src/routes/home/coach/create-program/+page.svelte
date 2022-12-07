<script lang="ts">
    import ProgramForm from "../../../../lib/components/ProgramForm.svelte";
    import type {Program} from "../../../../lib/classes/program";
    import {auth0Client, userDB} from "../../../../lib/stores/authStore";
    import {ProgramService} from "../../../../lib/service/ProgramService";
    import {program, programError} from "../../../../lib/stores/programStore";
    import {onMount} from "svelte";


    const handleSubmit = async (event, programData: Program) => {
        if(!$auth0Client === null) return
        event.preventDefault()

        try {
            const savedProgram = await ProgramService.createProgram($auth0Client, programData, $userDB)
            console.log(savedProgram)
        } catch (e) {
            console.log(e)
            programError.set(e.message)
        }
    }

</script>

<div>
    <ProgramForm handleSubmit={handleSubmit} />
</div>

<style>

</style>