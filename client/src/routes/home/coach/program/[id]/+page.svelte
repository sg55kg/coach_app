<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {Program} from "$lib/classes/program";
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import dayjs from "dayjs";
    import ProgramOverview from "$lib/components/WriteProgram/ProgramOverview.svelte";

    export let data

    const { programDto } = data

    let showOverview: boolean = true
    let initialIndex: number = 0

    if (programDto) {
        $program = Program.build(programDto)
        program.update(prev => {
            prev.days.forEach(d => d.exercises.sort((a, b) => a.order - b.order))
            return prev
        })
        initialIndex = $program.days.length - 1
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
    <div class="relative">
        {#if !showOverview}
            <ProgramForm handleSubmit={handleSubmit} initialIndex={initialIndex} bind:showOverview={showOverview} />
        {:else}
            <ProgramOverview bind:showOverview={showOverview} bind:initialIndex={initialIndex} />
        {/if}
        {#if $programSuccess}
            <div class="absolute bottom-10 z-10 text-green-dark border-l-2 border-l-green-dark bg-gray-400 shadow-2xl">{$programSuccess}</div>
        {:else if $programError}
            <div class="absolute bottom-10 z-10 text-red border-l-2 border-l-red-shade bg-gray-400 shadow-2xl">{$programError}</div>
        {/if}
    </div>
{/if}

<style>

</style>