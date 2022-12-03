<script lang="ts">
    import ProgramForm from "../../../lib/components/ProgramForm.svelte";
    import type {Program} from "../../../lib/classes/program";

    const handleSubmit = async (event, programData: Program) => {
        event.preventDefault()

        const res = await fetch('/api/programs', {
            method: 'POST',
            //mode: 'no-cors',
            body: JSON.stringify(programData),
            //headers: { 'Authority': Cookies.get('Authority')}
        })

        const program = await res.json()
        console.log(program)
    }


</script>

<div>
    <ProgramForm handleSubmit={handleSubmit} />
</div>

<style>

</style>