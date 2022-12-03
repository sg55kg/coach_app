<script lang="ts">
    import {program} from "../../../../lib/stores/programStore";
    import {onDestroy, onMount} from "svelte";
    import {Program} from "../../../../lib/classes/program";
    import ProgramForm from "../../../../lib/components/ProgramForm.svelte";
    export let data

    const { programResponse } = data
    console.log(programResponse)

    const handleSubmit = async (event, programData: Program) => {
        event.preventDefault()
        //const data = Object.assign(pro, )
        const res = await fetch('/api/programs', {
            method: 'PUT',
            body: JSON.stringify(programData),
        })

        const program = await res.json()
        console.log(program)
    }

    onMount(() => {
        program.set(Program.build(programResponse))
    })

    onDestroy(() => {
        program.set(new Program())
    })
</script>

{#if $program.id}
    <ProgramForm handleSubmit={handleSubmit} initialIndex={$program.days.length-1} />
{/if}

<style>

</style>