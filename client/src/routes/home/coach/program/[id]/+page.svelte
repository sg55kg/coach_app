<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {Program} from "$lib/classes/program";
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import dayjs from "dayjs";

    export let data
    console.log(data)

    const { programId } = data

    const handleSubmit = async (event, programData: Program) => {
        if (!$auth0Client) return
        event.preventDefault()
        programError.set('')

        // check for new days with invalid ids
        for (const day of programData.days) {
            if (dayjs(day.id).isValid()) {
                day.id = ''
            }
        }
        try {
            const updatedProgram = await ProgramService.updateProgram($auth0Client, programData)
            program.set(Program.build(updatedProgram))
            programSuccess.set('Updated')
        } catch (e) {
            console.log(e)
            programError.set('Could not update program at this time')
        }
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