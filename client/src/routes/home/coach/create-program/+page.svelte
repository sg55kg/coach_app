<script lang="ts">
    import type { Program, ProgramDTO } from '$lib/classes/program';
    import { userDB } from '$lib/stores/authStore';
    import { ProgramService } from '$lib/service/ProgramService';
    import {
        program,
        programError,
        programSuccess,
    } from '$lib/stores/writeProgramStore';
    import { DisplayProgram } from '$lib/classes/program';
    import type { PageData } from './$types';
    import ProgramOverview from '$lib/components/WriteProgram/ProgramOverview.svelte';

    export let data: PageData;
    let athleteId: string = '';

    if (data.athleteId) {
        athleteId = data.athleteId;
    }

    const handleSubmit = async (event, programData: Program) => {
        programError.set('');
        try {
            const savedProgram: Program = await ProgramService.createProgram(
                programData
            );
            userDB.update(prev => {
                prev?.coachData?.programs.push(
                    DisplayProgram.build(savedProgram as ProgramDTO)
                );
                return prev;
            });
            if (savedProgram.athleteId === $userDB?.athleteData?.id) {
                userDB.update(prev => {
                    if (!prev?.athleteData?.programs) return prev;
                    prev?.athleteData?.programs.push(savedProgram);
                    if (programData.isCurrent) {
                        prev!.athleteData!.currentProgram = savedProgram;
                    }
                    return prev;
                });
            }
            programSuccess.set('Success!');
            window.location.replace(`/home/coach/${$userDB?.coachData?.id}`);
        } catch (e) {
            console.log(e);
            programError.set(`Error saving ${programData.name}`);
        }
    };
</script>

<svelte:head>
    <title>New Program</title>
    <meta name="description" content="Create a new program" />
</svelte:head>

<ProgramOverview />

<style>
</style>
