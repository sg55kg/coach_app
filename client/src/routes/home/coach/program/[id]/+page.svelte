<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {Program} from "$lib/classes/program";
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {program} from "$lib/stores/writeProgramStore";

    export let data
    console.log(data)

    const { programId } = data

    const handleSubmit = async (event, programData: Program) => {
        if (!$auth0Client) return
        event.preventDefault()

        const updatedProgram = await ProgramService.updateProgram($auth0Client, programData)

        program.set(Program.build(updatedProgram))
    }

    onMount(async () => {
        console.log(programId)
        if (!$auth0Client) return

        const programRes: Program = await ProgramService.getProgram($auth0Client, programId)
        program.set(programRes)
    })

    onDestroy(() => {
        program.set(new Program())
    })
</script>

{#if $program?.id}
    <ProgramForm handleSubmit={handleSubmit} initialIndex={$program.days.length-1} />
{/if}

<style>

</style>