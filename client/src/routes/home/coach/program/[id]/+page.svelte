<script lang="ts">
    import {onDestroy} from "svelte";
    import {Program} from "$lib/classes/program";
    import {ProgramService} from "$lib/service/ProgramService";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import dayjs from "dayjs";
    import ProgramOverview from "$lib/components/WriteProgram/ProgramOverview.svelte";

    export let data
    let reloading: boolean = false

    $: programDto = data.programDto
    $: programDto ? (() => {
        reloading = true
        $program.id = ''
        $program = Program.build(programDto)
        program.update(prev => {
            prev.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
            return prev
        })
        setTimeout(() => reloading = false, 100) // hacky work around to get the page to reload after searching program
    })() : null

    let showOverview: boolean = true
    let initialIndex: number = 0

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

{#if $program?.id && !reloading}
    <ProgramOverview selectedProgram={$program} />
{/if}

<style>
    html, body, p, div, h3, button, header {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>