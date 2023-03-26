<script lang="ts">
    import {onDestroy, onMount} from "svelte";
    import {Program} from "$lib/classes/program";
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import {ProgramService} from "$lib/service/ProgramService";
    import {auth0Client} from "$lib/stores/authStore";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import dayjs from "dayjs";
    import ProgramOverview from "$lib/components/WriteProgram2/ProgramOverview.svelte";
    import MdClose from 'svelte-icons/md/MdClose.svelte'

    export let data

    const { programDto } = data

    let showOverview: boolean = true
    let initialIndex: number = 0

    if (programDto) {
        console.log(programDto)
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
<!--    <div class="relative">-->
<!--        {#if !showOverview}-->
<!--            <ProgramForm handleSubmit={handleSubmit} initialIndex={initialIndex} bind:showOverview={showOverview} />-->
<!--        {:else}-->
<!--            <ProgramOverview bind:showOverview={showOverview} bind:initialIndex={initialIndex} />-->
<!--        {/if}-->
<!--        {#if $programSuccess}-->
<!--            <div class="sticky bottom-5 left-10 z-10 text-green border-l-4 border-l-green bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">-->
<!--                {$programSuccess}-->
<!--                <button class="h-8 w-8 hover:bg-gray-400 text-green-dark rounded-full hover:text-green p-1" on:click={() => $programSuccess = ''}>-->
<!--                    <MdClose />-->
<!--                </button>-->
<!--            </div>-->
<!--        {:else if $programError}-->
<!--            <div class="sticky bottom-5 left-10 z-10 text-red border-l-4 border-l-red-shade bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">-->
<!--                {$programError}-->
<!--                <button class="h-8 w-8 hover:bg-gray-400 rounded-full hover:text-red-shade p-1" on:click={() => $programError = ''}>-->
<!--                    <MdClose />-->
<!--                </button>-->
<!--            </div>-->
<!--        {/if}-->
<!--    </div>-->
    <ProgramOverview selectedProgram={$program} />
{/if}

<style>
    html, body, p, div, h3, button, header {
        -webkit-touch-callout:none;
        -webkit-user-select:none;
    }
</style>