<script lang="ts">
    import ProgramForm from "$lib/components/WriteProgram/ProgramForm.svelte";
    import type {Program, ProgramDTO} from "$lib/classes/program";
    import {auth0Client, userDB} from "$lib/stores/authStore";
    import {ProgramService} from "$lib/service/ProgramService";
    import {program, programError, programSuccess} from "$lib/stores/writeProgramStore";
    import {DisplayProgram} from "$lib/classes/program";



    const handleSubmit = async (event, programData: Program) => {
        if($auth0Client === null || $userDB === null) return
        programError.set('')
        try {
            const savedProgram: Program = await ProgramService.createProgram($auth0Client, programData, $userDB)
            userDB.update(prev => {
                prev?.coachData?.programs.push(DisplayProgram.build(savedProgram as ProgramDTO))
                return prev
            })
            if (savedProgram.athleteId === $userDB.athleteData?.id) {
                userDB.update(prev => {
                    prev?.athleteData?.programs.push(savedProgram)
                    if (programData.isCurrent) {
                        prev!.athleteData!.currentProgram = savedProgram
                    }
                    return prev
                })
            }
            programSuccess.set('Success!')
            window.location.replace('/home/coach')
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