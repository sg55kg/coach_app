<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {Program} from "$lib/classes/program";
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import dayjs from "dayjs";

    export let data

    const { programDto } = data

    if (programDto) {
        $program = Program.build(programDto)
        program.update(prev => {
            prev.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
            return prev
        })
    }

    const handleSubmit = async (event, programData: Program) => {
        event.preventDefault()
        programError.set('')


        // check for new days with invalid ids
        for (const day of programData.days) {
            if (dayjs(day.id).isValid()) {
                day.id = ''
            }
        }
        try {
            const updatedProgram = await ProgramService.updateProgram(programData)
            program.set(updatedProgram)
            programSuccess.set('Updated')
        } catch (e) {
            console.log(e)
            programError.set('Could not update program at this time')
        }
    }

    onDestroy(() => {
        program.set(new Program())
    })
</script>

{#if $program?.id}
    <ProgramForm handleSubmit={handleSubmit} initialIndex={$program.days.length-1} />
{/if}

<style>

</style>